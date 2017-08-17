package com.springboot.camel;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;




public class CamelServiceRoute extends RouteBuilder{


	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.5.45:61616");
	@Override
	public void configure() throws Exception {
		System.out.println("Inside configure method");
		CamelContext camelContext = getContext();
		
		camelContext.addComponent("activemq", jmsComponentAutoAcknowledge(connectionFactory));
		
		from("timer:foo?fixedRate=true&period=5000&delay=3000")
		.routeId("proxy-service")
		
		.to("restlet:http://microservice-goep-demo.apps.dev.openshift.opentlc.com/employees?restletMethod=get")
		.log(LoggingLevel.INFO, "${body}")
		
		.to("activemq:queue:TEST.GOEP?disableReplyTo=true")
		.log(LoggingLevel.INFO, "###########Message successfully sent to destination###########");
		
	}
	
	/**
	 * 
	 * @param connectionFactory
	 * @return
	 */
	public static JmsComponent jmsComponentAutoAcknowledge(ConnectionFactory connectionFactory) {
		        JmsConfiguration template = new JmsConfiguration(connectionFactory);
		        template.setAcknowledgementMode(Session.AUTO_ACKNOWLEDGE);
		        return new JmsComponent(template);
		    }
}
