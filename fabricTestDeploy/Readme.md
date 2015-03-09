# An example that demontrates basic deployment of OSGI bundle in Fabric8

- The example consists of a very basic camel route using blueprint which just prints messages at a regular interval of every 5 seconds.

~~~
<camelContext id="log-example-context" xmlns="http://camel.apache.org/schema/blueprint" >
        <route id="log-route">
            <from uri="timer:foo?period=5s"/>
            <setBody>
                <simple>Hello from Fabric based Camel route!</simple>
            </setBody>
            <log message=">>> ${body} : ${sys.runtime.id}"/>
            <log message="Testing the changes now !!!"/>
        </route>
    </camelContext>
~~~

### Deploying the bundle in Fuse Fabric:

- First you need to make sure you have a fabric instance already created. This means you have the `root` instance up and running.
- Execute, `mvn clean install`. This will publish the bundle in the local maven repository.
- Then, create a `features.xml` file for easier deployment of the bundle in Fabric.

~~~
<?xml version="1.0" encoding="UTF-8"?>
<features name="my-features">
    <feature name="TestDeploy">
        <bundle>mvn:com.redhat/fabricTestDeploy/1.0-SNAPSHOT</bundle>
    </feature>
</features>
~~~

- Create a child container in Fabric:

~~~
JBossFuse:karaf@root> container-create-child root test1
The following containers have been created successfully:
	Container: test1.
~~~

- Now, create the `profile` and assign the required `features` to the profile:

~~~
JBossFuse:karaf@root> profile-create --parents default Test
JBossFuse:karaf@root> profile-edit --repositories file:///Viral_Data/Projects_To_Deploy/Fuse/Fabric/fabricTestDeploy/features.xml Test
Adding feature repository:file:///Viral_Data/Projects_To_Deploy/Fuse/Fabric/fabricTestDeploy/features.xml to profile:Test version:1.0
JBossFuse:karaf@root> profile-edit --features TestDeploy Test
~~~
*Here, features.xml can be exits in any file location within the local file system*.

~~~
JBossFuse:karaf@root> profile-display Test
Profile id: Test
Version   : 1.0
Attributes:
	parents: default
Containers: test1

Container settings
----------------------------
Repositories :
	file:///Viral_Data/Projects_To_Deploy/Fuse/Fabric/fabricTestDeploy/features.xml

Features :
	TestDeploy
	camel-blueprint

Agent Properties :
	  lastRefresh.Test = 1425899529402


Configuration details
----------------------------

Other resources
----------------------------
~~~

- Assign the `profile` to the child container or `root` conatiner:

~~~
JBossFuse:karaf@root>container-add-profile test1 Test
~~~

- On deployment, you should see messages as below,

~~~
2015-03-09 16:52:38,271 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | >>> Hello from Fabric based Camel route! :
2015-03-09 16:52:38,271 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Testing the changes now !!!
2015-03-09 16:52:43,271 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | >>> Hello from Fabric based Camel route! :
2015-03-09 16:52:43,272 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Testing the changes now !!!
2015-03-09 16:52:48,271 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | >>> Hello from Fabric based Camel route! :
2015-03-09 16:52:48,272 | INFO  | #3 - timer://foo | log-route                        | rg.apache.camel.util.CamelLogger  176 | 105 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Testing the changes now !!!
~~~



*If you then make any changes to the bundle or in the blueprint.xml in the above example, you must again run the, mvn clean install command to make sure changes are reflected in the local maven repository*.

- If you change something in the bundle, for example adding the line, ` <log message="Testing the changes now !!!"/>` in the blueprint.xml file, the changes for this should be reflected in the Fabric environment, just by using the command, `profile-refresh`.

~~~
JBossFuse:karaf@root> profile-refresh Test
~~~

