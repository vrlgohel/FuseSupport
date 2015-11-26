# An example showing how messages are processed in camel-file2 component using the 'maxMessagesPerPoll' file consumer 

- This file consumer attribute polls for the files specified by the attributes values. Eg. if `maxMessagesPerPoll=2`, then on each poll, 2 files will be processed in parallel. 
- So, the files will be processed in batches of 2. 

- Here's the route definition, 

~~~
<camelContext trace="false" xmlns="http://camel.apache.org/schema/spring">
  <route shutdownRoute="Default" shutdownRunningTask="CompleteAllTasks" id="maxFilePolls">
    <from uri="file:input?maxMessagesPerPoll=2&amp;delay=4000&amp;noop=true&amp;scheduler=spring"/>
    <process ref="myProcessor"/>
    <to uri="file:output"/>
  </route>
</camelContext>
~~~

- To execute the example, just hit, `mvn exec:java`.
- You will see that the files are processed in batches of 2 files, 

~~~

[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : e.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : a.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : d.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : g.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : b.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : f.txt
[Camel (camel-1) thread #0 - file://input] INFO com.redhat.FileReaderProcessor - Files processed : c.txt

~~~

