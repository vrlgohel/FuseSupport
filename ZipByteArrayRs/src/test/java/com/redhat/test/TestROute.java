package com.redhat.test;

import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.cxf.test.AbstractCXFTest;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import org.apache.camel.component.cxf.*;

/**
 * Created by vgohel on 9/2/15.
 */
public class TestROute extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camelContext.xml");
    }

    @Test
    public void testRoute() throws Exception{
        String cxfRs="cxfrs:http://localhost/" + getPort() + "/" + "cxf/rest/zip/docs";
        Path path1 = Paths.get("zipFiles/spring_tutorial.pdf");
        byte[] data1 = Files.readAllBytes(path1);
        List<byte[]> files=new ArrayList<byte[]>();
        files.add(data1);
        template.sendBody(cxfRs,files);
        assertMockEndpointsSatisfied();
    }

    public int getPort(){
        int port= AvailablePortFinder.getNextAvailable();
        System.setProperty("port",Integer.toString(port));
        return port;
    }
}
