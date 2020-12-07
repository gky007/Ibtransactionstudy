package com.jamie.server1.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jamie
 * @version 1.0.0
 * @date 2020/12/6 19:17
 * @description
 */
public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String sex;
    private int score;
    private int copyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }
}
