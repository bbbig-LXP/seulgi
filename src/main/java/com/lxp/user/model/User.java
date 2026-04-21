package com.lxp.user.model;

public class User {

    private Long id;
    private String name;
    private UserType type;

    public User(String name, UserType type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }

    public boolean isInstructor() {
        return type == UserType.INSTRUCTOR;
    }
}
