package com.redhat;

public class OrderProcessor {
	public String processIncomingOrder(String orderPayload) {
		// this is just an example. in a real project, we'd unmarsall the request and do something
		// useful with the message.
		return orderPayload.concat( " [DONE] ");
	}
}
