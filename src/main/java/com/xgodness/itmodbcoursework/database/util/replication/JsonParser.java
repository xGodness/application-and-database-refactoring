package com.xgodness.itmodbcoursework.database.util.replication;

import com.xgodness.itmodbcoursework.database.util.ResourceExtractor;
import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.Recipe;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class JsonParser {
    private final String itemsJson;
    private final String recipesJson;
    private final String unresolvedItemsJson;
    @Getter
    private final List<Item> itemList = new ArrayList<>();
    @Getter
    private final List<Recipe> recipeList = new ArrayList<>();
    @Getter
    private final List<String> itemNamesToDeleteList = new ArrayList<>();

    public JsonParser() throws JSONException {
        ResourceExtractor resourceExtractor = new ResourceExtractor();
        itemsJson = resourceExtractor.readResourceAsString("replication/items.json");
        recipesJson = resourceExtractor.readResourceAsString("replication/recipes.json");
        unresolvedItemsJson = resourceExtractor.readResourceAsString("replication/unresolved_pic_items.json");
        parseItems();
        parseRecipes();
        parseItemsToDelete();
    }

    private void parseItems() throws JSONException {
        JSONArray jsonArray = new JSONObject(itemsJson).getJSONArray("items");
        JSONObject curObj;
        for (var i = 0; i < jsonArray.length(); i++) {
            curObj = jsonArray.getJSONObject(i);
            itemList.add(
                    new Item(
                            curObj.getLong("id"),
                            curObj.getString("name"),
                            curObj.getString("displayName")
                    )
            );
        }
    }

    private void parseRecipes() throws JSONException {
        JSONObject rootObject = new JSONObject(recipesJson);
        Iterator<String> keyIter = rootObject.keys();
        String key;
        JSONArray curObjRecipes;
        JSONObject result;

        long[][] craftMatrix;
        long resultItemId;
        int resultItemCnt;

        while (keyIter.hasNext()) {
            key = keyIter.next();
            curObjRecipes = rootObject.getJSONArray(key);

            for (var i = 0; i < curObjRecipes.length(); i++) {
                craftMatrix = parseCraftMatrix(curObjRecipes.getJSONObject(i));
                result = curObjRecipes.getJSONObject(i).getJSONObject("result");
                resultItemId = result.getLong("id");
                resultItemCnt = result.getInt("count");

                recipeList.add(
                        new Recipe(
                                null,
                                resultItemId,
                                resultItemCnt,
                                craftMatrix
                        )
                );
            }
        }
    }

    private void parseItemsToDelete() throws JSONException {
        JSONArray jsonArray = new JSONObject(unresolvedItemsJson).getJSONArray("items");
        String cur;
        for (var i = 0; i < jsonArray.length(); i++) {
            cur = (String) jsonArray.get(i);
            itemNamesToDeleteList.add(cur);
        }
    }

    private long[][] parseCraftMatrix(JSONObject obj) throws JSONException {
        var craftMatrix = new long[3][3];
        JSONArray curRow;
        var shape = obj.optJSONArray("inShape");
        if (shape != null) {
            for (int i = 0; i < shape.length(); i++) {
                curRow = shape.getJSONArray(i);
                for (int j = 0; j < curRow.length(); j++)
                    craftMatrix[i][j] = curRow.optInt(j);
            }
        } else {
            shape = obj.optJSONArray("ingredients");
            for (int i = 0; i < shape.length(); i++)
                craftMatrix[i / 3][i % 3] = shape.getInt(i);
        }
        return craftMatrix;
    }
}
