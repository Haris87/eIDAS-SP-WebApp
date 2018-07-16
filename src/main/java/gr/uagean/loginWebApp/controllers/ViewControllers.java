/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.controllers;

import eu.eidas.sp.SpEidasSamlTools;
import gr.uagean.loginWebApp.model.pojo.LinkedInAuthAccessToken;
import gr.uagean.loginWebApp.service.CountryService;
import gr.uagean.loginWebApp.service.EidasPropertiesService;
import gr.uagean.loginWebApp.service.KeyStoreService;
import gr.uagean.loginWebApp.service.ParameterService;
import gr.uagean.loginWebApp.utils.CookieUtils;
import gr.uagean.loginWebApp.utils.JwtUtils;
import gr.uagean.loginWebApp.utils.LinkedInResponseParser;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author nikos
 */
@Controller
public class ViewControllers {

    final static String EIDAS_URL = "EIDAS_NODE_URL";
    final static String SP_FAIL_PAGE = "SP_FAIL_PAGE";
    final static String SP_SUCCESS_PAGE = "SP_SUCCESS_PAGE";
    final static String SP_LOGO = System.getenv("SP_LOGO");

    final static String UAEGEAN_LOGIN = "UAEGEAN_LOGIN";
    final static String LINKED_IN_SECRET = "LINKED_IN_SECRET";
    final static String SECRET = "SP_SECRET";

    final static String CLIENT_ID = "CLIENT_ID";
    final static String REDIRECT_URI = "REDIRECT_URI";
    final static String HTTP_HEADER = "HTTP_HEADER";
    final static String URL_ENCODED = "URL_ENCODED";
    final static String URL_PREFIX = "URL_PREFIX";

    final static Logger LOG = LoggerFactory.getLogger(ViewControllers.class);

    @Autowired
    private EidasPropertiesService propServ;

    @Autowired
    private CountryService countryServ;

    @Autowired
    private KeyStoreService keyServ;

    @Autowired
    private ParameterService paramServ;

    @RequestMapping("/login")
    public ModelAndView loginView(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("nodeUrl", SpEidasSamlTools.getNodeUrl());
        mv.addObject("countries", countryServ.getEnabled());
        mv.addObject("spFailPage", System.getenv(SP_FAIL_PAGE));
        mv.addObject("spSuccessPage", System.getenv(SP_SUCCESS_PAGE));
        mv.addObject("logo", SP_LOGO);

        mv.addObject("legal", propServ.getLegalProperties());
        mv.addObject("natural", propServ.getNaturalProperties());
        String urlPrefix = StringUtils.isEmpty(paramServ.getParam(URL_PREFIX)) ? "" : paramServ.getParam(URL_PREFIX);
        mv.addObject("urlPrefix", urlPrefix);
        return mv;
    }

    @RequestMapping("/authfail")
    public String authorizationFail(@RequestParam(value = "t", required = false) String token,
            @RequestParam(value = "reason", required = false) String reason,
            Model model) {

        if (reason != null) {
            if (reason.equals("disagree")) {
                model.addAttribute("errorType", "DISAGREE");
            } else {
                model.addAttribute("errorType", "CANCEL");
            }
            model.addAttribute("title", "");
            model.addAttribute("errorMsg", "Registration/Login Cancelled");
        }
        String urlPrefix = StringUtils.isEmpty(paramServ.getParam(URL_PREFIX)) ? "" : paramServ.getParam(URL_PREFIX);
        model.addAttribute("urlPrefix", urlPrefix);

        model.addAttribute("server", System.getenv("SP_SERVER"));
        return "authfail";
    }

    @RequestMapping(value = "/linkedInResponse", method = {RequestMethod.POST, RequestMethod.GET})
    public String linkedInResponse(@RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String errorDescription,
            HttpServletResponse httpResponse) {

        //TODO Before you accept the authorization code, your application should ensure that the value returned in the state parameter matches the state value from your original authorization code request.
        if (org.apache.commons.lang3.StringUtils.isEmpty(error)) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("grant_type", "authorization_code");
            map.add("code", code);
            map.add("redirect_uri", paramServ.getParam(REDIRECT_URI));
            map.add("client_id", paramServ.getParam(CLIENT_ID));
            map.add("client_secret", paramServ.getParam(LINKED_IN_SECRET));

            HttpEntity<MultiValueMap<String, String>> request
                    = new HttpEntity<>(map, headers);

            ResponseEntity<LinkedInAuthAccessToken> response = restTemplate
                    .exchange("https://www.linkedin.com/oauth/v2/accessToken", HttpMethod.POST, request, LinkedInAuthAccessToken.class);

            //TODO get User Data using accessToken 
            HttpHeaders headersUser = new HttpHeaders();
            headersUser.setContentType(MediaType.APPLICATION_JSON);
            headersUser.set("Authorization", "Bearer " + response.getBody().getAccess_token());
            HttpEntity<String> entity = new HttpEntity<String>("", headersUser);
            ResponseEntity<String> userResponse
                    = restTemplate.exchange("https://www.linkedin.com/v1/people/~:(id,firstName,lastName,email-address)?format=json",
                            HttpMethod.GET, entity, String.class); //user details https://www.linkedin.com/v1/people/~

            //return "token " + response.getBody().getAccess_token() + " , expires " + response.getBody().getExpires_in();
            //return userResponse.getBody();
            try {
                Map<String, String> jsonMap = LinkedInResponseParser.parse(userResponse.getBody());
                String access_token = JwtUtils.getJWT(jsonMap, paramServ, keyServ, "linkedIn");

                if (paramServ.getParam(HTTP_HEADER) != null && Boolean.parseBoolean(paramServ.getParam(HTTP_HEADER))) {
                    httpResponse.setHeader("Authorization", access_token);
                } else {
                    Cookie cookie = new Cookie("access_token", access_token);
                    cookie.setPath("/");
                    CookieUtils.addDurationIfNotNull(cookie, paramServ);
                    httpResponse.addCookie(cookie);
                }

                if (paramServ.getParam(HTTP_HEADER) != null && Boolean.parseBoolean(paramServ.getParam(HTTP_HEADER))) {
                    httpResponse.setHeader("Authorization", access_token);
                } else {

                    if (paramServ.getParam(URL_ENCODED) != null && Boolean.parseBoolean(paramServ.getParam(URL_ENCODED))) {
                        return "redirect:" + paramServ.getParam(SP_SUCCESS_PAGE) + "?login=" + access_token;
                    } else {
                        Cookie cookie = new Cookie("access_token", access_token);
                        cookie.setPath("/");
                        CookieUtils.addDurationIfNotNull(cookie, paramServ);
                        httpResponse.addCookie(cookie);

                    }
                }

                return "redirect:" + paramServ.getParam(SP_SUCCESS_PAGE);

            } catch (Exception e) {
                LOG.info("Exception", e);
            }

        }

        return "state" + state + " , code" + code;
    }

}
