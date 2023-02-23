package com.yeonfish.waiter.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@Data
public class RestaurantDTO {
    private String uuid;
    private String name;
    private String call;
    private int waits;
    private int c_wait_time;
    private String info;
    private String location;
    private String pictures;
}
