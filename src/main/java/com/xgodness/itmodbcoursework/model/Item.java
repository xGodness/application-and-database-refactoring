package com.xgodness.itmodbcoursework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long id;
    private String name;
    private String displayName;
}
