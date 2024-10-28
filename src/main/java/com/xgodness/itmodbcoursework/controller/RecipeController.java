package com.xgodness.itmodbcoursework.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.model.CraftMatrixDto;
import com.xgodness.itmodbcoursework.model.Recipe;
import com.xgodness.itmodbcoursework.model.RecipeCreationContext;
import com.xgodness.itmodbcoursework.model.RecipeFilterContext;
import com.xgodness.itmodbcoursework.service.MainService;
import com.xgodness.itmodbcoursework.util.ResponseData;
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

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final MainService mainService;

    public RecipeController(
            @Autowired MainService mainService
    ) {
        this.mainService = mainService;
    }

    @CrossOrigin
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<ResponseData> saveRecipe(
            @RequestBody RecipeCreationContext recipeCreationContext
    ) throws SQLException {
        return mainService.saveRecipe(recipeCreationContext.getUser(), recipeCreationContext.getRecipe());
    }

    @CrossOrigin
    @PostMapping("/check")
    @ResponseBody
    public ResponseEntity<ResponseData> findRecipe(
            @RequestBody CraftMatrixDto craftMatrix
    ) throws SQLException {
        return mainService.findRecipe(craftMatrix.getCraftMatrix());
    }

    @CrossOrigin
    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<ResponseData> getFilteredRecipes(
            @RequestBody RecipeFilterContext context
    ) throws SQLException {
        return mainService.filterRecipes(context);
    }
}
