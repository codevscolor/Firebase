package com.codevscolor.firebasedemo.realtimedatabase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Person {
    public String name;
    public int age;

    public Person(){

    }

    public Person(String pName, int pAge) {
        this.name = pName;
        this.age = pAge;
    }

}
