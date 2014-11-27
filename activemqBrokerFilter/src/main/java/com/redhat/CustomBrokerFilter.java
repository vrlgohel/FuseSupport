package com.redhat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vgohel
 *
 */
public class CustomBrokerFilter extends BrokerFilter {

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	List<String> allowedIpAddresses;
	Pattern pattern = Pattern.compile("^/([0-9\\.]*):(.*)");

	/**
	 * @param next
	 * @param allowedIpAddresses
	 */
	public CustomBrokerFilter(Broker next, List<String> allowedIpAddresses) {
		super(next);
		this.allowedIpAddresses = allowedIpAddresses;
	}

	/*
	 * @see
	 * org.apache.activemq.broker.BrokerFilter#addConnection(org.apache.activemq
	 * .broker.ConnectionContext, org.apache.activemq.command.ConnectionInfo)
	 */
	@Override
	public void addConnection(ConnectionContext context, ConnectionInfo info)
			throws Exception {
		String address = context.getConnection().getRemoteAddress();
		Matcher matcher = pattern.matcher(address);
		if (matcher.matches()) {
			String ipAddress = matcher.group(1);
			if (!allowedIpAddresses.contains(ipAddress)) {
				throw new SecurityException("Conecting to this IP : "
						+ ipAddress + "is not allowed");
			} else {
				throw new SecurityException("Invalid IP address ...." + address);
			}
		}
		super.addConnection(context, info);
	}

	/*
	 * @see
	 * org.apache.activemq.broker.BrokerFilter#removeConnection(org.apache.activemq
	 * .broker.ConnectionContext, org.apache.activemq.command.ConnectionInfo,
	 * java.lang.Throwable)
	 */
	@Override
	public void removeConnection(ConnectionContext context,
			ConnectionInfo info, Throwable error) throws Exception {
		log.info("Something Weird happened at the client end : " + error);
		super.removeConnection(context, info, error);
	}
}
