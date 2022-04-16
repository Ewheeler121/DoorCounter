package com.company;
import java.awt.image.BufferedImage;

class User {
    public enum UserType {
        STUDENT, TEACHER, ADMIN
    }

    private UserType type;
    private String firstName;
    private String lastName;
    private String schoolId;
    private String username;
    private String passwordHash = "";
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public User() {
        type = UserType.STUDENT;
        firstName = "NULL";
        lastName = "NULL";
        schoolId = "NULL";
        username = "NULL";
        passwordHash = "NULL";
    }

}