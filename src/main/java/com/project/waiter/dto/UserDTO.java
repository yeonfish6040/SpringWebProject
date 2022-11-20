package com.project.waiter.dto;

import com.project.waiter.DevController.logger;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@Data
public class UserDTO {
    private final logger log = new logger();

    private String uuid;
    private int phone;
}
