/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service;

import eu.eidas.sp.SpAuthenticationResponseData;

/**
 *
 * @author nikos
 */
public interface EidasDecryptService {

    public SpAuthenticationResponseData processResponse(String SAMLResponse, String remoteHost);
}
