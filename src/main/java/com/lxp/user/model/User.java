package com.lxp.user.model;

public class User {

    private Long id;
    private String name;
    private UserType type;

    private User() {
    }

    private User(String name, UserType type) {
        validateName(name);
        validateUserType(type);

        this.name = name;
        this.type = type;
    }

    public static User create(String name, UserType type) {
        return new User(name, type);
    }

    public static User reconstruct(Long id, String name, UserType type) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.type = type;
        return user;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
    }

    private void validateUserType(UserType type) {
        if (type == null) {
            throw new IllegalArgumentException("사용자 타입은 필수입니다.");
        }
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
