package com.njxs.eurekaconsumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("hello")
    public String sayHello(){
        ServiceInstance instance = client.choose("eureka-client");

        String target = String.format("http://%s:%s/sayHi",
                instance.getHost(),
                instance.getPort());
        log.info("url is {}", target);

        return restTemplate.getForObject(target, String.class);
    }

}
