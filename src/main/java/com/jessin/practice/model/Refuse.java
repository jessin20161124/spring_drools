package com.jessin.practice.model;

/**
 * @author
 * @create 2019-02-01 上午11:40
 **/


import lombok.Data;

import java.io.Serializable;

/**
 * 拒件实体
 * @author Administrator
 *
 */
@Data
public class Refuse implements Serializable {

    /**
     * 年龄
     */
    private int age;
    /**
     * 工作城市
     */
    private String workCity;
    /**
     * 申请城市
     */
    private String applyCity;
    /**
     * 居住城市
     */
    private String addressCity;
}
