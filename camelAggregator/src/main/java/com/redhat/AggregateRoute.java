package com.redhat;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;

/**
 * Created by vgohel
 */
public class AggregateRoute extends RouteBuilder implements InitializingBean, DisposableBean{
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
        Namespaces namespaces=new Namespaces("p","http://redhat.com/")
                .add("Payment.xsd", "http://www.w3.org/2001/XMLSchema");
        from(sourceUri)
                .split().xpath("/p:Payments/p:Payment",namespaces)
                .convertBodyTo(String.class)
                .aggregate(new BodyAppenderAggregator())
                .xpath("p:Payment/p:to",String.class,namespaces)
                .completionTimeout(aggregateTimoutPeriod*1000)
                .log("\n Getting the Aggregated components with these contents, \n ${body} \n which is now being sent to the sinkDirectory, \n" + sinkDirectory)
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
        if (!sourceFile.exists()){
            boolean noDirectory=false;
            if (createDirectories)
                noDirectory=sourceFile.mkdirs();
            if (!noDirectory)
                throw new BeanInitializationException("Given sourceDirectory : " + sourceFile + "does not exists");
        }
        if (!sourceFile.canRead())
            throw new BeanInitializationException("The sourceDirectory : " + sourceFile + "is not readable");
        sourceUri="file:"+sourceDirectory;

        if (sinkDirectory==null||"".equals(sinkDirectory.trim()))
            throw new BeanInitializationException("You must set a value for the sinkDirectory : " + sinkDirectory);
        File sinkSource=new File(sinkDirectory);
        if (!sinkSource.exists()) {
            boolean noSinkDir=false;
            if (createDirectories)
                noSinkDir=sinkSource.mkdirs();
            if (!noSinkDir)
                throw new BeanInitializationException("Given sinkDirectory does not exist : " + sinkSource);
        }
        if (!sinkSource.canWrite())
            throw new BeanInitializationException("Given sinkDirectory : " + sinkSource + "is not writable ..");

        sinkUri="file:"+sinkDirectory;
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors.
     *                   Exceptions will get logged but not rethrown to allow
     *                   other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception {
        log.info("Shutting down now : ...");
    }
}
