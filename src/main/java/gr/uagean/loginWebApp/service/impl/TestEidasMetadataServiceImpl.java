/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.service.impl;

import gr.uagean.loginWebApp.service.TestEidasMetadataService;
import org.springframework.stereotype.Service;

/**
 *
 * @author nikos
 */
@Service
public class TestEidasMetadataServiceImpl implements TestEidasMetadataService {

    
    private String meta = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><md:EntityDescriptor xmlns:md=\"urn:oasis:names:tc:SAML:2.0:metadata\" entityID=\"http://84.205.248.180:80/EidasNode/ConnectorResponderMetadata\" validUntil=\"2017-08-10T16:51:36.316Z\"><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/><ds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512\"/><ds:Reference URI=\"\"><ds:Transforms><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha512\"/><ds:DigestValue>m1gaP+kBC4us9fUgIwTLhE0SNMk087K0XNe7Mlmo9WdEZ15TY+iANobpcolC1MPTkBPNofsfBrMF1B1Uo8zZPQ==</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>gS3VT/nAGcUwTqZPjn4ItTcaN/+DGIJlcDpTH4KVYzdlYAlngTew3Bdr4FhMn+XOilN9A6FXDt1IhSNCghimvKnRCkw4fofQKW9ei9CgWFx2uAuFudBNJmuRk4Eye8/hVrn4XUo2Rbu+NDlP1W/+QCWcy6fxvSwMI3FDBTjWwHjvLzU47yslSXFnX30h2yQ9FIYtGvcNCu9pKu29DxtRTcecgP/3U+3oAoc7RaO7LIexBdfXdb+DHvgSa5T9OcFPo3F1spsIcENr9PjYr+YQsvhymVhi0kklFULVYFPp6YdDAoYx8elr4Ydy/zIV1RTt1Qxc+i09/aCJlWQc88/BVNLUZYsgEuct+vsSxOx2rIJKL52S7ju71J/+mSDt0s3UbTLz2JN1r56Wv9LD+8hHmjyu674nr5meFwSbJo2YbcGMrF4tmZQJBG1eSIEIN5rCvfjKkhZUOZ0Wm4A8Nmz0deue6HwenYt8k++9wsQ+STTxnhtH95UuZmZIWBxznnRbIefce1DT184njPTXninyUdTxmmvTcra3KWqsgiY1NuMrwShkR3wSdwRqtqn+wPEJtYkT8jT5VA1wuVUyUQSTPGbHVICNp769+KRMTrb43juyRL33H1L/jy9ky85BhFFx4uR4QPnvaX7NX2hBY6BWSTWIu/QLeiBBBHQXXd32p4U=</ds:SignatureValue><ds:KeyInfo><ds:X509Data><ds:X509Certificate>MIIFMTCCAxkCBFYbwMgwDQYJKoZIhvcNAQENBQAwXTELMAkGA1UEBhMCQkUxCzAJBgNVBAgMAkVV\n" +
"MREwDwYDVQQHDAhCcnVzc2VsczELMAkGA1UECgwCRUMxDjAMBgNVBAsMBURJR0lUMREwDwYDVQQD\n" +
"DAhtZXRhZGF0YTAeFw0xNTEwMTIxNDE2NDBaFw0xNjEwMTExNDE2NDBaMF0xCzAJBgNVBAYTAkJF\n" +
"MQswCQYDVQQIDAJFVTERMA8GA1UEBwwIQnJ1c3NlbHMxCzAJBgNVBAoMAkVDMQ4wDAYDVQQLDAVE\n" +
"SUdJVDERMA8GA1UEAwwIbWV0YWRhdGEwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCN\n" +
"5mYsOKzSJ9ksT9dHtFOztF1M8GIMeBLm6chIvtKHwXVLzO53RKhcOwt0j847VL6m5PcAAp57SutC\n" +
"DeukA8p6UCUA905p+m7+dt7iEsUV3yje4M8dDFS/LwEF9GhFm3v471ZRYPDW769v14QkmnA9vxWB\n" +
"WAj4WcMRMats9choHJdnRa1xUnVjx8yMojoVaPwt1tkG/rRnPev2o0g+VI63XkYd1pLKAU5Pt+n7\n" +
"LevLPJsvgkKLQhEB7bvMG1tZ1P4fJ0n3FOHmfLHJ/yEWA+hHXorX5T3G8Fq6GsI5O/c1zkZ7QMSw\n" +
"WwzXDbq5qrfyzesdlTPfdsPnFIRddCgx8NuVwI+brlYDSdLGEm+byfM9W4WmfDN6SK1oGMSibuz7\n" +
"K49Xh0MFVKNyxT9hCz309UiV71RGnveZxdMGu4vdzP74Ll3G48IIgQ4ymFPMONYBesuorxDunSqs\n" +
"R2F1+Th7k7UXL1xblFRaEyqdHlvhVrJqDP6sM9k3lM75aN4L4QMOyKRAqar+Q7f7NoUcx8cvHfqD\n" +
"GLJUPcqn2msMa3mAXO5ihA2ERN41wmnmeJzsd/UiFkaqIvXUTZVwxUfQWn3D9uCg2lRAvOTHydkP\n" +
"Cfwj4BtL0P9L3eSZ9NM8IGlTmlyApp2bPlzO92BsE8RE7feOmSLZESDKosqkQzZo2CMr/7V9XQID\n" +
"AQABMA0GCSqGSIb3DQEBDQUAA4ICAQALfSi+sa90MbJkAeTIA/la1ibtRkPX6jIjHBvkeq8IYEZi\n" +
"XxjJvI4CuQY6WSPMoDY0w9iJvKIygCxRlVi77CtFzu/otOLrXb8ozInopykRMIH4TyVmKYf//CoE\n" +
"fkQ3vThaf1JLpKpLuhtqHwV03f7jwODaJBqvqdaBX3VHHMPDOeAWQTAd2abMoHgYRlUgB9TKcbJ1\n" +
"akWUyX7hnwZSCiKWbL4nrwsFJc0skFVkfjEQxlZUeRXj/bKgnb0BYUsPsFfxXKJIsIc8CmXGvxKz\n" +
"B5TSpYIR79WliT9Fo8T1dJ9a/wr+bOXeM/aSUxLechCl+uDuP8yI2iRz9LT++/16HOrRSUuefHpo\n" +
"7wJLJnALMABW21eMwS2XBInUBrBN9CVGAJUDF6GQWMbfxA8x0uh4oKoa/4stP5maaf/FBe52pNNv\n" +
"Tacb7P3xJc0mS7jatuAHH0UfXy3+3D3z+SJY4Vy2a1cj5U1nUuxxwIRwsoRtWph0BER4RlOz4lXS\n" +
"N8ZK9ahgmCsndm+eDvIJm706s7bd8m/X8Xc/lMK+eKhrK6uIIMmkwbdzbgsOS7Plj9IMGm0S4Kdb\n" +
"rnAKhkhAXUi4zbd55aTx1kDodpid/dYPiqxSauyYmCXKbyFCAfY76Zw9SuFBRJClx4h5Mxb/EEpq\n" +
"1WHM9IyZshufnuZ587WzqtGmJJubTA==</ds:X509Certificate></ds:X509Data></ds:KeyInfo></ds:Signature><md:Extensions><eidas:SPType xmlns:eidas=\"http://eidas.europa.eu/saml-extensions\">public</eidas:SPType><alg:DigestMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#sha384\"/><alg:DigestMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha512\"/><alg:DigestMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha384\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2007/05/xmldsig-more#sha256-rsa-MGF1\"/><alg:SigningMethod xmlns:alg=\"urn:oasis:names:tc:SAML:metadata:algsupport\" Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512\"/></md:Extensions><md:IDPSSODescriptor WantAuthnRequestsSigned=\"true\" protocolSupportEnumeration=\"urn:oasis:names:tc:SAML:2.0:protocol\"><md:KeyDescriptor use=\"signing\"><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:X509Data><ds:X509Certificate>MIIFTTCCAzUCBFTI/IgwDQYJKoZIhvcNAQENBQAwazELMAkGA1UEBhMCQ0ExCzAJBgNVBAgMAkVV\n" +
"MQswCQYDVQQHDAJFVTEOMAwGA1UECgwFU1BFUFMxDjAMBgNVBAsMBVNUT1JLMSIwIAYDVQQDDBlz\n" +
"cGVwcy1jYS1kZW1vLWNlcnRpZmljYXRlMB4XDTE1MDEyODE1MTMxMloXDTE2MDEyODE1MTMxMlow\n" +
"azELMAkGA1UEBhMCQ0ExCzAJBgNVBAgMAkVVMQswCQYDVQQHDAJFVTEOMAwGA1UECgwFU1BFUFMx\n" +
"DjAMBgNVBAsMBVNUT1JLMSIwIAYDVQQDDBlzcGVwcy1jYS1kZW1vLWNlcnRpZmljYXRlMIICIjAN\n" +
"BgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAhFXC/GDKjHA0aXwU+xIv+2GyfSjQ2L1ZS1asC5AY\n" +
"OLa1PC2TWiljbOj1vRXGlNXG4ue91nQkpMBNg3nMmZtdhP2vfEtT+9V2INQR+1aQ8plPGWNIOX1O\n" +
"5NZ/F2M/daAZ2K3Y+WSKLin7FCdBL6Bw0IGFmQUeblIrumMoeRpQhXBfrDHyFW/ozNeB6M9x2bTD\n" +
"mk/hjJO8l/auxsPjYzU2+rENS+kEKr10HRy8MTqtfcqVft5blCAUOeDctvsf/5s37JK2MBGjgX6X\n" +
"mchLQylOpUZrBo7cNTfc77RjobdgN5rWUyZ8WeEYHtfyUo+nF7DHP2euiuhueFW7Gind+MrcAudI\n" +
"JI5r27x4jhneyul1a5OWbGUP84lOoIpzCkvE0w7HbpItDxbnuX09KBmm8lKUingB5CaqG5D+APCW\n" +
"T3emo1HIkLX3Jq8OLXNu7c9jPB1xGk4b6ZC2fhCLwvv37bAahWZ81WkqzWLaZpyAKZ+vRCa7WMtt\n" +
"HfhVEJmEU/wCgfvv2l1mvCz61H6AQWzEd8ZLwiZnljgDEtxq8sRNZHQVPvBnC9TKyzy6D1/+sZ7R\n" +
"8p4AxFHX5UxG+qEtEiz5qXFswXU8gBsKpDO9cG6n7C8Bg8uhsx1TfWJeoe1mtJpKTAvJbqE/O2ak\n" +
"QMHu0C2D+fou3jzWjoDfrMJi+9CVqsyICRcCAwEAATANBgkqhkiG9w0BAQ0FAAOCAgEAbywum+pY\n" +
"trXnRmtI2zrun7Jum7UpxSlVjW1MUvt0vZHNgunAkDivF1eOXsXQR1boXdjsuvRw3ERMhdpibn7I\n" +
"mvFAmMrYMez5PNEusqQ+pzIz2OPa7JIjemx6emZToUKhJ5iqjDlL+J5O7ov6+FrjjlRjfPsMWJdC\n" +
"zu3Nk0TBX1pM2PWDs2NoITCOjMI8jv+Ur9gqH8mopYf1SF94RjNKqiSQxAeXr0BM6aWbZzeTXP5z\n" +
"Mwlbm0icqAF26YMZgEypCR3UhJmlhgFVd/fPuao6laWS5tsKoYhGXz7llheY1gPNVp28pE6PkD3S\n" +
"egbZu2RM9iry0G+v46O+gwYDIuXCyA9BRagiSek4NN9NoHFrLEtbN5PlxcV7UavIxVc+ql/DTq/9\n" +
"2d/ENzay/MV1kF2ZHJz+TeOEOWXe0mErN2IDuDrJ1oXXAJZMsV3cn/EUn2NZ1d8mhQB1DpPIzWck\n" +
"8PKzzo8+EEWMxYrL3vjrH9SPE0QW2TnMovSvcomg9Zqyd79NCpJkKCt2FKsBziKBcIcjoFj98+9B\n" +
"kXexiIiotyQKHKJSScDy6ruhidU5C7jinwrWDhTiEhkpycKviqUm40P3ZCEbFhy0IasjV+pN8edd\n" +
"gf4TBex/kF5TeuRD0fBZaQijozdeVlx4lej1SiiUZg+bONGvmrJrkLLlrrjft/7NdP4=</ds:X509Certificate></ds:X509Data></ds:KeyInfo></md:KeyDescriptor><md:KeyDescriptor use=\"encryption\"><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:X509Data><ds:X509Certificate>MIIFTTCCAzUCBFTI/IgwDQYJKoZIhvcNAQENBQAwazELMAkGA1UEBhMCQ0ExCzAJBgNVBAgMAkVV\n" +
"MQswCQYDVQQHDAJFVTEOMAwGA1UECgwFU1BFUFMxDjAMBgNVBAsMBVNUT1JLMSIwIAYDVQQDDBlz\n" +
"cGVwcy1jYS1kZW1vLWNlcnRpZmljYXRlMB4XDTE1MDEyODE1MTMxMloXDTE2MDEyODE1MTMxMlow\n" +
"azELMAkGA1UEBhMCQ0ExCzAJBgNVBAgMAkVVMQswCQYDVQQHDAJFVTEOMAwGA1UECgwFU1BFUFMx\n" +
"DjAMBgNVBAsMBVNUT1JLMSIwIAYDVQQDDBlzcGVwcy1jYS1kZW1vLWNlcnRpZmljYXRlMIICIjAN\n" +
"BgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAhFXC/GDKjHA0aXwU+xIv+2GyfSjQ2L1ZS1asC5AY\n" +
"OLa1PC2TWiljbOj1vRXGlNXG4ue91nQkpMBNg3nMmZtdhP2vfEtT+9V2INQR+1aQ8plPGWNIOX1O\n" +
"5NZ/F2M/daAZ2K3Y+WSKLin7FCdBL6Bw0IGFmQUeblIrumMoeRpQhXBfrDHyFW/ozNeB6M9x2bTD\n" +
"mk/hjJO8l/auxsPjYzU2+rENS+kEKr10HRy8MTqtfcqVft5blCAUOeDctvsf/5s37JK2MBGjgX6X\n" +
"mchLQylOpUZrBo7cNTfc77RjobdgN5rWUyZ8WeEYHtfyUo+nF7DHP2euiuhueFW7Gind+MrcAudI\n" +
"JI5r27x4jhneyul1a5OWbGUP84lOoIpzCkvE0w7HbpItDxbnuX09KBmm8lKUingB5CaqG5D+APCW\n" +
"T3emo1HIkLX3Jq8OLXNu7c9jPB1xGk4b6ZC2fhCLwvv37bAahWZ81WkqzWLaZpyAKZ+vRCa7WMtt\n" +
"HfhVEJmEU/wCgfvv2l1mvCz61H6AQWzEd8ZLwiZnljgDEtxq8sRNZHQVPvBnC9TKyzy6D1/+sZ7R\n" +
"8p4AxFHX5UxG+qEtEiz5qXFswXU8gBsKpDO9cG6n7C8Bg8uhsx1TfWJeoe1mtJpKTAvJbqE/O2ak\n" +
"QMHu0C2D+fou3jzWjoDfrMJi+9CVqsyICRcCAwEAATANBgkqhkiG9w0BAQ0FAAOCAgEAbywum+pY\n" +
"trXnRmtI2zrun7Jum7UpxSlVjW1MUvt0vZHNgunAkDivF1eOXsXQR1boXdjsuvRw3ERMhdpibn7I\n" +
"mvFAmMrYMez5PNEusqQ+pzIz2OPa7JIjemx6emZToUKhJ5iqjDlL+J5O7ov6+FrjjlRjfPsMWJdC\n" +
"zu3Nk0TBX1pM2PWDs2NoITCOjMI8jv+Ur9gqH8mopYf1SF94RjNKqiSQxAeXr0BM6aWbZzeTXP5z\n" +
"Mwlbm0icqAF26YMZgEypCR3UhJmlhgFVd/fPuao6laWS5tsKoYhGXz7llheY1gPNVp28pE6PkD3S\n" +
"egbZu2RM9iry0G+v46O+gwYDIuXCyA9BRagiSek4NN9NoHFrLEtbN5PlxcV7UavIxVc+ql/DTq/9\n" +
"2d/ENzay/MV1kF2ZHJz+TeOEOWXe0mErN2IDuDrJ1oXXAJZMsV3cn/EUn2NZ1d8mhQB1DpPIzWck\n" +
"8PKzzo8+EEWMxYrL3vjrH9SPE0QW2TnMovSvcomg9Zqyd79NCpJkKCt2FKsBziKBcIcjoFj98+9B\n" +
"kXexiIiotyQKHKJSScDy6ruhidU5C7jinwrWDhTiEhkpycKviqUm40P3ZCEbFhy0IasjV+pN8edd\n" +
"gf4TBex/kF5TeuRD0fBZaQijozdeVlx4lej1SiiUZg+bONGvmrJrkLLlrrjft/7NdP4=</ds:X509Certificate></ds:X509Data></ds:KeyInfo><md:EncryptionMethod Algorithm=\"http://www.w3.org/2009/xmlenc11#aes192-gcm\"/><md:EncryptionMethod Algorithm=\"http://www.w3.org/2009/xmlenc11#aes256-gcm\"/><md:EncryptionMethod Algorithm=\"http://www.w3.org/2009/xmlenc11#aes128-gcm\"/></md:KeyDescriptor><md:NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:persistent</md:NameIDFormat><md:NameIDFormat>urn:oasis:names:tc:SAML:2.0:nameid-format:transient</md:NameIDFormat><md:NameIDFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified</md:NameIDFormat><md:SingleSignOnService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\" Location=\"http://84.205.248.180:80/EidasNode/ServiceProvider\"/><md:SingleSignOnService Binding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect\" Location=\"http://84.205.248.180:80/EidasNode/ServiceProvider\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"D-2012-17-EUIdentifier\" Name=\"http://eidas.europa.eu/attributes/legalperson/D-2012-17-EUIdentifier\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"EORI\" Name=\"http://eidas.europa.eu/attributes/legalperson/EORI\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"LEI\" Name=\"http://eidas.europa.eu/attributes/legalperson/LEI\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"LegalAddress\" Name=\"http://eidas.europa.eu/attributes/legalperson/LegalAddress\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"LegalName\" Name=\"http://eidas.europa.eu/attributes/legalperson/LegalName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"LegalPersonIdentifier\" Name=\"http://eidas.europa.eu/attributes/legalperson/LegalPersonIdentifier\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"SEED\" Name=\"http://eidas.europa.eu/attributes/legalperson/SEED\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"SIC\" Name=\"http://eidas.europa.eu/attributes/legalperson/SIC\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"TaxReference\" Name=\"http://eidas.europa.eu/attributes/legalperson/TaxReference\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"VATRegistration\" Name=\"http://eidas.europa.eu/attributes/legalperson/VATRegistration\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"BirthName\" Name=\"http://eidas.europa.eu/attributes/naturalperson/BirthName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"CurrentAddress\" Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentAddress\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"FamilyName\" Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"FirstName\" Name=\"http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"DateOfBirth\" Name=\"http://eidas.europa.eu/attributes/naturalperson/DateOfBirth\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"EidasAdditionalAttribute\" Name=\"http://eidas.europa.eu/attributes/naturalperson/EidasAdditionalAttribute\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"Gender\" Name=\"http://eidas.europa.eu/attributes/naturalperson/Gender\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"PersonIdentifier\" Name=\"http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/><saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" FriendlyName=\"PlaceOfBirth\" Name=\"http://eidas.europa.eu/attributes/naturalperson/PlaceOfBirth\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:uri\"/></md:IDPSSODescriptor><md:Organization><md:OrganizationName xml:lang=\"en\">Sample Country Connector</md:OrganizationName><md:OrganizationDisplayName xml:lang=\"en\">CA</md:OrganizationDisplayName><md:OrganizationURL xml:lang=\"en\">http://localhost:8080/EidasNode/</md:OrganizationURL></md:Organization><md:ContactPerson contactType=\"support\"><md:Company>eIDAS Connector Operator</md:Company><md:GivenName>John</md:GivenName><md:SurName>Doe</md:SurName><md:EmailAddress>contact.support@eidas-connector.eu</md:EmailAddress><md:TelephoneNumber>+40 123456</md:TelephoneNumber></md:ContactPerson><md:ContactPerson contactType=\"technical\"><md:Company>eIDAS Connector Operator</md:Company><md:GivenName>John</md:GivenName><md:SurName>Doe</md:SurName><md:EmailAddress>contact.technical@eidas-connector.eu</md:EmailAddress><md:TelephoneNumber>+41 123456</md:TelephoneNumber></md:ContactPerson></md:EntityDescriptor>";
    
    
    
    
    @Override
    public String getMetadata() {
        return meta;
    }
    
}
