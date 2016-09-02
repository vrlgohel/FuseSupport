package com.redhat.client;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Viral Gohel
 */
public class CamelHeadersProcessor implements Processor{
    private Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    public void process(Exchange exchange) throws Exception {
        Map<String, Object> headMap = exchange.getIn().getHeaders();
        Iterator<Map.Entry<String, Object>> entries = headMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            logger.info("Key : " + entry.getKey() + ", Value : " + entry.getValue());
        }
        exchange.getIn().setBody(exchange);
    }
}
