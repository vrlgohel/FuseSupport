package com.redhat;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Viral Gohel
 */
public class SiftAppenderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        org.slf4j.MDC.put("app.name", "MyTestApp");
        from("timer:foo?period=3s").setBody().simple("Sift Appender Test : ${header.fireTime}").to("file:out");
    }
}
