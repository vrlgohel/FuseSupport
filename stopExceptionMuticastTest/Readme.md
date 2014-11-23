**An example test case showing how the stopOnException() camel processor behaves with the multicast() processor alongwith the parallelProcessing()**

- We normally make use of the `stopOnException()` with the `multicast()` processor for the reasons that if any of the endpoint recepients fail, the route flow should be rolled back and the subsequent processors stop executing.



- The `Muticast` is there for sending `Exchange` to multiple recipients and it sends the exchange to a number of recipients sequentially. This is therefore a drawback of using `multicast` because it can easily become a bottleneck if there is a high peak load situation and very limited number of system resources available.

- For these reasons we normally have the good practice to use `parallelProcessing()` processor with `multicast()` with `n` number of recipients.

- If an exception occurs in `any one of the recipients`, the default behaviour of the `multicast()` is still to continue to send `Exchanges` to the remaining endpoints/recipients setup and not to stop them processing.
    - For the above behavior to change, you will need to include the `stopOnException()` processor.

- This exanple test case, shows the behavior with `parallelProcessing()` processor.


--------------------

- To execute the test case, you need to build it first.
    - Execute,  `mvn clean install`, this will in turn execute the camel unit test.
    - It has 2 test scenarios, which explicitly fail when an exception occurs,
    
    ~~~
    @Test
	public void endpoint1FailureTest() throws Exception {
		endpoint1.whenAnyExchangeReceived(new Processor() {

			public void process(Exchange exchange) throws Exception {
				throw new RuntimeException(
						"We got a Runtime Exception in endpoint1 failure....");
			}
		});
		endpoint2.expectedMinimumMessageCount(0);
		endpoint3.expectedMessageCount(0);
		endpoint4.expectedMessageCount(1);

		String result = producerTemplate.requestBody("direct:start",
				"Viral's Test for endpoint1 failure", String.class);
		assertEquals("Stop", result);
		assertMockEndpointsSatisfied();
	}
    ~~~
    
    ~~~
    @Test
	public void endpoint2FailureTest() throws Exception {
		endpoint2.whenAnyExchangeReceived(new Processor() {

			public void process(Exchange exchange) throws Exception {
				throw new RuntimeException(
						"We got a Runtime Exception in endpoint2 failure ....");
			}
		});
		endpoint1.expectedMinimumMessageCount(0);
		endpoint3.expectedMessageCount(0);
		endpoint4.expectedMessageCount(1);

		String result = producerTemplate.requestBody("direct:start",
				"Viral's Test for endpoint2 failure", String.class);
		assertEquals("Stop", result);
		assertMockEndpointsSatisfied();
	}
    ~~~
    

- The route is extremely simple, which consists of a `multicast()` using `stopOnException()` and `parallelProcessing()`,

~~~
@Override
	public void configure() throws Exception {

		/*
		 * You can optionally set the exception thrown by the route to handle it
		 * in a custom manner.
		 * 
		 * onException(Exception.class).handled(true).log("log:onException")
		 * .to("mock:endpoint4").transform(constant("Stopped !!!"));
		 */
		from("direct:start")
				.multicast()
				.log("Starting Executing the endpoints ......")
				.stopOnException()
				.parallelProcessing()
				.to("mock:endpoint1", "mock:endpoint2", "mock:endpoint4",
						"mock:endpoint3")
				.transform(constant("Hello to you now !!!!"));
	}

~~~

- You will get the test failures and the route is never processed ahead, if one of the endpoint/recepients fails,

~~~
Caused by: org.apache.camel.CamelExchangeException: Parallel processing failed for number 0. Exchange[Message: Viral's Test for endpoint1 failure]. Caused by: [java.lang.RuntimeException - We got a Runtime Exception in endpoint1 failure....]
	at org.apache.camel.processor.MulticastProcessor$1.call(MulticastProcessor.java:307)
	at org.apache.camel.processor.MulticastProcessor$1.call(MulticastProcessor.java:278)
	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
~~~


~~~
Caused by: org.apache.camel.CamelExchangeException: Parallel processing failed for number 1. Exchange[Message: Viral's Test for endpoint2 failure]. Caused by: [java.lang.RuntimeException - We got a Runtime Exception in endpoint2 failure ....]
	at org.apache.camel.processor.MulticastProcessor$1.call(MulticastProcessor.java:307)
	at org.apache.camel.processor.MulticastProcessor$1.call(MulticastProcessor.java:278)
	at java.util.concurrent.FutureTask.run(FutureTask.java:262)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
~~~

