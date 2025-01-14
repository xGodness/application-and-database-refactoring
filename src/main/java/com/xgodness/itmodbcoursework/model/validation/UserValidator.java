package com.xgodness.itmodbcoursework.model.validation;

import java.util.ArrayList;
import java.util.List;

import com.xgodness.itmodbcoursework.model.User;

public class UserValidator {

    private UserValidator() {
    }

    public static List<String> validateUser(User user) {
        List<String> errorList = new ArrayList<>();
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null) {
            errorList.add("Username is required");
        } else {
            if (username.isEmpty())
                errorList.add("Username must not be empty");
            else if (username.length() > 32)
                errorList.add("Username must contain no more than 32 characters");
        }

        if (password == null) {
            errorList.add("Password is required");
        } else {
            if (password.isEmpty())
                errorList.add("Password must not be empty");
            else if (password.length() > 32) {
                errorList.add("Password must contain no more than 32 characters");
            }
        }

        return errorList;
    }
}
