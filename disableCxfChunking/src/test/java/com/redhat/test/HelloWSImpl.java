package com.redhat.test;

import org.apache.cxf.annotations.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.Date;

/**
 * Created by Viral Gohel
 */
@WebService(serviceName = "HelloWS",portName = "hello")
@Logging(pretty = true)
public class HelloWSImpl implements HelloWs {
    private Logger logger= LoggerFactory.getLogger(this.getClass().getName());

    public ReturnDate hello(String name)
    {
        logger.info("Hello, " + name);
        ReturnDate d = new ReturnDate();
        if(name.startsWith("K"))
        {
            return d;
        }
        else
        {
            d.setDate(new Date());
            return d;
        }
    }
}
