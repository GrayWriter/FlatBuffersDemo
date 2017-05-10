package com.example.guofengwu.flatbufferdemo.javaBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guofeng.wu on 2017/5/9.
 */

public class JavaPerson implements Serializable{
    String name;
    JavaPerson spouse;
    List<JavaPerson> friends;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JavaPerson getSpouse() {
        return spouse;
    }

    public void setSpouse(JavaPerson spouse) {
        this.spouse = spouse;
    }

    public List<JavaPerson> getFriends() {
        return friends;
    }

    public void setFriends(List<JavaPerson> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "JavaPerson{" +
                "name='" + name + '\'' +
                ", spouse=" + spouse +
                ", friends=" + friends +
                '}';
    }
}
