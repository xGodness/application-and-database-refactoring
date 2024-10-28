package com.xgodness.itmodbcoursework.database.util.replication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.Recipe;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParser {
    private final String itemsJson;
    private final String recipesJson;
    private final String unresolvedItemsJson;
    private final List<Item> itemList = new ArrayList<>();
    private final List<Recipe> recipeList = new ArrayList<>();
    private final List<String> itemNamesToDeleteList = new ArrayList<>();

    public JsonParser() throws IOException, JSONException {
        itemsJson = Files.readString(
                Paths.get("src/main/java/com/xgodness/itmodbcoursework/database/util/replication/items.json")
        );
        recipesJson = Files.readString(
                Paths.get("src/main/java/com/xgodness/itmodbcoursework/database/util/replication/recipes.json")
        );
        unresolvedItemsJson = Files.readString(
                Paths.get("src/main/java/com/xgodness/itmodbcoursework/database/util/replication/unresolved_pic_items.json")
        );
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

    public List<Item> getItemList() {
        return itemList;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public List<String> getItemNamesToDeleteList() {
        return itemNamesToDeleteList;
    }
}
