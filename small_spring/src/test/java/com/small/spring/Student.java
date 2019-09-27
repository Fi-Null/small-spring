package com.small.spring;

import com.small.spring.beans.annotation.AutoWired;

/**
 * @ClassName Student
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/24 23:20
 * @Version 1.0
 **/
public class Student {

    private String stuName;
    @AutoWired
    private School school;

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public School getSchool() {
        return school;
    }
//
//    public void setSchool(School school) {
//        this.school = school;
//    }

    public void learning() {
        System.out.println("i am learning");
    }

}
