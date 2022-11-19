package com.project.waiter.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserVO {
    private int phone;
    private String uuid;
}
