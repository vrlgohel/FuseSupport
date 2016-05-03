package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Viral Gohel
 */
public class DummyProcessor implements Processor {
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    public void process(Exchange exchange) throws Exception {
        String me = exchange.getIn().getBody(String.class);
        exchange.getOut().setBody("Hello " + me);
        throw new Exception();
    }
}
