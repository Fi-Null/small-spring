package com.small.spring;

import com.small.spring.beans.annotation.AutoWired;
import com.small.spring.beans.annotation.Value;

/**
 * @ClassName School
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/24 23:19
 * @Version 1.0
 **/
public class School {

    @AutoWired
    private Student student;
    private String name;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Student getStudent() {
        return student;
    }
//
//    public void setStudent(Student student) {
//        this.student = student;
//    }



}
