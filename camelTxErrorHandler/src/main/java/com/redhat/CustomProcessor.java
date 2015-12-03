package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgohel on 8/31/15.
 */
public class CustomProcessor implements Processor {
    private Logger logger= LoggerFactory.getLogger(CustomProcessor.class);

    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    public void process(Exchange exchange) throws Exception {
        logger.info("Doing nothing ....");
        Thread.sleep(12000);
        logger.info("Processing the messages : " + exchange.getIn().getBody());
    }
}
