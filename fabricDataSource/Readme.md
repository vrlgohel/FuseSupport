# Using JPA-Hibernate with Fuse Fabric

Basic useful feature list:

 * This example shows how can use and deploy JPA persistence bundles with profiles in Fuse Fabric environment.
 * The project, `fabricDataSource` is a multi-module maven project, which consists of 2 OSGI bundles, `fabricDS` is the datasource blueprint bundle and the `fabricDSBundle` is the bundle which consists of actual `PersistenceUnit` defined.




Running the examples:

- Firstly, you will need to create a fabric in JBoss Fuse. The most basic way to start and create a fabric, which starts the `root` container is shown below,

~~~
JBossFuse:karaf@root> fabric:create --clean
~~~

- Then you would need to create `profiles` which are deployment units in Fabric. Either you can create `profiles` manually and assign `features` to it like the below,

~~~
JBossFuse:karaf@root> profile-create fabricDSBundle

JBossFuse:karaf@root> profile-edit --bundles mvn:com.redhat/fabricDSBundle/1.0-SNAPSHOT --features jpa  fabricDSBundle
~~~

---

- Or the other option to create profiles in Fabric is to use the `fabric8-maven-plugin`.


- In the bundles, i have used the `fabric8-maven-plugin` as below,

~~~
              <plugin>
                <groupId>io.fabric8</groupId>
                <artifactId>fabric8-maven-plugin</artifactId>
                <version>1.2.0.Beta4</version>
                <configuration>
                    <features>jpa jpa-hibernate jndi</features>
                    <bundles>mvn:com.redhat/fabricDSTest/1.0-SNAPSHOT</bundles>
                    <bundles>mvn:com.redhat/fabricDSBundle/1.0-SNAPSHOT</bundles>
                    <bundles>wrap:mvn:mysql/mysql-connector-java/5.1.18</bundles>
                    <profile>fabricDSBundle</profile>
                </configuration>
            </plugin>
~~~


- Here, the above plugin indicates that i am using `jpa`,`jpa-hibernate`,`jndi` features and also installing a non-osgi driver `mysql` jar, `wrap:mvn:mysql/mysql-connector-java/5.1.18`.
- I am creating a profile with name as `fabricDSBundle`.

- You also need to make sure you have the following below, so that `Fabric Agent` can upload the profile and bundle repository information in the maven local repository. This is a way that fabric look up dependencies to be satified within the ensemble.

~~~
<servers>
	   <server>
            <id>fabric8.upload.repo</id>
            <username>admin</username>
            <password>admin</password>
           </server>
	</servers>
~~~
- You then need to just execute, `mvn fabric8:deploy`. Make sure that you have the Fabric8 instance running.

- The should provide you with the following below output,

