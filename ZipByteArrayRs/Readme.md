# Body with byte array issue in Camel Route (0076)

Here's how you test the attached route:

 * Build the route using `mvn clean install -Dmaven.test.skip=true`
 * Start JBoss Fuse 6.1.0.
 * Deploy the bundle in Fuse, `JBossFuse:karaf@root> install -s mvn:com.redhat/ZipByteArrayRs/1.0-SNAPSHOT`


You should be able to see this output from the console, 

~~~
12:05:34,322 | INFO  | ExtenderThread-9 | XmlBeanDefinitionReader          | 124 - org.apache.servicemix.bundles.spring-beans - 3.2.8.RELEASE_1 | Loading XML bean definitions from URL [bundle://251.4:0/META-INF/spring/camelContext.xml]
12:05:34,379 | INFO  | ExtenderThread-9 | WaiterApplicationContextExecutor | 145 - org.springframework.osgi.extender - 1.2.1 | No outstanding OSGi service dependencies, completing initialization for OsgiBundleXmlApplicationContext(bundle=com.redhat.ZipByteArrayRs, config=osgibundle:/META-INF/spring/*.xml)
12:05:34,383 | INFO  | xtenderThread-10 | DefaultListableBeanFactory       | 124 - org.apache.servicemix.bundles.spring-beans - 3.2.8.RELEASE_1 | Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@156d612: defining beans [camel-9:beanPostProcessor,camel-9,zipRouter]; root of factory hierarchy
12:05:34,406 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-5) is starting
12:05:34,406 | INFO  | xtenderThread-10 | ManagedManagementStrategy        | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | JMX is enabled
12:05:37,026 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
12:05:37,026 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
12:05:37,027 | INFO  | xtenderThread-10 | ServerImpl                       | 164 - org.apache.cxf.cxf-api - 2.7.0.redhat-610379 | Setting the server's publish address to be /rest
12:05:37,027 | INFO  | xtenderThread-10 | InstrumentationManagerImpl       | 163 - org.apache.cxf.cxf-rt-management - 2.7.0.redhat-610379 | registering MBean org.apache.cxf:bus.id=cxf16174149,type=Bus.Service.Endpoint,service="{http://redhat.com/}ZipExporter",port="ZipExporter",instance.id=9375031: org.apache.cxf.endpoint.ManagedEndpoint@1accc88
12:05:37,028 | INFO  | xtenderThread-10 | InstrumentationManagerImpl       | 163 - org.apache.cxf.cxf-rt-management - 2.7.0.redhat-610379 | registering MBean org.apache.cxf:bus.id=cxf16174149,type=Bus.Service.Endpoint,service="{http://redhat.com/}ZipExporter",port="ZipExporter",instance.id=9375031: javax.management.modelmbean.RequiredModelMBean@1a820a9
12:05:37,044 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Route: route5 started and consuming from: Endpoint[cxfrs:///rest?resourceClasses=com.redhat.ZipExporter]
12:05:37,049 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Total 1 routes, of which 1 is started.
12:05:37,050 | INFO  | xtenderThread-10 | OsgiSpringCamelContext           | 142 - org.apache.camel.camel-core - 2.12.0.redhat-610379 | Apache Camel 2.12.0.redhat-610379 (CamelContext: camel-5) started in 2.643 seconds
12:05:37,052 | INFO  | xtenderThread-10 | OsgiBundleXmlApplicationContext  | 121 - org.apache.servicemix.bundles.spring-context - 3.2.8.RELEASE_1 | Publishing application context as OSGi service with properties {org.springframework.context.service.name=com.redhat.ZipByteArrayRs, Bundle-SymbolicName=com.redhat.ZipByteArrayRs, Bundle-Version=1.0.0.SNAPSHOT}
12:05:37,055 | INFO  | xtenderThread-10 | ContextLoaderListener            | 145 - org.springframework.osgi.extender - 1.2.1 | Application context successfully refreshed (OsgiBundleXmlApplicationContext(bundle=com.redhat.ZipByteArrayRs, config=osgibundle:/META-INF/spring/*.xml))

~~~

- Attached is a SOAP-UI project for testing this, `REST-Project-1-soapui-project.xml`.
- Just import the above xml file in soap-ui and add any pdf as attachement.

- Once the request is sent you can see the .zip created with the attachement on it stored in the `$FUSE_HOME/out/ID-dhcp201-202-englab-pnq-redhat-com-46417-1441173798719-3-1`.


## Here's the code for the camel route, 

~~~
 from("cxfrs:/rest?resourceClasses=com.redhat.ZipExporter")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        String csv=exchange.getIn().getBody(String.class);
                        List<byte[]> files=new ArrayList<byte[]>();
                        files.add(csv.getBytes());
                        byte[] out=zipFiles(files);
                        exchange.getIn().setBody(out,byte[].class);
                    }

                    private byte[] zipFiles(List<byte[]> files) throws IOException {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ZipOutputStream zos = new ZipOutputStream(baos);
                        int i = 0;

                        for (byte[] pdf : files) {
                            ZipEntry entry = new ZipEntry("doc_" + i++ + ".pdf");
                            entry.setSize(pdf.length);
                            log.info("Pdf len: " + pdf.length);
                            zos.putNextEntry(entry);
                            zos.write(pdf);
                            zos.closeEntry();
                        }
                        baos.flush();
                        zos.close();
                        baos.close();
                        return baos.toByteArray();
                    }
                }).log("${body}")
                .to("file:out");
~~~

