package org.jamie.demo.controller;

import org.jamie.demo.properties.DemoProperties;
import org.jamie.demo.service.TestService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.MapPropertySource;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description TODO
 * @date 2023/02/14 22:36
 */

@RestController
@RefreshScope
public class DemoController {

    public static final Logger LOGGER = Logger.getLogger(DemoController.class.getName());

    @Autowired
    private ContextRefresher CONTEXT_REFRESHER;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private ConfigurableEnvironment environment;

    //@Autowired
    //private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @Autowired
    private DemoProperties demoProperties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // private final TestService testService;
    //
    // public DemoController(TestService testService) {
    //     this.testService = testService;
    // }


    @RequestMapping("/hello")
    public String get() {
        return demoProperties.getTest() + "---" + environment.getProperty("abc");
    }

    @GetMapping("/refresh")
    public String refresh(String a) {
        //StaticUtil.refresh();
        Map<String, String> map = new HashMap<>();
        map.put("demo.test", a);
        map.put("abc", "84f6e8dq");
        //new ResourcePropertySource()
        environment.getPropertySources().addFirst(new MapPropertySource("dynamic", Collections.unmodifiableMap(map)));
        new Thread(() -> CONTEXT_REFRESHER.refresh()).start();
        return "ok";
    }

    @Value("${server.port}")
    private String port;
    @PostMapping("/mq")
    public String send2Mq(String text) {
        rabbitTemplate.convertAndSend("test", "INTE.8887", text);
        return port + " send: " + text;
    }

    @GetMapping("/bool")
    public boolean bool() {
        return false;
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("yozo")
    public Map yozo(String fileUrl) {
        boolean flag = false;
        String domain = "https://yozo-fcscloud.dfy.definesys.cn/fcscloud/";
        String uri = "/composite/httpfile";
        MultiValueMap<String, Object> req = new LinkedMultiValueMap<>();
        if (domain.endsWith("/") && uri.startsWith("/")) {
            uri = uri.substring(1);
        }


        req.add("convertType", "0");
        req.add("fileUrl", new String(Base64.getDecoder().decode(fileUrl), StandardCharsets.UTF_8));

        LOGGER.info(req.toString());

        HashMap<String, Object> result = new HashMap<>();


        Map resp = restTemplate.postForObject(domain.concat(uri), new HttpEntity<>(req), Map.class);

        LOGGER.info(resp.toString());
        if (resp!= null && resp.get("data") != null) {
            LOGGER.info(resp.get("data").toString());
            flag = true;
            result.put("status", flag ? "ok" : "err" );
            result.put("url", ((Map) resp.get("data")).get("viewUrl"));
        }


        return result;
    }
}
