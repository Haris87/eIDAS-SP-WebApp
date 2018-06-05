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
import gr.uagean.loginWebApp.service.KeyStoreService;
import gr.uagean.loginWebApp.service.ParameterService;
import gr.uagean.loginWebApp.utils.CookieUtils;
import gr.uagean.loginWebApp.utils.JwtUtils;
import gr.uagean.loginWebApp.utils.eIDASResponseParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private ParameterService paramServ;
    @Autowired
    private KeyStoreService keyServ;

    @Value("${eidas.error.consent}")
    private String EIDAS_CONSENT_ERROR;
    @Value("${eidas.error.qaa}")
    private String EIDAS_QAA_ERROR;
    @Value("${eidas.error.missing}")
    private String EIDAS_MISSING_ATTRIBUTE_ERROR;

    private final static Logger LOG = LoggerFactory.getLogger(RestControllers.class);

//    private final static String SP_BACKEND = System.getenv("SP_BACKEND");
    private final static String SP_COUNTRY = System.getenv("SP_COUNTRY");
    private final static String SP_SUCCESS_PAGE = System.getenv("SP_SUCCESS_PAGE");
    private final static String SP_FAIL_PAGE = System.getenv("SP_FAIL_PAGE");
    private final static String SECRET = System.getenv("SP_SECRET");

    final static String UAEGEAN_LOGIN = "UAEGEAN_LOGIN";
    final static String LINKED_IN_SECRET = "LINKED_IN_SECRET";
//    final static String SECRET = "SP_SECRET";

    final static String CLIENT_ID = "CLIENT_ID";
    final static String REDIRECT_URI = "REDIRECT_URI";
    final static String HTTP_HEADER = "HTTP_HEADER";
    final static String URL_ENCODED = "URL_ENCODED";

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
            HttpServletResponse response, RedirectAttributes redirectAttrs) {

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
        if (data.getResponseXML().contains("202007") || data.getResponseXML().contains("202004")
                || data.getResponseXML().contains("202012") || data.getResponseXML().contains("202010")
                || data.getResponseXML().contains("003002")) {
            //"title", "Registration/Login Cancelled"
            if (data.getResponseXML().contains("202007") || data.getResponseXML().contains("202012")) {
                redirectAttrs.addFlashAttribute("title", "Registration/Login Cancelled");
                redirectAttrs.addFlashAttribute("errorMsg", EIDAS_CONSENT_ERROR);
            }
            if (data.getResponseXML().contains("202004")) {
                redirectAttrs.addFlashAttribute("title", "Registration/Login Cancelled");
                redirectAttrs.addFlashAttribute("errorMsg", EIDAS_QAA_ERROR);
            }
            if (data.getResponseXML().contains("202010")) {
                redirectAttrs.addFlashAttribute("title", "Registration/Login Cancelled");
                redirectAttrs.addFlashAttribute("errorMsg", EIDAS_MISSING_ATTRIBUTE_ERROR);
            }

            //003002
            if (data.getResponseXML().contains("003002")) {
                redirectAttrs.addFlashAttribute("title", "Non-sucessful authentication");
                redirectAttrs.addFlashAttribute("errorMsg", "Please, return to the home page and re-initialize the process. If the authentication fails again, please contact your national eID provider");
            }

            return "redirect:/authfail";
        }

        try {
            Map<String, String> jsonMap = eIDASResponseParser.parse(data.getResponseXML());
//            access_token = Jwts.builder()
//                    .setSubject(mapper.writeValueAsString(jsonMap))
//                    .signWith(SignatureAlgorithm.HS256, SECRET.getBytes("UTF-8"))
//                    .compact();
            String origin = jsonMap.get("eid").contains("aegean") ? "UAegean" : "eIDAS";
            access_token = JwtUtils.getJWT(jsonMap, paramServ, keyServ, origin);
            if (paramServ.getParam(HTTP_HEADER) != null && Boolean.parseBoolean(paramServ.getParam(HTTP_HEADER))) {
                response.setHeader("Authorization", access_token);
            } else {

                if (paramServ.getParam(URL_ENCODED) != null && Boolean.parseBoolean(paramServ.getParam(URL_ENCODED))) {
                    return "redirect:" + paramServ.getParam(SP_SUCCESS_PAGE) + "?login=" + access_token;
                } else {
                    Cookie cookie = new Cookie("access_token", access_token);
                    cookie.setPath("/");
                    CookieUtils.addDurationIfNotNull(cookie, paramServ);
                    response.addCookie(cookie);
                }
            }
            return "redirect:" + paramServ.getParam(SP_SUCCESS_PAGE);

        } catch (Exception e) {
            LOG.info(e.getMessage());
            return "redirect:" + SP_FAIL_PAGE;
        }
//        return "redirect:" + SP_SUCCESS_PAGE;//+ "?token=" + token;

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
