package com.njxs.rabbitmqproducer;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitmqProducerApplicationTests {

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    void testSender() {
        Map<String,Object> map = new HashMap<>();
        map.put("attr1","12345");
        map.put("attr2","abcde");
        rabbitSender.send("我是徐森",map);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
