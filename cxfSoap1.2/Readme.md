# An example CXF bundle which shows how you can set the soap binding to 1.2 

- This is a very simple bundle which show how you can configure soap binding 1.2 in the camel-cxf.xsd elements you are using to expose the published endpoints in camel routes.
    - This example uses OSGI Blueprint file as below to configure the soap binding,
    
    ~~~
    <camel-cxf:cxfEndpoint id="serviceEndpoint"
		serviceClass="com.redhat.RouteMetrics" address="/RouteMetrics"
		loggingFeatureEnabled="true">
		<camel-cxf:properties>
			<entry key="dataFormat" value="PAYLOAD"></entry>
		</camel-cxf:properties>
		<camel-cxf:binding>
			<soap:soapBinding version="1.2" />
		</camel-cxf:binding>
	</camel-cxf:cxfEndpoint>
    ~~~


- It has a simple processor thatt logs the incoming soap message to the console:

~~~
<camelContext trace="true"
		xmlns="http://camel.apache.org/schema/blueprint">
		<route streamCache="true">
			<from uri="cxf:bean:serviceEndpoint?synchronous=true" />
			<to uri="bean:messageDump" />
		</route>
	</camelContext>
~~~

~~~
/**
 * @author vgohel
 *
 */
public class MessageDump {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public SOAPMessage processMessage(Exchange exchange) {
		SOAPMessage soapMessage = (SOAPMessage) exchange.getIn().getHeader(
				CxfConstants.CAMEL_CXF_MESSAGE, SOAPMessage.class);
		logger.info("Got the soap message ......");
		return soapMessage;
	}
}
~~~


- To build the project, just execute, `mvn clean install`

------

- In JBoss Fuse, make sure that `cxf-bindings-soap` bundle is installed.

~~~
JBossFuse:karaf@root> features:list|grep soap
[installed  ] [2.7.0.redhat-610379    ] cxf-bindings-soap                       cxf-2.7.0.redhat-610379
~~~

- Install the bundle using,

~~~
JBossFuse:karaf@root> install -s mvn:com.redhat/cxfSoap1.2/0.0.1-SNAPSHOT
~~~

- Fire a soap request with the `/request.sh` script, which will pass the below soap message,

~~~
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:red="http://redhat.com/">
   <soap:Header/>
   <soap:Body>
      <red:sayHi>
         <!--Optional:-->
         <arg0>Viral</arg0>
      </red:sayHi>
   </soap:Body>
</soap:Envelope>
~~~

- You can observe on the console or log files, the below soap response,

~~~
ID: 3
Response-Code: 200
Encoding: UTF-8
Content-Type: application/soap+xml
Headers: {accept-encoding=[gzip,deflate], breadcrumbId=[ID-localhost-49229-1416984040193-2-1], Host=[localhost:8181], User-Agent=[Jakarta Commons-HttpClient/3.1]}
Payload: <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"><soap:Body/></soap:Envelope>
~~~


- As you can see the `Content-Type` is set to `application/soap+xml` which means that the soap binding is set to 1.2 version

- There are also other ways you can set the soap bindings,

    - Add a soap12 namespace declaration to the top-level wsdl:definitions element.
    `xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/`
    - In the wsdl:binding section, change the namespace prefix of the soap:binding to soap12:binding. The rest of the declaration can remain as-is.
    `<soap12:binding transport="http://schemas.xmlsoap.org/soap/http" style="document" />`
    Finally, in the `wsdl:service/wsdl:port` section, update the namespace prefix of the soap:address element: `<soap12:address location="http://localhost:8080..."/>`
    - If you're using CXF, no change to the build process is necessary as the CXF WSDL2Java Maven plugin will detect the new SOAP bindings in the WSDL and generate a SOAP 1.2-compliant web service provider and client accordingly.
    
- Converting a Java First Web Service to soap binding 1.2,
    ~~~
    import javax.xml.ws.BindingType
    
    @BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
    ~~~
