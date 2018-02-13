package com.jingtuo.android.lottery.model.entity;


import lombok.Data;

/**
 * 双色球信息
 * Created by jingtuo on 2018/2/12.
 */
@Data
public class DcbInfo {
    private int year;//年份
    private int serialNo;//第serialNo期
    private String redBall;//红球结果
    private String blueBall;//蓝球结果
    private long insertTime;
}
