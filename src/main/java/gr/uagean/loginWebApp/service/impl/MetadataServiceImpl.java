/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service.impl;

import eu.eidas.auth.engine.ProtocolEngineFactory;
import gr.uagean.loginWebApp.service.MetadataService;

import eu.eidas.auth.engine.metadata.MetadataConfigParams;
import eu.eidas.auth.engine.metadata.MetadataGenerator;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;
import eu.eidas.sp.Constants;
import eu.eidas.sp.SPUtil;

import eu.eidas.auth.engine.configuration.dom.EncryptionKey;
import eu.eidas.auth.engine.configuration.dom.SignatureKey;
import eu.eidas.auth.engine.metadata.Contact;

import static eu.eidas.sp.Constants.SP_CONF;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author nikos
 */
@Service
public class MetadataServiceImpl implements MetadataService {

    private final Properties configs = SPUtil.loadSPConfigs();
    private static final Logger logger = LoggerFactory.getLogger(MetadataServiceImpl.class);
    private final static String SP_METADATA_URL = System.getenv("SP_METADATA_URL");
    private final static String SP_RETURN_URL = System.getenv("SP_RETURN_URL");
    
    @Override
    public String getMetadata() {
        String metadata = "error gen metadata";
        if (SPUtil.isMetadataEnabled()) {
            try {
                MetadataGenerator generator = new MetadataGenerator();
                MetadataConfigParams mcp = new MetadataConfigParams();
                generator.setConfigParams(mcp);
                generator.initialize(ProtocolEngineFactory.getDefaultProtocolEngine(SP_CONF));
//                mcp.setEntityID(configs.getProperty(Constants.SP_METADATA_URL));
                mcp.setEntityID(SP_METADATA_URL);
                generator.addSPRole();
//                String returnUrl = configs.getProperty(Constants.SP_RETURN);
//                String returnUrl = configs.getProperty(SP_RETURN_URL);
                mcp.setAssertionConsumerUrl(SP_RETURN_URL);
                mcp.setTechnicalContact(getTechnicalContact(generator.getContactStrings()));
                mcp.setSupportContact(getSupportContact(generator.getContactStrings()));
                mcp.setSigningMethods(configs == null ? null : configs.getProperty(SignatureKey.SIGNATURE_ALGORITHM_WHITE_LIST.getKey()));
                mcp.setDigestMethods(configs == null ? null : configs.getProperty(SignatureKey.SIGNATURE_ALGORITHM_WHITE_LIST.getKey()));
                mcp.setEncryptionAlgorithms(configs == null ? null : configs.getProperty(EncryptionKey.ENCRYPTION_ALGORITHM_WHITE_LIST.getKey()));
                mcp.setOrganizationName(configs == null ? null : configs.getProperty(MetadataConfigParams.ORG_NAME));
                metadata = generator.generateMetadata();
            } catch (EIDASSAMLEngineException see) {
                logger.error("error generating metadata {}", see);
            }
        }
        return metadata;

    }

 
    private Contact getSupportContact(String[][] source) {
        return createContact(source[1]);
    }

    private Contact createContact(String[] propsNames) {
        Contact contact = new Contact();
        contact.setCompany(propsNames != null && propsNames.length > 0 && configs != null ? configs.getProperty(propsNames[0]) : null);
        contact.setEmail(propsNames != null && propsNames.length > 1 && configs != null ? configs.getProperty(propsNames[1]) : null);
        contact.setGivenName(propsNames != null && propsNames.length > 2 && configs != null ? configs.getProperty(propsNames[2]) : null);
        contact.setSurName(propsNames != null && propsNames.length > 3 && configs != null ? configs.getProperty(propsNames[3]) : null);
        contact.setPhone(propsNames != null && propsNames.length > 4 && configs != null ? configs.getProperty(propsNames[4]) : null);
        return contact;
    }

    private Contact getTechnicalContact(String[][] source) {
        return createContact(source[0]);
    }

     

}
