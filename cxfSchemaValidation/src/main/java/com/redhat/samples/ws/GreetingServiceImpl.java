package com.redhat.samples.ws;

import org.apache.cxf.annotations.SchemaValidation;

/**
 * Author: Viral G.Gohel
 * Date: 6/29/15
 **/
//@SchemaValidation
public class GreetingServiceImpl implements GreetingService {
    public String goodbye(String name) {
        return "GoodBye : " + name;
    }

    public String hello(String name) {
        return "Hello : " + name;
    }
}
