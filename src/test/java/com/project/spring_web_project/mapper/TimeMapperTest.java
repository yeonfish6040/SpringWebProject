//package com.project.spring_web_project.mapper;


import com.project.spring_web_project.DevController.logger;
import com.project.spring_web_project.mappers.TimeMapper;
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
