package com.xgodness.itmodbcoursework.service;

import java.sql.SQLException;
import java.util.*;

import com.xgodness.itmodbcoursework.model.Item;
import com.xgodness.itmodbcoursework.model.ItemCraftContext;
import com.xgodness.itmodbcoursework.model.ItemPack;
import com.xgodness.itmodbcoursework.model.Offer;
import com.xgodness.itmodbcoursework.model.OfferAcceptDTO;
import com.xgodness.itmodbcoursework.model.Recipe;
import com.xgodness.itmodbcoursework.model.RecipeFilterContext;
import com.xgodness.itmodbcoursework.model.User;
import com.xgodness.itmodbcoursework.repository.*;
import com.xgodness.itmodbcoursework.util.ResponseData;
import com.xgodness.itmodbcoursework.util.ValidationMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.xgodness.itmodbcoursework.model.validation.RecipeValidator.validateRecipe;
import static com.xgodness.itmodbcoursework.model.validation.UserValidator.validateUser;

@Service
public class MainService {
    private final UserRepository userRepository;
    private final ChestRepository chestRepository;
    private final RecipeRepository recipeRepository;
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public MainService(
            @Autowired UserRepository userRepository,
            @Autowired ChestRepository chestRepository,
            @Autowired RecipeRepository recipeRepository,
            @Autowired ItemRepository itemRepository,
            @Autowired OfferRepository offerRepository
    ) {
        this.userRepository = userRepository;
        this.chestRepository = chestRepository;
        this.recipeRepository = recipeRepository;
        this.itemRepository = itemRepository;
        this.offerRepository = offerRepository;
    }

