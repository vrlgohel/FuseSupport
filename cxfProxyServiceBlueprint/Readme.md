**This example shows the configuration of cxfProxyService example using OSGi Blueprint**

  - In this exmaple we use a bean `TriggerRealService` to publish the real web service. But in the real world, the practical use case of the example would be to invoke the webservice hosted on another remote server.
  - The example uses contract first development approach.

*To build and install the bundle follow these steps,*

* Build the bundle, `mvn clean install`.
* This bundle is tested with JBoss Fuse, but you can also install the same in Apache Karaf and Apache ServiceMix. You need to alter some of the component versions in the pom.xml if using Karaf or ServiceMix.
    `install -s mvn:com.redhat/cxfProxyService/1.0-SNAPSHOT`
* Once deployed, you can check the deployed endpoints,

~~~
JBossFuse:karaf@root> cxf:list-endpoints
Name                      State      Address                                                      BusID
[GreeterPort            ] [Started ] [http://localhost:9080/proxyService/Greeter                ] [com.redhat.cxfProxyService-cxf1555925967]
~~~

* Open a SOAPUI tool and fire issue the soap request.

* The below is the proxy route which invokes the real webservice.

~~~
<camelContext trace="false" xmlns="http://camel.apache.org/schema/blueprint">
        <endpoint id="callRealWebService" uri="http://localhost:9090/Greeter"/>
        <route id="proxyRoute">
            <from uri="cxf:bean:proxyService?dataFormat=MESSAGE"/>
            <convertBodyTo type="java.lang.String"/>
            <log message="Soap Message from Proxy Service received, ${body} \n operationName : ${in.header.SOAPAction}"/>
            <to ref="callRealWebService"/>
        </route>
    </camelContext>
~~~