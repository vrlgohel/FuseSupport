
package com.redhat.samples.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.redhat.samples.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Goodbye_QNAME = new QName("http://ws.samples.redhat.com/", "goodbye");
    private final static QName _HelloResponse_QNAME = new QName("http://ws.samples.redhat.com/", "helloResponse");
    private final static QName _GoodbyeResponse_QNAME = new QName("http://ws.samples.redhat.com/", "goodbyeResponse");
    private final static QName _Hello_QNAME = new QName("http://ws.samples.redhat.com/", "hello");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.redhat.samples.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Hello }
     * 
     */
    public Hello createHello() {
        return new Hello();
    }

    /**
     * Create an instance of {@link GoodbyeResponse }
     * 
     */
    public GoodbyeResponse createGoodbyeResponse() {
        return new GoodbyeResponse();
    }

    /**
     * Create an instance of {@link HelloResponse }
     * 
     */
    public HelloResponse createHelloResponse() {
        return new HelloResponse();
    }

    /**
     * Create an instance of {@link Goodbye }
     * 
     */
    public Goodbye createGoodbye() {
        return new Goodbye();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Goodbye }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.samples.redhat.com/", name = "goodbye")
    public JAXBElement<Goodbye> createGoodbye(Goodbye value) {
        return new JAXBElement<Goodbye>(_Goodbye_QNAME, Goodbye.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.samples.redhat.com/", name = "helloResponse")
    public JAXBElement<HelloResponse> createHelloResponse(HelloResponse value) {
        return new JAXBElement<HelloResponse>(_HelloResponse_QNAME, HelloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GoodbyeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.samples.redhat.com/", name = "goodbyeResponse")
    public JAXBElement<GoodbyeResponse> createGoodbyeResponse(GoodbyeResponse value) {
        return new JAXBElement<GoodbyeResponse>(_GoodbyeResponse_QNAME, GoodbyeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Hello }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.samples.redhat.com/", name = "hello")
    public JAXBElement<Hello> createHello(Hello value) {
        return new JAXBElement<Hello>(_Hello_QNAME, Hello.class, null, value);
    }

}
