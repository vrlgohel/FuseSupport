package com.redhat;

import org.apache.cxf.annotations.Logging;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService(targetNamespace = "http://redhat.com/")
public class SoapNamespaceImpl implements SoapNamespaceInt {
    public String sayHi(String name) {
        return "Hi: " +name;
    }
}
