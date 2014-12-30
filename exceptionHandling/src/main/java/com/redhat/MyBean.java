package com.redhat;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgohel
 */
public class MyBean {
    private static final Logger logger = LoggerFactory.getLogger(MyBean.class);
    private int counter = 0;

    public void process(Exchange exchange) throws Exception {

        String payment = (String) exchange.getIn().getHeader("Payments");
        logger.info(">>>> Payment type = "+ payment);
        if (payment.equals("EUR")) {
            counter++;
            logger.info(">>>> Exception created for : " + payment + ", counter = " + counter);
            throw new MyFunctionalException(">>>> MyFunctionalException created.");
        } else if (payment.equals("USD")) {
            counter++;
            logger.info(">>>> Exception created for : " + payment + ", counter = " + counter);
            throw new Exception("===> Exception created");
        } else {
            logger.info(">>>> No match for payment type of " + payment);
        }

    }
}
