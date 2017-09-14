/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service.impl;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.UnmodifiableIterator;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.ImmutableAttributeMap;
import eu.eidas.auth.commons.protocol.IRequestMessage;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssurance;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssuranceComparison;
import eu.eidas.auth.commons.protocol.eidas.impl.EidasAuthenticationRequest;
import eu.eidas.auth.commons.protocol.impl.EidasSamlBinding;
import eu.eidas.auth.engine.ProtocolEngineI;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;
import eu.eidas.sp.SPUtil;
import eu.eidas.sp.SpAuthenticationRequestData;
import gr.uagean.loginWebApp.service.EncryptService;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.stereotype.Service;

/**
 *
 * @author nikos
 */
@Service
public class EncryptServiceImpl implements EncryptService {

    private final Properties configs = SPUtil.loadSPConfigs();
    private final String spId = configs.getProperty("provider.name");
    private final String providerName = configs.getProperty("provider.name");
    private final String nodeUrl = configs.getProperty("country1.url");
    private final int qaaLvl = Integer.parseInt(configs.getProperty("sp.qaalevel"));
    private final static String SP_RETURN_URL = System.getenv("SP_RETURN_URL");
    private final static String SP_METADATA_URL = System.getenv("SP_METADATA_URL");

    @Override
    public SpAuthenticationRequestData getSAMLReq(ArrayList<String> pal, String citizenCountry, String serviceProviderCountry) {
        System.out.println(java.nio.file.Paths.get(".", new String[0]).toAbsolutePath().normalize().toString());

        ProtocolEngineI protocolEngine = eu.eidas.auth.engine.ProtocolEngineFactory.getDefaultProtocolEngine("SP");

        EidasAuthenticationRequest.Builder reqBuilder = new EidasAuthenticationRequest.Builder();

        ImmutableSortedSet<AttributeDefinition<?>> allSupportedAttributesSet = protocolEngine.getProtocolProcessor().getAllSupportedAttributes();
        List<AttributeDefinition<?>> reqAttrList = new ArrayList(allSupportedAttributesSet);

        for (UnmodifiableIterator localUnmodifiableIterator = allSupportedAttributesSet.iterator(); localUnmodifiableIterator.hasNext();) {
            AttributeDefinition<?> attributeDefinition = (AttributeDefinition) localUnmodifiableIterator.next();

            String attributeName = attributeDefinition.getNameUri().toASCIIString();

            System.out.println("Checking " + attributeName);
            if (!pal.contains(attributeName)) {
                reqAttrList.remove(attributeDefinition);
            }
        }
        ImmutableAttributeMap reqAttrMap = new ImmutableAttributeMap.Builder().putAll(reqAttrList).build();
        reqBuilder.requestedAttributes(reqAttrMap);

        reqBuilder.destination(nodeUrl);
        reqBuilder.providerName(providerName);

        reqBuilder.levelOfAssurance(LevelOfAssurance.LOW.stringValue());
        if (qaaLvl == 3) {
            reqBuilder.levelOfAssurance(LevelOfAssurance.SUBSTANTIAL.stringValue());
        } else if (qaaLvl == 4) {
            reqBuilder.levelOfAssurance(LevelOfAssurance.HIGH.stringValue());
        }
        reqBuilder.spType("public");
        reqBuilder.levelOfAssuranceComparison(LevelOfAssuranceComparison.fromString("minimum").stringValue());
        reqBuilder.nameIdFormat("urn:oasis:names:tc:saml:1.1:nameid-format:unspecified");
        reqBuilder.binding(EidasSamlBinding.EMPTY.getName());

//        reqBuilder.issuer(configs.getProperty("sp.metadata.url"));
        reqBuilder.issuer(SP_METADATA_URL);
        reqBuilder.serviceProviderCountryCode(serviceProviderCountry);
        reqBuilder.citizenCountryCode(citizenCountry);

        IRequestMessage binaryRequestMessage = null;
        String ncName = null;
        try {
            ncName = eu.eidas.auth.engine.xml.opensaml.SAMLEngineUtils.generateNCName();
            reqBuilder.id(ncName);
            EidasAuthenticationRequest authnRequest = (EidasAuthenticationRequest) reqBuilder.build();
            binaryRequestMessage = protocolEngine.generateRequestMessage(authnRequest, configs.getProperty("country1.metadata.url"));

            System.out.println(">>>>>>>>>>>> " + authnRequest.getId() + "-" + authnRequest.getIssuer());
        } catch (EIDASSAMLEngineException e) {
            String errorMessage = "Could not generate token for Saml Request: " + e.getMessage();
            System.out.println(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Could not generate token for Saml Request: " + e.getMessage();
            System.out.println(errorMessage);
            e.printStackTrace();
        }
        byte[] token = binaryRequestMessage.getMessageBytes();

        SpAuthenticationRequestData data = new SpAuthenticationRequestData(EidasStringUtil.encodeToBase64(token), binaryRequestMessage.getRequest().getId());
        return data;
    }

}
