# eIDAS WebApp 2.0

## Introduction

This project was developed in the context of (and for the needs of) the Greek eIDAS Node.

It is comprised of a Java WebApp that streamlines and greatly speeds up the integration of a Service Provider to the Greek eIDAS node, in such a way that no
knowledge of the eIDAS SAML  profile is required. Additionally, it provides pre-built UI for guiding the users through eIDAS authentication flow. 

It was developed by the "Information Management Lab (i4M Lab)", participant of the Atlantis Group (http://www.atlantis-group.gr/) of the University of the Aegean (https://www.aegean.gr/).

## Project Purpose

The eIDAS WebApp 2.0 facilitates the integration of a SP with the eIDAS node by:

- Allowing the SP to avoid development time for processing SAML messages

- Completely handling an eIDAS-based authentication flow (including UIs)

- Being SP infrastructure independent; operates over a simple REST API

- Providing strong security assertions in the form of JWT ( RFC 7519)

Important Note: In order to use the WebApp 2.0 for integration with the eIDAS GR node, it needs to be deployed in the same domain as the SP

## Deployment

Internally the eIDAS WebApp 2.0 uses the eIDAS SAML library presented https://github.com/ellak-monades-aristeias/eIDAS-SP-SAML-Tools-Library . Thus, the same steps presented in the deployment of the SAML library are required. These can be summed up to:

- The copying of the configEidas folder

- Configure the keystore (eidasKeystore.jks)

- Edit the configuration files (SignModule_SP.xml, EncryptModule_SP.xml, sp.properties)

After configuring the SAML library we can proceed with the configuration of the eIDAS WebApp 2.0. 
This WebApp is offered as a Docker image. Thus, in order to deploy the WebApp 2.0 the hosting machine must have a functional Docker engine.
 For instructions of how to setup Docker please refer to: https://docs.docker.com/install/ and follow the installation instructions depending on your hosting system (linux, windows, mac). Additionally for easier configuration of the container it is assumed that Docker compose is also installed (for instructions on installing docker compose please also refer to: https://docs.docker.com/compose/install/ ) although it is not a requirement.

```
version: '3'

services:

loginWebApp2:

image: endimion13/eidas-gr-loginwebapp:3.1

ports:

- 9080:8090

- 9090:8090

environment:

-EIDAS_PROPERTIES=http://eidas.europa.eu/attributes/naturalperson/CurrentFamilyName,

http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName,

http://eidas.europa.eu/attributes/naturalperson/DateOfBirth,

http://eidas.europa.eu/attributes/naturalperson/PersonIdentifier

- SP_FAIL_PAGE= http://www.host.com/authfail

- SP_SUCCESS_PAGE=http://www.host.com/loginSuccess

- SP_METADATA_URL=http://www.host.com:9090/metadata

- SP_RETURN_URL=http://www.host.com:9090/eidasResponse

- SP_LOGO=http://www.host.com/university-of-the-aegean.png

- SP_CONFIG_REPOSITORY=/configEidas/

- SP_SECRET=secret

- AUTH_DURATION=1800

volumes:

- /configEidas:/configEidas

```

The various parts of this file are explained below:

- Version: denotes the syntax version of the composer file (up to the user for compatibility with the examples provided in this document please use 2 or 3
- Services: denotes the start of the service sector of the compose file
- loginWebApp2: denotes the name of the Thin WebApp 2.0 service name (up to the user to pick a friendly name). Format; hostmachineport:containerport
- Image: the image to use to build the container
- Ports: list of host machine ports that will be mapped to container ports (note that you should always map to container port 8090)
- environment: list of environmental variables that will be added to the container
- EIDAS_PROPERTIES: comma separated properties the SP requires from eIDAS
- SP_FAIL_PAGE: SP url to redirect to in case of authentication failure
- SP_SUCCESS_PAGE: SP url to redirect to in case of authentication success
- SP_METADATA_URL: WebApp 2.0 endpoint that the eIDAS node will use to retrieve the required communication metadata (do not change)
- SP_RETURN_URL: WebApp 2.0 endpoint that the eIDAS node will redirect to after the authentication
- SP_LOGO: a url containing the logo of the SP for UI customization
- SP_CONFIG_REPOSITORY: path to the /configEidas directory (either do not edit this or edit it so that it matches the mounting of the /configEidas directory in the volumes section)
- SP_SECRET: string that will be used on HS256 signature of the generated assertions that will be propagated to the SP.
- AUTH_DURATION: integer denoting the duration for which the authentication cookie will be stored
- volumes: list of native directories that will be mounted as container directories. Here the mounting of the /configEidas directory should take place. Format: nativeDir:containerDir

Deployment now is simply a matter of starting the services defined in such a compose file, for example: docker-compose -f loginService.yml up

## Integration

In order for the SP to interact with the WebApp 2.0 it needs to follow the next steps (in the rest of this section we assume that the WebApp 2.0 is deployed at http://www.host.com:8090 ): · Upon authentication request, redirect the user to http://www.host.com:8090/login

- Build an endpoint (e.g. http://www.host.com/authsuccess) that the WebApp 2.0 will redirect to in case of authentication success
- (optionally) Build an endpoint (e.g. http://www.host.com/authfail ) that the WebApp 2.0 will redirect to in case of authentication failure

The success and fail endpoints will consume a JSON Web Token (JWT) generated by the WebApp 2.0 delivered to the SP in the form of a cookie. JSON Web Tokens are an open, industry standard RFC 7519 method for representing claims securely between two parties. For this reason the WebApp 2.0 needs to be deployed in the same domain as the SP. In the case of a successful authentication this token will contain the retrieved eIDAS identification attributes of the user. In case of an authentication failure this token will contain the authentication error message.
The generated authentication token is signed using HS256 (HMAC with SHA-256) with a secret shared between the SP and the WebApp 2.0. Upon receiving the token the SP should validate it and read the identification attributes from its payload. The format of the generated JWT token payload is presented in the following table:

```
{ "sub": "{

\"eid\":\eidValue\",

\"personIdentifier\":\"eidValue\",

...

\"eIDASAttribute_friendlyName\":\"attributeValue\""}"

}
```