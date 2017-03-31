package com.redhat.test;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService(serviceName = "HelloWS",portName = "hello")
public interface HelloWs {
    public ReturnDate hello(String name);
}
