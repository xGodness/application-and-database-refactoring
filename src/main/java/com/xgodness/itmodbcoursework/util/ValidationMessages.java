package com.xgodness.itmodbcoursework.util;

public enum ValidationMessages {
    ADMIN_RIGHTS_REQUIRED("Admin rights are required"),
    EMPTY_OFFER("Items wanted and items offered must not be empty"),
    INVALID_CREDENTIALS("Invalid credentials"),
    ITEM_DOESNT_EXIST("Item does not exist"),
    NEGATIVE_ITEM_COUNT("Item count must be positive"),
    NOT_ENOUGH_ITEMS("Not enough items"),
    OFFER_DOESNT_EXIST("Offer does not exist"),
    RECIPE_ALREADY_EXISTS("Recipe already exists"),
    RECIPE_DOESNT_EXIST("Recipe does not exist"),
    USERNAME_TAKEN("Username is already taken"),
    USER_DOESNT_EXIST("User does not exist");

    final String message;

    ValidationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
