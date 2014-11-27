package com.redhat;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

/**
 * @author vgohel
 *
 */
public class PublisherTest {

	private int MAX_DELTA_PERCENT = 1;
	private Map<String, Double> LAST_PRICES = new Hashtable<String, Double>();
	private static int count = 10;
	private static int total;

	private static String brokerURL = "tcp://localhost:61616";
	private static transient ConnectionFactory factory;
	private transient Connection connection;
	private transient Session session;
	private transient MessageProducer producer;

	private String username = "admin";
	private String password = "admin";

	/**
	 * 
	 */
	public PublisherTest() throws JMSException {
		super();
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection(username, password);
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(null);
	}

	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * @param args
	 * @throws JMSException
	 */
	public static void main(String[] args) throws JMSException {

		PublisherTest publisher = new PublisherTest();
		while (total < 1000) {
			for (int i = 0; i < count; i++) {
				publisher.sendMessage(args);
			}
			total += count;
			System.out.println("Published '" + count + "' of '" + total
					+ "' price messages");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException x) {
			}
		}
		publisher.close();
	}

	/**
	 * @param args
	 * @throws JMSException
	 */
	private void sendMessage(String[] stocks) throws JMSException {
		int idx = 0;
		while (true) {
			idx = (int) Math.round(stocks.length * Math.random());
			if (idx < stocks.length) {
				break;
			}
		}
		String stock = stocks[idx];
		Destination destination = session.createTopic("STOCKS." + stock);
		Message message = createStockMessage(stock, session);
		System.out.println("Sending: "
				+ ((ActiveMQMapMessage) message).getContentMap()
				+ " on destination: " + destination);
		producer.send(destination, message);
	}

	/**
	 * @param stock
	 * @param session
	 * @return
	 * @throws JMSException
	 */
	private Message createStockMessage(String stock, Session session)
			throws JMSException {
		Double value = LAST_PRICES.get(stock);
		if (value == null) {
			value = new Double(Math.random() * 100);
		}

		// lets mutate the value by some percentage
		double oldPrice = value.doubleValue();
		value = new Double(mutatePrice(oldPrice));
		LAST_PRICES.put(stock, value);
		double price = value.doubleValue();

		double offer = price * 1.001;

		boolean up = (price > oldPrice);
		MapMessage message = session.createMapMessage();
		message.setString("stock", stock);
		message.setDouble("price", price);
		message.setDouble("offer", offer);
		message.setBoolean("up", up);
		return message;

	}

	/**
	 * @param price
	 * @return
	 */
	private double mutatePrice(double price) {
		double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT)
				- MAX_DELTA_PERCENT;

		return price * (100 + percentChange) / 100;

	}

}