~~~
[INFO] Updating profile: fabricDSBundle with parent profile(s): [karaf] using OSGi resolver
[INFO] About to invoke mbean io.fabric8:type=ProjectDeployer on jolokia URL: http://localhost:8181/jolokia with user: admin
[INFO] Result: DeployResults{profileUrl='null', profileId='fabricDSBundle', versionId='1.0'}
[INFO] No profile configuration file directory /Viral_Data/Projects_To_Deploy/Fuse/Fabric/fabricDataSource/fabricDSBundle/src/main/fabric8 is defined in this project; so not importing any other configuration files into the profile.
[INFO] Performing profile refresh on mbean: io.fabric8:type=Fabric version: 1.0 profile: fabricDSBundle
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] fabricTestBundle ................................... SUCCESS [  8.224 s]
[INFO] fabricDSTest ....................................... SUCCESS [ 31.366 s]
[INFO] fabricDSBundle ..................................... SUCCESS [ 14.637 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 55.043 s
[INFO] Finished at: 2015-02-09T15:56:36+05:30
[INFO] Final Memory: 36M/354M
[INFO] ------------------------------------------------------------------------

~~~

- You can then check the JBoss Fuse console, to see if the profile exists,

~~~
JBossFuse:karaf@root> profile-list |grep fabric
fabric                                   1              karaf, hawtio
fabricDSBundle                           1              karaf, karaf, karaf
feature-fabric-web                       0              karaf
feature-process                          0              fabric


JBossFuse:karaf@root> profile-display fabricDSBundle
Profile id: fabricDSBundle
Version   : 1.0
Attributes:
	parents: karaf karaf karaf
Containers: root

Container settings
----------------------------
Features :
	jndi
	jpa
	jpa-hibernate

Bundles :
	wrap:mvn:mysql/mysql-connector-java/5.1.18
	mvn:com.redhat/fabricDSTest/1.0-SNAPSHOT
	mvn:com.redhat/fabricDSBundle/1.0-SNAPSHOT

Agent Properties :
	  lastRefresh.fabricDSBundle = 1423477596201


Configuration details
----------------------------

Other resources
----------------------------
Resource: modules/requirements.json
Resource: modules/com.redhat/fabricDSTest-requirements.json
Resource: modules/com.redhat/fabricDSBundle-requirements.json
~~~

- Now, to deploy this in the `root` or a child conatiner, you just need to assign the profile to the container,

~~~
JBossFuse:karaf@root> container-add-profile root fabricDSBundle
~~~

- The above command will directly deploy the `mysql datasource` and the `Fabric Deployment Agent` will trigger the activation of bundle.

- You should get the below output in JBoss Fuse or Karaf console,

~~~
2015-02-09 15:58:30,063 | INFO  | agent-2-thread-1 | container                        | l.ManagedPersistenceUnitInfoImpl   50 | 328 - org.apache.aries.jpa.container - 1.0.1.redhat-610379 | The org.osgi.service.jdbc package is unavailable. As a result the Aries JPA container will not offer any DataSourceFactory integration.
2015-02-09 15:58:30,136 | INFO  | agent-2-thread-1 | Version                          | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HCANN000001: Hibernate Commons Annotations {4.0.4.Final}
2015-02-09 15:58:30,140 | INFO  | agent-2-thread-1 | Version                          | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000412: Hibernate Core {4.2.9.Final}
2015-02-09 15:58:30,141 | INFO  | agent-2-thread-1 | Environment                      | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000206: hibernate.properties not found
2015-02-09 15:58:30,142 | INFO  | agent-2-thread-1 | Environment                      | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000021: Bytecode provider name : javassist
2015-02-09 15:58:30,153 | INFO  | agent-2-thread-1 | Ejb3Configuration                | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000204: Processing PersistenceUnitInfo [
	name: CUSTOMER
	...]
2015-02-09 15:58:30,197 | INFO  | agent-2-thread-1 | ConnectionProviderInitiator      | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000130: Instantiating explicit connection provider: org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider
2015-02-09 15:58:30,412 | WARN  | agent-2-thread-1 | JdbcServicesImpl                 | org.jboss.loggi
2015-02-09 15:58:30,427 | INFO  | agent-2-thread-1 | Dialect                          | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000400: Using dialect: org.hibernate.dialect.MySQLDialect
2015-02-09 15:58:30,440 | INFO  | agent-2-thread-1 | LobCreatorBuilder                | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000422: Disabling contextual LOB creation as connection was null
2015-02-09 15:58:30,556 | INFO  | agent-2-thread-1 | TransactionFactoryInitiator      | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000268: Transaction strategy: org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory
2015-02-09 15:58:30,560 | INFO  | agent-2-thread-1 | ASTQueryTranslatorFactory        | org.jboss.logging.JDKLogger        64 | 340 - org.jboss.logging.jboss-logging - 3.1.4.GA | HHH000397: Using ASTQueryTranslatorFactory
2015-02-09 15:58:30,724 | INFO  | agent-2-thread-1 | container                        | er.impl.PersistenceBundleManager  635 | 328 - org.apache.aries.jpa.container - 1.0.1.redhat-610379 | No quiesce support is available, so managed persistence units will not participate in quiesce operations.
2015-02-09 15:58:30,726 | INFO  | agent-2-thread-1 | DeploymentAgent                  | io.fabric8.agent.DeploymentAgent  753 | 60 - io.fabric8.fabric-agent - 1.0.0.redhat-379 | Done.
~~~


*NOTE: You should have the database already up and running for the bundle to deploy properly. I have used mysql database, you can add any other datasource. You need to make sure the Database exits before you setup the connectionUrl settings for the database driver. *




