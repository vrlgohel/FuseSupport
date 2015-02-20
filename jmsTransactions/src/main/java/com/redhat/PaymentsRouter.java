package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import com.fusesource.training.payment.Payments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Viral Gohel
 */
public class PaymentsRouter extends SpringRouteBuilder implements InitializingBean{
    private Logger logger= LoggerFactory.getLogger(PaymentsRouter.class);

    private String incomingQueue;
    private String outgoingQueue;

    String sourceUri;
    String targetUri;

    public void setIncomingQueue(String incomingQueue) {
        this.incomingQueue = incomingQueue;
    }

    public void setOutgoingQueue(String outgoingQueue) {
        this.outgoingQueue = outgoingQueue;
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
        if (incomingQueue==null||incomingQueue.equals(""))
            throw new BeanInitializationException("The incomingQueue is not set : " + incomingQueue);
        if (outgoingQueue==null||outgoingQueue.equals(""))
            throw new BeanInitializationException("The outgoingQueue is not set : " + outgoingQueue);

        sourceUri="activemq:queue:"+incomingQueue;
        targetUri="activemq:queue:"+outgoingQueue;
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
        JaxbDataFormat jaxbDataFormat=new JaxbDataFormat();
        jaxbDataFormat.setPrettyPrint(true);

        errorHandler(noErrorHandler());

        from(sourceUri)
                .convertBodyTo(String.class)
                .unmarshal(jaxbDataFormat)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        logger.info("Attempting to process an incoming message ....");
                        Payments payments=exchange.getIn().getBody(Payments.class);
                        if (payments.getCurrency().equals("???")){
                            logger.info("Rejecting payments with currency : " + payments.getCurrency());
                            throw new Exception("Rejected payments with currency as : " + payments.getCurrency());
                        }else
                            logger.info("Message looks good !!" + "Can be processed ahead .....");
                    }
                })
                .marshal(jaxbDataFormat)
                .convertBodyTo(String.class)
                .to(targetUri);
    }
}
