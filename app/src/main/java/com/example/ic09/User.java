package com.example.ic09;

import androidx.annotation.NonNull;

public class User {

    public String id, firstName, lastName;


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
