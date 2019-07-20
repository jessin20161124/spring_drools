package com.jessin.practice.model;

/**
 * @Author: zexin.guo
 * @Date: 19-7-17 下午7:49
 */
public class Message {

    public static final String HELLO_WORLD = "hello world";

    public static final String GOOD_BYE = "good byte";

    private String status;

    private String desc;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Message{" +
                "status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
