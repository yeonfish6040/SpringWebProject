package com.project.waiter.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserVO {
    private String phone;
    private String uuid;
    private boolean verified;
}
