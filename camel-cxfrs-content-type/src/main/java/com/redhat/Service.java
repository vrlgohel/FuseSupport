package com.redhat;

@javax.ws.rs.Path("/")
public interface Service {
	@javax.ws.rs.POST()
	@javax.ws.rs.Produces("text/xml")
	Object invoke();
}
