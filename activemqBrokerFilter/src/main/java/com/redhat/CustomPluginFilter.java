package com.redhat;

import java.util.List;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

/**
 * @author vgohel
 *
 */
public class CustomPluginFilter implements BrokerPlugin {

	List<String> allowedIpAddresses;

	/**
	 * @return the allowedIpAddresses
	 */
	public List<String> getAllowedIpAddresses() {
		return allowedIpAddresses;
	}

	/**
	 * @param allowedIpAddresses
	 *            the allowedIpAddresses to set
	 */
	public void setAllowedIpAddresses(List<String> allowedIpAddresses) {
		this.allowedIpAddresses = allowedIpAddresses;
	}

	/*
	 * @see
	 * org.apache.activemq.broker.BrokerPlugin#installPlugin(org.apache.activemq
	 * .broker.Broker)
	 */
	public Broker installPlugin(Broker broker) throws Exception {
		return new CustomBrokerFilter(broker, allowedIpAddresses);
	}

}
