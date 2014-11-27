# An example to show how you can override the default ActiveMQ interceptor and Filters to provide a custom implementation for Security, Logging and notify the users when the connection fails.

- This example makes use of the `BrokerFilter` class which is implemented to filter out the `IPAddresses` coming through the broker.
- The `BrokerFilter` class provides the ability to intercept many of the available broker-level operations. Broker operations include such items as adding consumers and producers to the broker, committing transactions in the broker, and adding and removing connections to the broker, to name a few.
- Custom functionality can be added by extending the BrokerFilter class and overriding a method for a given operation.


**Implementing the Custom Broker Plugin**

- To filter IP addresses, a class named `CustomBrokerFilter` will be created to override the `BrokerFilter's addConnection()` method.
- The implementation of this method will perform a simple check of the IP address using a regular expression to determine the ability to connect.

~~~
@Override
	public void addConnection(ConnectionContext context, ConnectionInfo info)
			throws Exception {
		String address = context.getConnection().getRemoteAddress();
		Matcher matcher = pattern.matcher(address);
		if (matcher.matches()) {
			String ipAddress = matcher.group(1);
			if (!allowedIpAddresses.contains(ipAddress)) {
				throw new SecurityException("Conecting to this IP : "
						+ ipAddress + "is not allowed");
			} else {
				throw new SecurityException("Invalid IP address ...." + address);
			}
		}
		super.addConnection(context, info);
	}
~~~

- The main test here is to verify whether, `BrokerFilter's removeConnection()` method returns null when a client disconnects or does it always throw exception of what actually happened.
    - The JavaDoc for this, says that 'error' returned should be `null` in case of client disconnect.
    
    
- After the actual plugin logic has been implemented, the plugin must be configured and installed. For this purpose, an implementation of the BrokerPlugin will be created as below, 

~~~
/**
 * @author vgohel
 *
 */
public class CustomPluginFilter implements BrokerPlugin {

	List<String> allowedIpAddresses;

	/**
	 * @return the allowedIpAddresses
	 */
	public List<String> getAllowedIpAddresses() {
		return allowedIpAddresses;
	}

	/**
	 * @param allowedIpAddresses
	 *            the allowedIpAddresses to set
	 */
	public void setAllowedIpAddresses(List<String> allowedIpAddresses) {
		this.allowedIpAddresses = allowedIpAddresses;
	}

	/*
	 * @see
	 * org.apache.activemq.broker.BrokerPlugin#installPlugin(org.apache.activemq
	 * .broker.Broker)
	 */
	public Broker installPlugin(Broker broker) throws Exception {
		return new CustomBrokerFilter(broker, allowedIpAddresses);
	}

}
~~~

- The BrokerPlugin is used to expose the configuration of a plugin and also installs the plugin into the ActiveMQ broker.

- The `installPlugin()` method is used to instantiate the plugin and return a new intercepted broker for the next plugin in the chain.

For more details, refer to this link, http://activemq.apache.org/interceptors.html


**Configuring the custom Plugin in activemq.xml**

- You can then configure the custom plugin in `activemq.xml` as below, 

~~~
<broker xmlns="http://activemq.apache.org/schema/core" useJmx="true">

		<persistenceAdapter>
			<kahaDB directory="./target/activemq-data" />
		</persistenceAdapter>

		<transportConnectors>
			<transportConnector uri="tcp://localhost:61616" />
		</transportConnectors>

		<plugins>
			<bean id="customBrokerFilter" class="com.redhat.CustomPluginFilter"
				xmlns="http://www.springframework.org/schema/beans">
				<property name="allowedIpAddresses">
					<list>
						<value>127.0.0.1</value>
					</list>
				</property>
			</bean>
		</plugins>
	</broker>

~~~


** Testing the Plugin **

- Build the maven project using, `mvn clean install`
- This project makes use of an embedded broker with an `activemq.xml`  file located under, `src/main/resources/conf`.
- Open up a terminal and issue, 'mvn activemq:run'.
- In another terminal, execute, 'mvn exec:java'.
- The client is a standalone dummy client, which probably takes time and does vitually nothing.


- To check if the 'Broker's removeConnection()' method really returns the `Throwable` object as `null`, press `Crtl-c` to terminate the java program.
- On doing this, you should be able to see this logging, which means that `error` reference of `Throwable` returns `null`.

~~~
[vgohel@localhost activemqBrokerFilter]$ mvn activemq:run
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building activemqBrokerFilter 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- activemq-maven-plugin:5.9.0:run (default-cli) @ activemqBrokerFilter ---
[INFO] Loading broker configUri: xbean:file:src/main/resources/conf/activemq.xml
log4j:WARN No appenders could be found for logger (org.apache.activemq.xbean.XBeanBrokerFactory$1).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
[INFO] PListStore:[/Viral_Data/DevWorkspace/activemqBrokerFilter/./targetactivemq-data/localhost/tmp_storage] started
[INFO] Using Persistence Adapter: KahaDBPersistenceAdapter[/Viral_Data/DevWorkspace/activemqBrokerFilter/./target/activemq-data]
[INFO] JMX consoles can connect to service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi
[INFO] KahaDB is version 5
[INFO] Recovering from the journal ...
[INFO] Recovery replayed 1 operations from the journal in 0.037 seconds.
[INFO] Apache ActiveMQ 5.9.0 (localhost, ID:localhost-57534-1417076377850-0:1) is starting
[INFO] Listening for connections at: tcp://localhost:61616
[INFO] Connector tcp://localhost:61616 started
[INFO] Apache ActiveMQ 5.9.0 (localhost, ID:localhost-57534-1417076377850-0:1) started
[INFO] For help or more information please see: http://activemq.apache.org
[WARNING] Store limit is 102400 mb, whilst the data directory: /Viral_Data/DevWorkspace/activemqBrokerFilter/./target/activemq-data only has 86662 mb of usable space - resetting to maximum available disk space: 86662 mb
[WARNING] Transport Connection to: tcp://127.0.0.1:57992 failed: java.io.EOFException
[INFO] Something Weird happened at the client end : null

~~~



