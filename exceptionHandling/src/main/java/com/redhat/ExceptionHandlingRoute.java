package com.redhat;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgohel
 */
public class ExceptionHandlingRoute extends RouteBuilder{
    private static final Logger logger= LoggerFactory.getLogger(ExceptionHandlingRoute.class);
    private int count=0;

    @EndpointInject(ref = "sourceDirectoryXml")
    Endpoint sourceDirectoryXml;
    @EndpointInject(ref = "moneyUriXml")
    Endpoint moneyUriXml;
    @EndpointInject(ref = "directErrorHandlerWithException")
    Endpoint directErrorHandlerWithException;
    @EndpointInject(ref = "directErrorHandler")
    Endpoint directErrorHandler;
    @EndpointInject(ref = "directDLQ")
    Endpoint directDLQ;

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
        onException(MyFunctionalException.class).routeId("myfunctional-exception").maximumRedeliveries(0).handled(true)
                .log(LoggingLevel.INFO,"%%% My Functional Exception Handled ....");

        from(sourceDirectoryXml)
                .convertBodyTo(String.class)
                .log(LoggingLevel.INFO,"Message to be handled is : ${file:onlyname} and body is : ${body}")
                .choice()
                .when(xpath("/p:Payments/p:Currency='EUR'").namespace("p","http://www.fusesource.com/training/payment"))
                .log(LoggingLevel.INFO,"This is a EURO Xml Payment, ${file:onlyname}")
                .setHeader("Payments").simple("EUR")
                .to(directErrorHandlerWithException)
                .when(xpath("/p:Payments/p:Currency='USD'").namespace("p","http://www.fusesource.com/training/payment"))
                .log(LoggingLevel.INFO,"This is a USD Xml Payment, ${file:onlyname}")
                .setHeader("Payments").simple("USD")
                .to(directErrorHandler)
                .otherwise()
                .log(LoggingLevel.INFO,"This is the other Xml Payment, ${file:onlyname}")
                .to(moneyUriXml);

        from(directErrorHandlerWithException)
                .log(LoggingLevel.INFO,"The message will be processed only once ....")
                .beanRef("myBeanProcess");
        from(directErrorHandler)
                .log(LoggingLevel.INFO,"Message is now going to start redelivery .....")
                .errorHandler(deadLetterChannel(directDLQ).maximumRedeliveries(2))
                .log(LoggingLevel.INFO,"The message will be processed more than once, 2 times ..")
                .beanRef("myBeanProcess");
        from(directDLQ).log(LoggingLevel.INFO,"This is the DLQ message ...");
    }
}
