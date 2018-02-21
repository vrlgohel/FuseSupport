package com.redhat;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProcessor implements Processor {
  private Logger _log = LoggerFactory.getLogger(this.getClass());
  Properties properties = new Properties();

  public MessageProcessor(String mapping) throws Exception{
    setMapping(mapping);
  }


  public void setMapping(String mapping) throws Exception {
    URL url = new URL(mapping);
    final InputStream is = url.openStream();
    properties.load(is);
  }


  @Override
  public void process(Exchange exchange) throws Exception {
    _log.info("key1 from profile proeperties file "+ properties.getProperty("key1"));
    _log.info("key2 from profile proeperties file "+ properties.getProperty("key2"));
  }

}
