
**This example show how to monitor all performance based stats using the camel-cxf element configured.**

- Build the OSGI Bundle, 
    - `mvn clean install`

- Deploy the OSGI Bundle 
    - `install -s mvn:com.redhat/cxfRouteMetrics/0.0.1-SNAPSHOT`
    
- Fire a soap request as below using soapui or a regular JAX-WS client or a Java EE client,

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:red="http://redhat.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <red:sayHi>
         <!--Optional:-->
         <arg0>Viral</arg0>
      </red:sayHi>
   </soapenv:Body>
</soapenv:Envelope>
```

- You then need to refresh the JMX Connection from the Hawito console or the JMX Navigator if you are using JBoss Developer Studio (JBDS) or eclipse.

- You can see all the performance statistics as explained in the .png image file with the project. 

- Here, the blueprint bundle uses, camel-cxf elements used to populate the CXF endpoint in Karaf container.

```
<cxf:bus bus="MyBus" id="MyBus" name="MyBus">
		<cxf:features>
			<cxf:logging />
		</cxf:features>
		<cxf:properties>
			<entry key="bus.jmx.enabled" value="true" />
			<entry key="bus.jmx.persistentBusId" value="MyBus" />
			<entry key="bus.jmx.usePlatformMBeanServer" value="true" />
			<entry key="bus.jmx.createMBServerConnectorFactory" value="false" />
		</cxf:properties>
	</cxf:bus>
```

```
<camel-cxf:cxfEndpoint id="serviceEndpoint"
		serviceClass="com.redhat.RouteMetrics" address="/RouteMetrics"
		bus="MyBus" loggingFeatureEnabled="true">
		<camel-cxf:properties>
			<entry key="dataFormat" value="PAYLOAD"></entry>
		</camel-cxf:properties>
	</camel-cxf:cxfEndpoint>
```