package com.xgodness.itmodbcoursework.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ping")
public class PingController {
    @CrossOrigin
    @PostMapping
    public String ping() {
        return "pong";
    }
}
