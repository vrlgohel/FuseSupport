package com.redhat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Endpoint;

/**
 * Created by vgohel
 */
public class TriggerRealService {
    private Logger logger= LoggerFactory.getLogger(TriggerRealService.class);
    private String url;
    private ServiceImpl service=new ServiceImpl();
    private Endpoint endpoint;

    public void setUrl(String url) {
        this.url = url;
    }

    public void start() throws Exception{
        logger.info("Starting the Real Web Service ....");
        endpoint.publish(url,service);
        logger.info("Started the real Web Service at : " + url);
    }

    public void stop() throws Exception {
        if (endpoint != null) {
            logger.info("Stopping real web service ....");
            endpoint.stop();
            logger.info("The real web service is stopped ....");
        }
    }
}
