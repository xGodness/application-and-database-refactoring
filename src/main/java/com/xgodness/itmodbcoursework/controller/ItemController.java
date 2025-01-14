package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;

import com.xgodness.itmodbcoursework.model.ItemCraftContext;
import com.xgodness.itmodbcoursework.model.User;
import com.xgodness.itmodbcoursework.service.MainService;
import com.xgodness.itmodbcoursework.util.ResponseData;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/api/item")
public class ItemController {
    private final MainService mainService;

    public ItemController(
            @Autowired MainService mainService
    ) {
        this.mainService = mainService;
    }

    @CrossOrigin
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<ResponseData> getAllItems() throws SQLException {
        log.info("[REQUEST] get all items");
        return mainService.getAllItems();
    }

    @CrossOrigin
    @PostMapping("/inventory")
    @ResponseBody
    public ResponseEntity<ResponseData> getInventory(
            @RequestBody User user
    ) throws SQLException {
        log.info("[REQUEST] get inventory of user %s with id %d".formatted(user.getUsername(), user.getId()));
        return mainService.getInventory(user);
    }

    @CrossOrigin
    @PostMapping("/craft")
    @ResponseBody
    public ResponseEntity<ResponseData> craft(
            @RequestBody ItemCraftContext context
    ) throws SQLException {
        log.info("[REQUEST] craft item attempt by user %s with id %d"
                .formatted(context.getUser().getUsername(), context.getUser().getId()));
        return mainService.craftItem(context);
    }
}
