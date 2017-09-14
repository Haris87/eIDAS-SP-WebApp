/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.controllers;

import eu.eidas.sp.SpAuthenticationRequestData;
import eu.eidas.sp.SpEidasSamlTools;
import gr.uagean.loginWebApp.service.CountryService;
import gr.uagean.loginWebApp.service.EidasPropertiesService;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    final static Logger LOG = LoggerFactory.getLogger(ViewControllers.class);

   

    @Autowired
    private CountryService countryServ;

    @RequestMapping("/login")
    public ModelAndView loginView(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("nodeUrl", SpEidasSamlTools.getNodeUrl());
        mv.addObject("countries", countryServ.getEnabled());
        mv.addObject("spFailPage",System.getenv(SP_FAIL_PAGE));
        mv.addObject("spSuccessPage",System.getenv(SP_SUCCESS_PAGE));
        mv.addObject("logo",SP_LOGO);
        return mv;
    }

}
