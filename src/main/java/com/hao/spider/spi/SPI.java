package com.hao.spider.spi;

import java.lang.annotation.*;

/**
 * Created by donghao on 16/8/20.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    String value() default "";

}
