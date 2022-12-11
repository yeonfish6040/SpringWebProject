package com.project.waiter.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class WaitsVO {
    private String uuid;
    private String r_uuid;
    private int waitNum;
    private String endpoint;
    private String p256dh;
    private String auth;
}
