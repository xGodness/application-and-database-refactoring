package com.xgodness.itmodbcoursework.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilterContext {
    private List<Long> filterBy;
}
