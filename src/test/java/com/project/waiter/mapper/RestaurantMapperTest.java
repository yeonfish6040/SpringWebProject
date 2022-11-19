package com.project.waiter.mapper;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.mappers.RestaurantMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RestaurantMapperTest {
    private final logger log = new logger();

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Test
    @Disabled
    public void insert() {
        RestaurantVO restaurantVO = new RestaurantVO();
        restaurantVO.setUuid("a");
        restaurantVO.setName("a");
        restaurantVO.setInfo("a");
        restaurantVO.setCall(1);
        restaurantVO.setWaits(1);
        restaurantVO.setC_wait_time(1);
        restaurantVO.setLocation1(1);
        restaurantVO.setLocation2(1);
        restaurantVO.setPictures("a");
        log.info(restaurantMapper.add(restaurantVO));
    }

    @Test
    @Disabled
    public void get() {
        log.info(restaurantMapper.get("a"));
    }

//    @Test
//    @Disabled
//    public void getList() {
//        log.info(restaurantMapper.getList(1));
//    }
}
