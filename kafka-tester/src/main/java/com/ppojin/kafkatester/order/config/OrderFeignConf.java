package com.ppojin.kafkatester.order.config;


import lombok.Getter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${order.url:localhost:8181}/order")
public interface OrderFeignConf {
    @PostMapping
    ResponseEntity order(@RequestBody OrderDTO order);


}
