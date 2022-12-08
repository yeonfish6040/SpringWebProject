package com.project.waiter.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestaurantVO {
    private String uuid;
    private String name;
    private float location1;
    private float location2;
    private String call;
    private String info;
    private int waits;
    private int c_wait_time;
    private String pictures;
    private String adminPhone;
    private int active;
}