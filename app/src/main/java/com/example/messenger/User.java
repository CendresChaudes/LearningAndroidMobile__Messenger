package com.example.messenger;

import androidx.annotation.NonNull;

public class User {

    private String id;
    private String name;
    private String lastname;
    private int age;
    private boolean isOnline;

    public User() {}

    public User(
            String id,
            String name,
            String lastname,
            int age,
            boolean isOnline
    ) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.age = age;
        this.isOnline = isOnline;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public int getAge() {
        return this.age;
    }

    public boolean getIsOnline() {
        return this.isOnline;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", isOnline=" + isOnline +
                '}';
    }
}
