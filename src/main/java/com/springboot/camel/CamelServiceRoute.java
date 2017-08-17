package com.springboot.camel;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;




public class CamelServiceRoute extends RouteBuilder{



	@Override
	public void configure() throws Exception {
		System.out.println("Inside configure method");
		CamelContext camelContext = getContext();
		camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("tcp://127.0.0.1:61616"));
		
		from("timer:foo?fixedRate=true&period=5000&delay=3000")
		.to("restlet:http://microservice-goep-demo.apps.dev.openshift.opentlc.com/employees?restletMethod=get")
		.log(LoggingLevel.INFO, "${body}")
		.to("activemq:queue:TEST.GOEP?disableReplyTo=true");//fis lab
		

		
		
		
	}
}
