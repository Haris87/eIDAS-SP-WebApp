/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.AttributeValue;
import eu.eidas.auth.commons.attribute.ImmutableAttributeMap;
import eu.eidas.auth.commons.attribute.ImmutableAttributeMap.Builder;
import eu.eidas.auth.commons.protocol.IAuthenticationResponse;
import eu.eidas.auth.commons.protocol.IRequestMessage;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssurance;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssuranceComparison;
import eu.eidas.auth.commons.protocol.eidas.impl.EidasAuthenticationRequest;
import eu.eidas.auth.commons.protocol.impl.EidasSamlBinding;
import eu.eidas.auth.engine.ProtocolEngineI;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;
import eu.eidas.sp.ApplicationSpecificServiceException;
import eu.eidas.sp.SPUtil;
import eu.eidas.sp.SpAuthenticationResponseData;
import eu.eidas.sp.SpProtocolEngineFactory;
import eu.eidas.sp.SpProtocolEngineI;
import gr.uagean.loginWebApp.service.EidasDecryptService;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author nikos
 */
@Service
public class EidasDecryptServiceImpl implements EidasDecryptService {

    
    private final static Logger logger  = LoggerFactory.getLogger(EidasDecryptService.class);
    private final static String SP_METADATA_URL = System.getenv("SP_METADATA_URL");
    private final static String SP_RETURN_URL = System.getenv("SP_RETURN_URL");
    
    @Override
    public SpAuthenticationResponseData processResponse(String SAMLResponse, String remoteHost) {
        ArrayList<String[]> pal = new ArrayList();
        ApplicationSpecificServiceException exception = null;
        Properties configs = SPUtil.loadSPConfigs();

        
//        String metadataUrl = configs.getProperty("sp.metadata.url");
        String metadataUrl = configs.getProperty(SP_METADATA_URL);

        byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(SAMLResponse);
        String samlResponseXML = EidasStringUtil.toString(decSamlToken);
        System.out.println("-----samlResponseXML------");
        System.out.println(samlResponseXML);
        IAuthenticationResponse response = null;
        try {
            SpProtocolEngineI engine = SpProtocolEngineFactory.getSpProtocolEngine("SP");

            response = engine.unmarshallResponseAndValidate(decSamlToken, remoteHost, 0L, metadataUrl);
            System.out.println("-----response------");
            System.out.println(response);

            pal = eIDAS2PAL(response.getAttributes());

        } catch (EIDASSAMLEngineException e) {

            if (StringUtils.isEmpty(e.getErrorDetail())) {
                exception = new ApplicationSpecificServiceException("EIDASSAMLEngineException", e.getErrorMessage());
            } else {
                exception = new ApplicationSpecificServiceException("Exception", e.getErrorDetail());
            }
        }
        if (exception != null) {
            logger.error(exception.getMessage());
            throw exception;
        }

        SpAuthenticationResponseData data = new SpAuthenticationResponseData(pal, response.getId(), response.getInResponseToId(), response.toString(), response.getStatusCode(), response.getStatusMessage(), response.getLevelOfAssurance());
        return data;
    }

    private static ArrayList<String[]> eIDAS2PAL(ImmutableAttributeMap eidasPal) {
        ArrayList<String[]> pal = new ArrayList();
        ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> map = eidasPal.getAttributeMap();
        for (UnmodifiableIterator localUnmodifiableIterator1 = map.keySet().iterator(); localUnmodifiableIterator1.hasNext();) {
            AttributeDefinition<?> key = (AttributeDefinition) localUnmodifiableIterator1.next();

            ArrayList<String> pa = new ArrayList();

            String attrName = key.getNameUri().toString();
            ImmutableList<? extends AttributeValue<?>> vals = ((ImmutableSet) map.get(key)).asList();
            System.out.println(attrName);
            pa.add(attrName);
            pa.add("" + key.isRequired());
            AttributeValue<?> val;
            for (UnmodifiableIterator localUnmodifiableIterator2 = vals.iterator(); localUnmodifiableIterator2.hasNext(); pa.add(val.toString())) {
                val = (AttributeValue) localUnmodifiableIterator2.next();
            }
            pal.add(pa.toArray(new String[0]));
        }
        return pal;
    }

}
