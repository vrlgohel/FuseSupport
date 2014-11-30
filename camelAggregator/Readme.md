# An Example that demonstrates how you can implement the camel Aggregation Strategy with the Camel Splitter EIP

- The Aggregate class in this example, `com.redhat.AggregateRoute` defines a route that takes all files from a given directory, reads out their XML Payments and splits them into single payments.
- Then it aggregates the individual payments into a new file, ordered by the receiver of the payment within the given timeout.

~~~
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
~~~

- This example is a self-contained and can also be deployed in JBoss Fuse, Apache Karaf or Apache ServiceMix.
  
**To deploy this as a self-contained OSGI Bundle.**

   - You will first need to build the bundle, using `mvn clean install`.
    - Then, run `mvn camel:run`.
    - The camel route will create a directory, `./aggregate/src/main/in` in the root of the project, if it does not exit already.
    - Inside, this directory, just copy and paste the files, `EUPayments.xml` and `USPayments.xml`.
    - The files are routed based on the aggregate condition in the `./aggregate/src/main/out` directory and the directory is created if it does not exits already.
    You can see the routed messages in this directory.
    
**To deploy this in JBoss Fuse**

- Install the bundle, `JBossFuse:karaf@root> install -s mvn:com.redhat/camelAggregator/1.0-SNAPSHOT`
    - Then, you need to copy the files, `EUPayments.xml` and `USPayments.xml` into `/aggregate/src/main/in`.
    
    


