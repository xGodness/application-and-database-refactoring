package com.xgodness.itmodbcoursework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferAcceptDTO {
    private User user;
    private long offerId;
}
