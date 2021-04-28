package com.njxs.rabbitmqconsumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitRecevier {

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="queue-1",durable = "true"),
            exchange = @Exchange(name = "exchange-1",durable = "true",
                    type = "topic",ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
        )
    )
    public void onMessage(Message message, Channel channel) throws IOException {
        System.err.println("-----------------------");
        System.err.println("消费消息："+message.getPayload());

        Long deliverTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverTag,false);
    }

}
