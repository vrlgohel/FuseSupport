/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.redhat.client;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.hello_world_soap_http.Greeter;
import org.slf4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.File;
import java.net.URL;

public final class Client implements Processor {

    private Greeter greeter;

    private static final QName SERVICE_NAME
        = new QName("http://apache.org/hello_world_soap_http", "SOAPService");

    private static final QName PORT_NAME =
        new QName("http://apache.org/hello_world_soap_http", "SoapPort");


    public Client() {
    }

    public static void main(String args[], Logger log) throws Exception {

        if (args.length < 2) {
            log.info("please specify wsdl and configuration file");
            System.exit(1);
        }
        
        URL wsdlURL;
        File wsdlFile = new File(args[0]);
        if (wsdlFile.exists()) {
            wsdlURL = wsdlFile.toURI().toURL();
        } else {
            wsdlURL = new URL(args[0]);
        }
        
        SpringBusFactory bf = new SpringBusFactory();
        URL busURL;
        File busFile = new File(args[1]);
        if (busFile.exists()) {
            busURL = busFile.toURI().toURL();
        } else {
            busURL = new URL(args[1]);
        }

//        Bus bus = bf.createBus(busFile.toString());
        Bus bus = bf.createBus(busURL);
        SpringBusFactory.setDefaultBus(bus);
        SpringBusFactory.setThreadDefaultBus(bus);

        Service service = Service.create(wsdlURL, SERVICE_NAME);
        Greeter port = service.getPort(PORT_NAME, Greeter.class);

        log.info("Invoking greetMe...");
        try {
            String username = System.getProperty("user.name");
            String resp = port.greetMe(username);
            log.info("Server responded with: " + resp);
            if (!("Hello " + username).equals(resp)) {
                throw new Exception("Wrong response: " + resp);
            }
        } catch (Exception e) {
            log.error("Invocation failed with the following: " + e.getCause());
            log.error(e.getMessage(), e);
        }
    }

    public void setGreeter(Greeter greeter) {
        this.greeter = greeter;
    }

    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        greeter.greetMe(in.getBody(String.class));
    }
}
