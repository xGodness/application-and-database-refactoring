package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.model.Offer;
import com.xgodness.itmodbcoursework.model.OfferAcceptDTO;
import com.xgodness.itmodbcoursework.model.User;
import com.xgodness.itmodbcoursework.service.MainService;
import com.xgodness.itmodbcoursework.util.ResponseData;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/offer")
public class OfferController {
    private final MainService mainService;

    public OfferController(
            @Autowired MainService mainService
    ) {
        this.mainService = mainService;
    }

    @CrossOrigin
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ResponseData> createOffer(
            @RequestBody Offer offer
    ) throws SQLException {
        log.info("[REQUEST] create offer by user %s with id %d"
                .formatted(offer.getUser().getUsername(), offer.getUser().getId()));
        return mainService.createOffer(offer);
    }

    @CrossOrigin
    @PostMapping("/accept")
    @ResponseBody
    public ResponseEntity<ResponseData> acceptOffer(
            @RequestBody OfferAcceptDTO offerAccept
    ) throws SQLException {
        log.info("[REQUEST] accept offer with id %s by user %s with id %d"
                .formatted(offerAccept.getOfferId(), offerAccept.getUser().getUsername(), offerAccept.getUser().getId()));
        return mainService.acceptOffer(offerAccept);
    }

    @CrossOrigin
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<ResponseData> getAllOffers() throws SQLException {
        log.info("[REQUEST] get all offers");
        return mainService.getAllOffers();
    }
}
