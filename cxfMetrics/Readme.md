
**This example is a Basic CXF OSGI Bundle which shows how you can perform monitering and perfomance related tasks using the JMX Navigator.**

- Build the OSGI Bundle, 
    - `mvn clean install`

- Deploy the OSGI Bundle 
    - `install -s mvn:com.redhat/cxfMetrics/0.0.1-SNAPSHOT`

- Fire a soap request as below using soapui or a regular JAX-WS client or a Java EE client,

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:red="http://redhat.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <red:sayHello>
         <!--Optional:-->
         <arg0>Viral</arg0>
      </red:sayHello>
   </soapenv:Body>
</soapenv:Envelope>
```

- You then need to refresh the JMX Connection from the Hawito console or the JMX Navigator if you are using JBoss Developer Studio (JBDS) or eclipse.

- You can see all the performance statistics as explained in the .png image file with the project. 

- The blueprint.xml file has the following content through which you can monitor performance statistics, 

```
<cxf:bus id="MyBus" name="MyBus" bus="MyBus" >
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
<jaxws:endpoint implementor="com.redhat.MetricServiceImpl"
		id="serviceEndpoint" address="/MetricService" bus="MyBus">
		<jaxws:features>
			<bean class="org.apache.cxf.management.interceptor.ResponseTimeFeature"/>
		</jaxws:features>
	</jaxws:endpoint>

```