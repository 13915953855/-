package com.njxs.rabbitmqproducer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息确认回调方法，用于确认消息是否被broker接收
     */
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         *
         * @param correlationData 唯一标识
         * @param ack   broker是否落盘成功
         * @param s 失败的异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            System.err.println("消息ACK消息："+ack);
            System.err.println("correlationData:"+correlationData.getId());
        }
    };

    public void send(Object msg, Map<String,Object> props) {
        MessageHeaders mhs = new MessageHeaders(props);

        Message<Object> message = MessageBuilder.createMessage(msg, mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);

        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange-1", "springboot.rabbit", message,
                new MessagePostProcessor() {
                    @Override
                    public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                        System.out.println("--> post to do :"+message);
                        return message;
                    }
                },data);

    }
}
