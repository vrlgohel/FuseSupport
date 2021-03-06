package org.oasis_open.docs.ws_sx.ws_trust._200512;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

/**
 * This class was generated by Apache CXF 3.0.4
 * 2015-12-13T16:33:33.503+05:30
 * Generated source version: 3.0.4
 * 
 */
@WebService(targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/", name = "STS")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface STS {

    @WebMethod(operationName = "Issue", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTRC/IssueFinal")
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    public RequestSecurityTokenResponseCollectionType issue(
        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        AbstractRequestSecurityTokenType request
    );

    @WebMethod(operationName = "Cancel", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Cancel", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/CancelFinal")
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    public AbstractRequestSecurityTokenType cancel(
        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        AbstractRequestSecurityTokenType request
    );

    @WebMethod(operationName = "Validate", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Validate", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/ValidateFinal")
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    public AbstractRequestSecurityTokenType validate(
        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        AbstractRequestSecurityTokenType request
    );

    @WebMethod(operationName = "RequestCollection", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/RequestCollection")
    @WebResult(name = "RequestSecurityTokenResponseCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "responseCollection")
    public RequestSecurityTokenResponseCollectionType requestCollection(
        @WebParam(partName = "requestCollection", name = "RequestSecurityTokenCollection", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        RequestSecurityTokenCollectionType requestCollection
    );

    @WebMethod(operationName = "KeyExchangeToken", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KeyExchangeToken")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/KET", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/KETFinal")
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    public AbstractRequestSecurityTokenType keyExchangeToken(
        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        AbstractRequestSecurityTokenType request
    );

    @WebMethod(operationName = "Renew", action = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew")
    @Action(input = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Renew", output = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RSTR/RenewFinal")
    @WebResult(name = "RequestSecurityTokenResponse", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512", partName = "response")
    public AbstractRequestSecurityTokenType renew(
        @WebParam(partName = "request", name = "RequestSecurityToken", targetNamespace = "http://docs.oasis-open.org/ws-sx/ws-trust/200512")
        AbstractRequestSecurityTokenType request
    );
}
