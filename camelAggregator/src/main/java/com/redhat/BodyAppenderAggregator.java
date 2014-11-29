package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgohel
 */
public class BodyAppenderAggregator implements AggregationStrategy{

    private Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Aggregates an old and new exchange together to create a single combined exchange
     *
     * @param oldExchange the oldest exchange (is <tt>null</tt> on first aggregation as we only have the new exchange)
     * @param newExchange the newest exchange (can be <tt>null</tt> if there was no data possible to acquire)
     * @return a combined composite of the two exchanges
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        logger.info("Old : " + (oldExchange==null ? "null" : oldExchange.getIn().getBody()) + ",new :" + newExchange.getIn().getBody());
        if (oldExchange==null) {
            Exchange exchange = new DefaultExchange(newExchange);
            exchange.getIn().setBody(newExchange.getIn().getBody());
            return exchange;
        }else {
            String newBody =
                    oldExchange.getIn().getBody(String.class)
                            + newExchange.getIn().getBody(String.class);
            oldExchange.getIn().setBody( newBody, String.class );
            return oldExchange;
        }
    }
}
