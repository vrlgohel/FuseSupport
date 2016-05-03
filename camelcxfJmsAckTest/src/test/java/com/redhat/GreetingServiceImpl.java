package com.redhat;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService
public class GreetingServiceImpl implements GreetingService{
    public String echo(String name) {
        return "Hi : " + name;
    }
}
