package com.xgodness.itmodbcoursework.controller;

import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
@RequestMapping("api/ping")
public class PingController {
    @CrossOrigin
    @PostMapping
    public String ping() {
        log.info("[REQUEST] ping");
        return "pong";
    }
}
