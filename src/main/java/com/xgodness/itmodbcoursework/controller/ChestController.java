package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;

import com.xgodness.itmodbcoursework.model.User;
import com.xgodness.itmodbcoursework.service.MainService;
import com.xgodness.itmodbcoursework.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chest")
public class ChestController {
    private final MainService mainService;

    public ChestController(
            @Autowired MainService mainService
    ) {
        this.mainService = mainService;
    }

    @CrossOrigin
    @PostMapping
    @ResponseBody
    public ResponseEntity<ResponseData> openChest(
            @RequestBody User user
    ) throws SQLException {
        return mainService.openChest(user);
    }
}
