package com.redhat;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Viral Gohel
 */
public class EncryptionRoute extends RouteBuilder{
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement
     * the routes using the Java fluent builder syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        from("{{start.endpoint}}")
                .setHeader("customPassword",simple("{{custom.password"))
                .log(LoggingLevel.INFO,"{{log.message}}")
                .to("{{end.endpoint}}");

    }
}
