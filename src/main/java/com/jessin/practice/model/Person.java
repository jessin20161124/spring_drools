package com.jessin.practice.model;

/**
 * @Author: zexin.guo
 * @Date: 19-7-20 下午10:45
 */
public class Person {
    private int age;
    private String desc;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", desc='" + desc + '\'' +
                '}';
    }
}
