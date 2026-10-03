package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.bean.dto.ApiDni;
import com.example.demo.bean.dto.ApiRuc;

@FeignClient(name = "apiPeruClient", url = "${apiperu.url}")
public interface ApiPeruClient {

    @GetMapping("/dni/{numero}")
    ApiDni consultarDni(@PathVariable("numero") String numero,
                        @RequestParam("token") String token);

    @GetMapping("/ruc/{numero}")
    ApiRuc consultarRuc(@PathVariable("numero") String numero,
                        @RequestParam("token") String token);
}