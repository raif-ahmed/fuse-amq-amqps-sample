package com.redhat.demo.rahmed;


import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix = "address")
public class AMQPDemo extends RouteBuilder {
	private String topicName;
	private String subcsribtionName;
	private String queueName;

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getSubcsribtionName() {
		return subcsribtionName;
	}

	public void setSubcsribtionName(String subcsribtionName) {
		this.subcsribtionName = subcsribtionName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Map<String, Object> processDummyJMSMessage() {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", "1");
		map.put("MESSAGE_ATTRIBUTE_1", "asdsad");
		map.put("MESSAGE_ATTRIBUTE_2", "asdasddsasad");
		return map;
	}
	
	@Override
	public void configure() {
		from("timer:demo?period=3000").routeId("route-timer-producer").streamCaching().tracing()
			//I need to create a JMS
	 		//.bean(ConsumerTopic.class, "processDummyJMSMessage()")
		     .setBody (simple ("Hello World !!"))
	 		//.setExchangePattern(ExchangePattern.InOnly)
	 		.log("Sending Message ${body} to Queue amqp:queue:"+getQueueName())
	 		.to("amqp:queue:"+getQueueName())
		    //.log("Sending Message ${body} to Topic amqp:topic:"+getTopicName())
		 	//.to("amqp:topic:"+getTopicName())
         .end();

		
		from("amqp:queue:"+getQueueName()).routeId("route-from-incoming-amqp").streamCaching().tracing()
 			.log("Recieved Message ${body} from Queue amqp:queue:"+getQueueName())
 		.end();

	}
}
