package com.xgodness.itmodbcoursework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCraftContext {
    private User user;
    private long[][] craftMatrix;
}
