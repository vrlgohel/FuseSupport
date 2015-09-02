package com.redhat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Created by vgohel on 9/2/15.
 */
public class ZipRouter extends RouteBuilder {
    private Logger logger= LoggerFactory.getLogger(this.getClass().getName());


    public void configure() throws Exception {
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
    }
}