    public ResponseEntity<ResponseData> saveUser(User user) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        if (userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USERNAME_TAKEN.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> login(User user) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        userRepository.login(user);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> opUser(User user, User target) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userRepository.login(user) < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }
        if (!userRepository.existsByUsername(target.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        }
        if (!userRepository.isAdmin(user)) {
            errorList.add(ValidationMessages.ADMIN_RIGHTS_REQUIRED.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        userRepository.opUser(target);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> openChest(User user) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);
        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        chestRepository.openChest(userId);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> saveRecipe(User user, Recipe recipe) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        errorList.addAll(validateRecipe(recipe));
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        } else if (!userRepository.isAdmin(user)) {
            errorList.add(ValidationMessages.ADMIN_RIGHTS_REQUIRED.getMessage());
        }

        var craftMatrix = recipe.getCraftMatrix();

        if (recipeRepository.existsByCraftMatrix(craftMatrix)) {
            errorList.add(ValidationMessages.RECIPE_ALREADY_EXISTS.getMessage());
        }

        long itemId;
        for (int i = 0; i < 9; i++) {
            itemId = craftMatrix[i / 3][i % 3];
            if (!itemRepository.existsById(itemId)) {
                errorList.add(ValidationMessages.ITEM_DOESNT_EXIST.getMessage());
                break;
            }
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        recipeRepository.save(userId, recipe);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> getAllItems() throws SQLException {
        var responseData = new ResponseData();
        responseData.setResultList(itemRepository.findAll());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> getInventory(User user) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        responseData.setResultList(itemRepository.getInventory(userId));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> craftItem(ItemCraftContext context) throws SQLException {
        var responseData = new ResponseData();
        var user = context.getUser();
        var craftMatrix = context.getCraftMatrix();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        var recipe = recipeRepository.findByCraftMatrix(craftMatrix);
        if (recipe == null) {
            errorList.add(ValidationMessages.RECIPE_DOESNT_EXIST.getMessage());
        }

        List<ItemPack> inventory;
        Map<Long, Integer> requiredItems = new HashMap<>();
        if (userId > 0) {
            long itemId;
            for (int i = 0; i < 9; i++) {
                itemId = craftMatrix[i / 3][i % 3];
                if (itemId == 0) continue;
                requiredItems.merge(itemId, 1, Integer::sum);
            }

            inventory = itemRepository.getInventory(userId);
            int foundCnt = 0;

            for (var entry : inventory) {
                if (requiredItems.containsKey(entry.getItem().getId())) {
                    if (requiredItems.get(entry.getItem().getId()) > entry.getCount()) {
                        break;
                    }
                    foundCnt++;
                }
            }
            if (foundCnt != requiredItems.size()) {
                errorList.add(ValidationMessages.NOT_ENOUGH_ITEMS.getMessage());
            }
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        for (var entry : requiredItems.entrySet()) {
            itemRepository.deleteItemFromInventory(userId, entry.getKey(), entry.getValue());
        }
        itemRepository.saveItemToInventory(userId, recipe.getResultItemId(), recipe.getResultItemCnt());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> findRecipe(long[][] craftMatrix) throws SQLException {
        var responseData = new ResponseData();
        var errorList = new ArrayList<String>();

        var recipe = recipeRepository.findByCraftMatrix(craftMatrix);
        if (recipe == null) {
            errorList.add(ValidationMessages.RECIPE_DOESNT_EXIST.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        Item item = itemRepository.findById(recipe.getResultItemId());

        List<ItemPack> resultList = new ArrayList<>();
        resultList.add(new ItemPack(item, recipe.getResultItemCnt()));

        responseData.setResultList(resultList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> filterRecipes(RecipeFilterContext context) throws SQLException {
        var responseData = new ResponseData();

        responseData.setErrorList(new ArrayList<>());

        var recipeList = recipeRepository.findAll();
        if (context.getFilterBy().isEmpty()) {
            responseData.setResultList(recipeList);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }

        List<Recipe> filteredRecipeList = new ArrayList<>();
        long[][] craftMatrix;
        for (var recipe : recipeList) {
            Set<Long> itemIds = new HashSet<>();
            craftMatrix = recipe.getCraftMatrix();
            for (int i = 0; i < 9; i++) itemIds.add(craftMatrix[i / 3][i % 3]);

            boolean recipeFits = true;
            for (long filterId : context.getFilterBy()) {
                if (!itemIds.contains(filterId)) {
                    recipeFits = false;
                    break;
                }
            }

            if (recipeFits) filteredRecipeList.add(recipe);
        }

        responseData.setResultList(filteredRecipeList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> createOffer(Offer offer) throws SQLException {
        var responseData = new ResponseData();
        var user = offer.getUser();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        var inventory = itemRepository.getInventory(userId);
        Map<Long, Integer> ownedItemPacks = new HashMap<>();
        for (var itemPack : inventory) {
            ownedItemPacks.put(itemPack.getItem().getId(), itemPack.getCount());
        }

        if (offer.getItemsOffered().isEmpty() || offer.getItemsWanted().isEmpty()) {
            errorList.add(ValidationMessages.EMPTY_OFFER.getMessage());
        }

        Integer val;
        for (var itemPack : offer.getItemsOffered()) {
            if (itemPack.getCount() <= 0) {
                errorList.add(ValidationMessages.NEGATIVE_ITEM_COUNT.getMessage());
                break;
            }
            val = ownedItemPacks.get(itemPack.getItem().getId());
            if (val == null || val < itemPack.getCount()) {
                errorList.add(ValidationMessages.NOT_ENOUGH_ITEMS.getMessage());
                break;
            }
        }

        for (var itemPack : offer.getItemsWanted()) {
            if (itemPack.getCount() <= 0) {
                errorList.add(ValidationMessages.NEGATIVE_ITEM_COUNT.getMessage());
                break;
            }
            if (!itemRepository.existsById(itemPack.getItem().getId())) {
                errorList.add(ValidationMessages.ITEM_DOESNT_EXIST.getMessage());
                break;
            }
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        for (var itemPack : offer.getItemsOffered()) {
            itemRepository.deleteItemFromInventory(userId, itemPack.getItem().getId(), itemPack.getCount());
        }

        offerRepository.save(offer, userId);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> acceptOffer(OfferAcceptDTO offerAccept) throws SQLException {
        var responseData = new ResponseData();
        var user = offerAccept.getUser();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        if (!offerRepository.existsById(offerAccept.getOfferId())) {
            errorList.add(ValidationMessages.OFFER_DOESNT_EXIST.getMessage());
        }

        var inventory = itemRepository.getInventory(userId);
        Map<Long, Integer> ownedItemPacks = new HashMap<>();
        for (var itemPack : inventory) {
            ownedItemPacks.put(itemPack.getItem().getId(), itemPack.getCount());
        }

        Offer offer = offerRepository.findById(offerAccept.getOfferId());
        Integer val;
        for (var itemPack : offer.getItemsWanted()) {
            if (itemPack.getCount() <= 0) {
                errorList.add(ValidationMessages.NEGATIVE_ITEM_COUNT.getMessage());
                break;
            }
            val = ownedItemPacks.get(itemPack.getItem().getId());
            if (val == null || val < itemPack.getCount()) {
                errorList.add(ValidationMessages.NOT_ENOUGH_ITEMS.getMessage());
                break;
            }
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        long targetUserId = userRepository.findUserIdByUsername(offer.getUser().getUsername());
        for (var itemPack : offer.getItemsWanted()) {
            itemRepository.deleteItemFromInventory(userId, itemPack.getItem().getId(), itemPack.getCount());
            itemRepository.saveItemToInventory(targetUserId, itemPack.getItem().getId(), itemPack.getCount());
        }
        for (var itemPack : offer.getItemsOffered()) {
            itemRepository.saveItemToInventory(userId, itemPack.getItem().getId(), itemPack.getCount());
        }

        offerRepository.delete(offer.getId());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> getAllOffers() throws SQLException {
        var offerList = offerRepository.findAll();
        for (var offer : offerList) {
            offer.getUser().setUsername(
                    userRepository.findUsernameById(offer.getUser().getId())
            );
        }

        var responseData = new ResponseData();
        responseData.setResultList(offerList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<ResponseData> checkAdminRights(User user) throws SQLException {
        var responseData = new ResponseData();
        var errorList = validateUser(user);
        long userId = userRepository.login(user);

        if (!userRepository.existsByUsername(user.getUsername())) {
            errorList.add(ValidationMessages.USER_DOESNT_EXIST.getMessage());
        } else if (userId < 0) {
            errorList.add(ValidationMessages.INVALID_CREDENTIALS.getMessage());
        }

        responseData.setErrorList(errorList);

        if (!errorList.isEmpty()) {
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

        responseData.setResultList(List.of(userRepository.isAdmin(user)));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
