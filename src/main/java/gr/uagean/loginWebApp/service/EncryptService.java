/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service;

import eu.eidas.sp.SpAuthenticationRequestData;
import java.util.ArrayList;

/**
 *
 * @author nikos
 */
public interface EncryptService {
    public SpAuthenticationRequestData getSAMLReq(ArrayList<String> pal, String citizenCountry, String serviceProviderCountry);
}
