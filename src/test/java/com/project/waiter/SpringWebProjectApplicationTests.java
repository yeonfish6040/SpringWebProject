package com.project.waiter;

import com.project.waiter.util.Messages;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;

@SpringBootTest
class SpringWebProjectApplicationTests {

    @Test
    void contextLoads() throws FileNotFoundException {
        Messages m = new Messages();
        m.send("01067820989", "01067820989", "hi");
    }

}
