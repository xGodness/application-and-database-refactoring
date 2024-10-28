package com.xgodness.itmodbcoursework.model.validation;

import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.model.Recipe;

public class RecipeValidator {
    private RecipeValidator() { }

    public static List<String> validateRecipe(Recipe recipe) {
        List<String> errorList = new ArrayList<>();
        var craftMatrix = recipe.getCraftMatrix();
        for (int i = 0; i < 9; i++) {
            if (craftMatrix[i / 3][i % 3] < 0) {
                errorList.add("Item id in recipe craft matrix must not be negative");
                break;
            }
        }
        if (recipe.getResultItemId() < 0) {
            errorList.add("Result item id must not be negative");
        }
        if (recipe.getResultItemCnt() <= 0) {
            errorList.add("Result item count must be positive");
        }
        return errorList;
    }
}
