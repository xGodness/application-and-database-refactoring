package com.xgodness.itmodbcoursework.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    Long id;
    User user;
    private List<ItemPack> itemsWanted;
    private List<ItemPack> itemsOffered;
}
