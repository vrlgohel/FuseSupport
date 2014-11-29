package com.redhat;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;

/**
 * Created by vgohel
 */
public class AggregateRoute extends RouteBuilder implements InitializingBean{
    private Logger log= LoggerFactory.getLogger(this.getClass().getName());
    private String sourceDirectory;
    private String sinkDirectory;
    private boolean createDirectories;
    private int aggregateTimoutPeriod;

    String sourceUri;
    String sinkUri;
    public void setAggregateTimoutPeriod(int aggregateTimoutPeriod) {
        this.aggregateTimoutPeriod = aggregateTimoutPeriod;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setSinkDirectory(String sinkDirectory) {
        this.sinkDirectory = sinkDirectory;
    }

    public void setCreateDirectories(boolean createDirectories) {
        this.createDirectories = createDirectories;
    }

    Namespaces namespaces=new Namespaces("p","http://www.fusesource.com/training/payment")
            .add("xsd", "http://www.w3.org/2001/XMLSchema");


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
        from(sourceUri)
                .split().xpath("/p:Payments/p:Payment",namespaces)
                .convertBodyTo(String.class)
                .aggregate(new BodyAppenderAggregator())
                .xpath("p:Payment/p:to",String.class,namespaces)
                .completionTimeout(0)
                .to(sinkUri);

    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (sourceDirectory==null||"".equals(sourceDirectory.trim()))
            throw new BeanInitializationException("You must set a value for the sourceDirectory : " + sourceDirectory);

        File sourceFile=new File(sourceDirectory);
        if (sourceFile.exists()){
            boolean noDirectory=false;
            if (createDirectories)
                noDirectory=sourceFile.mkdirs();
            if (!noDirectory)
                throw new BeanInitializationException("Given sourceDirectory : " + sourceFile + "does not exists");
        }
        if (!sourceFile.canRead())
            throw new BeanInitializationException("The sourceDirectory : " + sourceFile + "is not readable");
    }
}
