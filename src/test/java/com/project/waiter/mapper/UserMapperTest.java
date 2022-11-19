package com.project.waiter.mapper;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.UserVO;
import com.project.waiter.mappers.UserMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    private final logger log = new logger();

    @Autowired
    private UserMapper userMapper;

    @Test
    @Disabled
    public void insert() {
        UserVO userVO = new UserVO();
        userVO.setUuid("a");
        userVO.setPhone(1);
        userMapper.add(userVO);
    }

    @Test
    @Disabled
    public void get() {
        log.info(userMapper.get(1));
    }
}
