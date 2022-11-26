package com.project.waiter.serviceTest;

import com.project.waiter.DevController.logger;
import com.project.waiter.services.GeneralService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    private final logger log = new logger();

    @Autowired
    GeneralService generalService;


    @Test
    public void register() {
        generalService.get_user("01067820989");
    }
}
