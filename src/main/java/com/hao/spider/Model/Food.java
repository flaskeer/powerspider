package com.hao.spider.Model;

import lombok.*;


/**
 * @author dongh
 * @Date 16/9/16
 * @Time 上午11:15
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Food {

    private String imgSrc;  //picture src
    private String info;
    private String desc;
    private String unit;
    private double price;





}
