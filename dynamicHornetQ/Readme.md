# Dynamic Destination example with Camel-HornetQ in JBoss EAP



This example show how you can create Dynamic Destinations connecting Camel as a producer on a HornetQ broker with JBoss EAP.
 

For running the example, 

- Start JBoss EAP, on the `standalone-full.xml` profile and create a user,

~~~
[vgohel@localhost bin]$ ./add-user.sh
Username : Viral
Password:
~~~

- Execute `mvn camel-run`.
