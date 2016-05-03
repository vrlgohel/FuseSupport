package com.redhat;

import javax.jws.WebService;

/**
 * Created by Viral Gohel
 */
@WebService
public interface GreetingService {
    public String echo(String name);
}
