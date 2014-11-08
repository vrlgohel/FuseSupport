/**
 * 
 */
package com.redhat;

import java.io.File;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author vgohel
 * 
 */
public class CustomRouter extends RouteBuilder implements InitializingBean,
		DisposableBean {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private String sourceDirectory;
	private String sinkDirectory;
	private boolean createDirectories;

	/**
	 * @param createDirectories the createDirectories to set
	 */
	public void setCreateDirectories(boolean createDirectories) {
		this.createDirectories = createDirectories;
	}

	public String sourceUri;
	public String euroUri;
	public String usdUri;
	public String otherUri;

	/**
	 * @param sourceUri
	 *            the sourceUri to set
	 */
	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}

	/**
	 * @param euroUri
	 *            the euroUri to set
	 */
	public void setEuroUri(String euroUri) {
		this.euroUri = euroUri;
	}

	/**
	 * @param usdUri
	 *            the usdUri to set
	 */
	public void setUsdUri(String usdUri) {
		this.usdUri = usdUri;
	}

	/**
	 * @param otherUri
	 *            the otherUri to set
	 */
	public void setOtherUri(String otherUri) {
		this.otherUri = otherUri;
	}

	/**
	 * @param sourceDirectory
	 *            the sourceDirectory to set
	 */
	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	/**
	 * @param sinkDirectory
	 *            the sinkDirectory to set
	 */
	public void setSinkDirectory(String sinkDirectory) {
		this.sinkDirectory = sinkDirectory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 */
	@Override
	public void configure() throws Exception {
		errorHandler(noErrorHandler());
		from(sourceUri)
				.convertBodyTo(String.class)
				.log(LoggingLevel.TRACE,
						"Message to be handled : ${file:onlyname}, body : ${body}")
				.choice()
				.when(xpath("/pay:Payments/pay:Currency='EUR'").namespace(
						"pay", "http://www.fusesource.com/training/payment"))
				.log("This is a EURO Xml Payment : ${file:onlyname} - Body: ${body}")
				.to(euroUri)
				.when(xpath("/pay:Payments/pay:Currency='USD'").namespace(
						"pay", "http://www.fusesource.com/training/payment"))
				.log("This is a USD XMl payment : ${file:onlyname} - Body : ${body}")
				.to(usdUri)
				.otherwise()
				.log("This is the Other Xml Payment : ${file:onlyname} - Body : ${body}")
				.to(otherUri);
		log.debug("CustomRouter configured, Listening on Directory "
				+ sourceDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (sourceDirectory == null || "".equals(sourceDirectory.trim())) {
			throw new BeanInitializationException(
					"You must specify the path for the sourceDirectory");
		}
		File newSourceDir = new File(sourceDirectory);
		if (!newSourceDir.exists()) {
			boolean noDirectory = false;
			if (createDirectories) {
				noDirectory = newSourceDir.mkdirs();
			}
			if (!noDirectory) {
				throw new BeanInitializationException(
						"Given source directory : " + sourceDirectory
								+ "is not a directory");
			}
		}
		if (!newSourceDir.canRead()) {
			throw new BeanInitializationException("Given source directory : "
					+ sourceDirectory + "is not readable");
		}
		sourceUri = "file:" + sourceDirectory;

		if (sinkDirectory == null || "".equals(sinkDirectory.trim())) {
			throw new BeanInitializationException(
					"You must specify a path for the sinkDirectory");
		}
		File newSinkDir = new File(sinkDirectory);
		if (!newSinkDir.exists()) {
			boolean noSinkDirectory = false;
			if (createDirectories) {
				noSinkDirectory = newSinkDir.mkdirs();
			}
			if (!noSinkDirectory) {
				throw new BeanInitializationException("Given sink directory"
						+ sinkDirectory + "is not a directory");
			}
		}
		if (!newSinkDir.canWrite()) {
			throw new BeanInitializationException("Given sink directory "
					+ sinkDirectory + "is not able to write");
		}
		euroUri = "file:"
				+ sinkDirectory
				+ "/eur/?fileExist=Append&fileName=euro-${date:now:yyyyMMdd}.xml";
		usdUri = "file:"
				+ sinkDirectory
				+ "/usd/?fileExist=Append&fileName=usd-${date:now:yyyyMMdd}.xml";
		otherUri = "file:"
				+ sinkDirectory
				+ "/other/?fileExist=Append&fileName=other-${date:now:yyyyMMdd}.xml";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		log.debug("CustomRouter shutting down");
	}
}
