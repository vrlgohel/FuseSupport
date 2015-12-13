WS-Trust (JAX-WS CXF STS sample)
=================================

Provides an example of a CXF SOAP client accessing a CXF STS for a SAML
assertion and then subsequently making a call to a CXF web service provider
(WSP).  X.509 authentication is used for the WSC->STS call. Sample keystores
and truststores for the WSC, WSP, and STS are provided but they
are not meant for production use.

Important Note:  By default, this example uses strong encryption which is 
recommended for use in production systems.

- To execute the example, just type, `mvn test`.