package com.redhat;

import com.redhat.samples.ws.GreetingService;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService
public class GreetingServiceImpl implements GreetingService{
    public String goodbye(String name) {
        return "GoodBye : " + name;
    }

    public String hello(String name) {
        return "Hello :" + name ;
    }
}
