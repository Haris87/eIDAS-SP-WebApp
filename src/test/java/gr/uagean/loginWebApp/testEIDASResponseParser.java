/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uagean.loginWebApp.utils.eIDASResponseParser;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author nikos
 */
public class testEIDASResponseParser {

    String testResponse = "'AuthenticationResponse{id='_YLc6H3WhE2mjssJZHnJyOIvuRFBPIHsfszeGwVzAipyXS2csl7SlpVbKjUo4UOp', issuer='http://84.205.248.180:80/EidasNode/ConnectorResponderMetadata', status='ResponseStatus{failure='false', statusCode='urn:oasis:names:tc:SAML:2.0:status:Success', statusMessage='urn:oasis:names:tc:SAML:2.0:status:Success', subStatusCode='null'}', ipAddress='null', inResponseToId='_BJK3gNxljIfI.hOeabwBjO5ZFE54BPHQXmG9gEoXNb.BMgIN4LRZRzY18-ZyG6m', levelOfAssurance='http://eidas.europa.eu/LoA/low', attributes='{AttributeDefinition{nameUri='http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName', friendlyName='FamilyName', personType=NaturalPerson, required=true, transliterationMandatory=true, uniqueIdentifier=false, xmlType='{http://eidas.europa.eu/attributes/naturalperson}CurrentFamilyNameType', attributeValueMarshaller='eu.eidas.auth.commons.attribute.impl.StringAttributeValueMarshaller'}=[cph8], AttributeDefinition{nameUri='http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName', friendlyName='FirstName', personType=NaturalPerson, required=true, transliterationMandatory=true, uniqueIdentifier=false, xmlType='{http://eidas.europa.eu/attributes/naturalperson}CurrentGivenNameType', attributeValueMarshaller='eu.eidas.auth.commons.attribute.impl.StringAttributeValueMarshaller'}=[cph8], AttributeDefinition{nameUri='http://eidas.europa.eu/attributes/naturalperson/DateOfBirth', friendlyName='DateOfBirth', personType=NaturalPerson, required=true, transliterationMandatory=false, uniqueIdentifier=false, xmlType='{http://eidas.europa.eu/attributes/naturalperson}DateOfBirthType', tributeValueMarshaller='eu.eidas.auth.commons.attribute.impl.DateTimeAttributeValueMarshaller'}=[1966-01-01], AttributeDefinition{nameUri='http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier', friendlyName='PersonIdentifier', personType=NaturalPerson, required=true, transliterationMandatory=false, uniqueIdentifier=true, xmlType='{http://eidas.europa.eu/attributes/naturalperson}PersonIdentifierType',attributeValueMarshaller='eu.eidas.auth.commons.attribute.impl.LiteralStringAttributeValueMarshaller'}=[CA/CA/Cph123456]}', audienceRestriction='http://138.68.103.237:8090/metadata', notOnOrAfter='2017-09-16T08:16:21.191Z', notBefore='2017-09-16T08:11:21.191Z', country='CA', encrypted='false'}'";

    @Test
    public void testEidasResponseParser() {
        Map<String, String> map = eIDASResponseParser.parse(testResponse);
        for (String key : map.keySet()) {
            System.out.println("key" + key + " vlaue: " + map.get(key));
        }

    }

    @Test
    public void testJSONString() throws JsonProcessingException {
        String expected = "{\"firstName\":\"cph8\",\"eid\":\"CA/CA/Cph123456\",\"familyName\":\"cph8\",\"personIdentifier\":\"CA/CA/Cph123456\",\"dateOfBirth\":\"1966-01-01\"}";
        Map<String, String> map = eIDASResponseParser.parse(testResponse);
        ObjectMapper mapper = new ObjectMapper();
       
        assertEquals(mapper.writeValueAsString(map),expected);
    }

}
