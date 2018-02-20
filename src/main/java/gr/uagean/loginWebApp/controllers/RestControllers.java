/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.eidas.sp.SpAuthenticationRequestData;
import eu.eidas.sp.SpAuthenticationResponseData;
import eu.eidas.sp.SpEidasSamlTools;
import eu.eidas.sp.metadata.GenerateMetadataAction;
import gr.uagean.loginWebApp.service.EidasPropertiesService;
import gr.uagean.loginWebApp.service.NetworkService;
import gr.uagean.loginWebApp.utils.eIDASResponseParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author nikos
 */
@Controller
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
public class RestControllers {

    @Autowired
    private EidasPropertiesService propServ;
    @Autowired
    private CacheManager cacheManager;
//    @Autowired
//    private NetworkService netServ;

    private final static Logger LOG = LoggerFactory.getLogger(RestControllers.class);

//    private final static String SP_BACKEND = System.getenv("SP_BACKEND");
    private final static String SP_COUNTRY = System.getenv("SP_COUNTRY");
    private final static String SP_SUCCESS_PAGE = System.getenv("SP_SUCCESS_PAGE");
    private final static String SP_FAIL_PAGE = System.getenv("SP_FAIL_PAGE");
    private final static String SECRET = System.getenv("SP_SECRET");

    @RequestMapping(value = "/metadata", method = {RequestMethod.POST, RequestMethod.GET}, produces = {"application/xml"})
    public @ResponseBody
    String metadata() {
        GenerateMetadataAction metaData = new GenerateMetadataAction();
        return metaData.generateMetadata().trim();
//        return metServ.getMetadata().trim();
    }

    @RequestMapping(value = "/generateSAMLToken", method = {RequestMethod.GET})
    public ResponseEntity getSAMLToken(@RequestParam(value = "citizenCountry", required = true) String citizenCountry) {

        String serviceProviderCountry = SP_COUNTRY;
        try {
            ArrayList<String> pal = new ArrayList();
            pal.addAll(propServ.getEidasProperties());
            SpAuthenticationRequestData data
                    = SpEidasSamlTools.generateEIDASRequest(pal, citizenCountry, serviceProviderCountry);
//            return ResponseEntity.ok(encryptServ.getSAMLReq(pal, citizenCountry, serviceProviderCountry).getSaml());
            return ResponseEntity.ok(data.getSaml());
        } catch (NullPointerException e) {
            LOG.error("NulPointer Caught", e);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/eidasResponse", method = {RequestMethod.POST, RequestMethod.GET})
    public String eidasResponse(@RequestParam(value = "SAMLResponse", required = false) String samlResponse,
            HttpServletResponse response) {

        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();

//        LOG.info("DATA" + samlResponse);
//        LOG.info("remoteAddress" + remoteAddress);
//        SpAuthenticationResponseData data = decryptServ.processResponse(samlResponse, remoteAddress);//SpEidasSamlTools.processResponse(samlResponse, remoteAddress);
        SpAuthenticationResponseData data = SpEidasSamlTools.processResponse(samlResponse, remoteAddress);

        UUID token = UUID.randomUUID();
        LOG.info("token " + token);

        ArrayList<String[]> pal = data.getAttributes();
        LOG.info("Reponse ID: " + data.getID());
        LOG.info("ReponseToID: " + data.getResponseToID());
        LOG.info("Status Code: " + data.getStatusCode());
        LOG.info("Status Message: " + data.getStatusMessage());
        pal.stream().map(attrArray -> {
            LOG.info("Attribute Name:" + attrArray[0]);
            LOG.info("Mandatory:" + attrArray[1]);
            LOG.info("Attribute Value: " + attrArray[2]);
            return null;
        });

        cacheManager.getCache("eidasResponses").put(token, data.getResponseXML());

        List<NameValuePair> urlParameters = new ArrayList();
        urlParameters.add(new NameValuePair("eidasResponse", data.getResponseXML()));
        urlParameters.add(new NameValuePair("token", token.toString()));

        String access_token;//            return "redirect:" + SP_FAIL_PAGE;
        ObjectMapper mapper = new ObjectMapper();
        LOG.info("FINAL DATA" + data.getResponseXML());
        try {
            Map<String, String> jsonMap = eIDASResponseParser.parse(data.getResponseXML());
            access_token = Jwts.builder()
                    .setSubject(mapper.writeValueAsString(jsonMap))
                    .signWith(SignatureAlgorithm.HS256, SECRET.getBytes("UTF-8"))
                    .compact();
            Cookie cookie = new Cookie("access_token", access_token);
            cookie.setPath("/");
            int maxAge = Integer.parseInt(System.getenv("AUTH_DURATION"));
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
            
        } catch (Exception e) {
            LOG.info(e.getMessage());
            
        }
        return "redirect:" + SP_SUCCESS_PAGE ;//+ "?token=" + token;

    }

    @RequestMapping(value = "/authToken", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    String getByToken(@RequestParam(value = "token", required = true) String token) {
        Cache.ValueWrapper optionalValue = cacheManager.getCache("eidasResponses").get(token);
        if (optionalValue == null) {
            return "UNAUTHORIZED_TOKEN";
        } else {
            return (String) optionalValue.get();
        }
    }

}
