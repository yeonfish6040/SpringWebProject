package com.project.waiter.mapper;

import com.project.waiter.DevController.logger;
import com.project.waiter.mappers.TimeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeMapperTest {

    private logger log = new logger();

    @Autowired
    TimeMapper mapper;

    @Test
    public void getTimeTest() {
        log.title("getTime");
        log.info(mapper.getTime());
    }
}
