package org.jamie.demo.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/4/18 11:15
 */
@Service
public class RabbitMQListener {

    //@Value("${server.port:}")
    //private String port;
    //
    //
    //@RabbitListener(bindings = {
    //        @QueueBinding(
    //                value = @Queue("${server.port:}"),
    //                exchange = @Exchange(name = "test", type = ExchangeTypes.TOPIC),
    //                key = {"#.${server.port}"}
    //                //key = {"INTE.#"}
    //        )
    //})
    //public void test(Message message) {
    //    System.out.println(port + ": " + new String(message.getBody(), StandardCharsets.UTF_8));
    //}

}
