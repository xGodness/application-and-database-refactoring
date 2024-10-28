package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;

import com.xgodness.itmodbcoursework.model.User;
import com.xgodness.itmodbcoursework.service.MainService;
import com.xgodness.itmodbcoursework.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final MainService mainService;

    public UserController(@Autowired MainService mainService) {
        this.mainService = mainService;
    }

    @CrossOrigin
    @PostMapping("/save")
    @ResponseBody
    ResponseEntity<ResponseData> saveUser(
            @RequestBody User user
    ) throws SQLException {
        return mainService.saveUser(user);
    }

    @CrossOrigin
    @PostMapping("/op/{target_user}")
    @ResponseBody
    ResponseEntity<ResponseData> opUser(
            @RequestBody User user,
            @PathVariable("target_user") String username
    ) throws SQLException {
        User target = new User(null, username, null);
        return mainService.opUser(user, target);
    }

    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    ResponseEntity<ResponseData> login(
            @RequestBody User user
    ) throws SQLException {
        return mainService.login(user);
    }

    @CrossOrigin
    @PostMapping("/amiadmin")
    @ResponseBody
    ResponseEntity<ResponseData> amIAdmin(
            @RequestBody User user
    ) throws SQLException {
        return mainService.checkAdminRights(user);
    }
}
