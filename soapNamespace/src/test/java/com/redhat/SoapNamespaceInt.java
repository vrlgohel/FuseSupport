package com.redhat;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService
public interface SoapNamespaceInt {
    public String sayHi(String name);
}
