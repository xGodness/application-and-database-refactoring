package com.xgodness.itmodbcoursework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private Long id;
    private long resultItemId;
    private int resultItemCnt;
    private long[][] craftMatrix = new long[3][3];
}
