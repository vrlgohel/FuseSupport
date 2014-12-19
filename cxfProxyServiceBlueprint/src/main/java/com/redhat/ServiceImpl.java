package com.redhat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgohel
 */
public class ServiceImpl implements Greeter {
    private Logger logger= LoggerFactory.getLogger(ServiceImpl.class);

    @Override
    public void pingMe() throws PingMeFault {
        logger.info("Invoked the real Web Service pingMe() !");
    }

    @Override
    public void greetMeOneWay(String requestType) {
        logger.info("Invoked the real Wen Service greetOneWay() !");
    }

    @Override
    public String greetMe(String requestType) {
        return "The response from greetMe() is : " + requestType;
    }

    @Override
    public String sayHi(String requestType) {
        return "The response from sayHi() is : " + requestType;
    }
}
