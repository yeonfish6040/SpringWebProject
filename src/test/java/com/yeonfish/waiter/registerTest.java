package com.yeonfish.waiter;

import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.services.RegisterService;
import com.yeonfish.waiter.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class registerTest {

    @Autowired
    RegisterService registerService;


    @Test
    public void testRegister() {
        RestaurantVO restaurantVO = new RestaurantVO();
        restaurantVO.setUuid(UUID.generate());
        restaurantVO.setName("공차 - Gong cha");
        restaurantVO.setPictures(restaurantVO.getUuid());
        restaurantVO.setCall("028986800");
        restaurantVO.setInfo("글로벌 티(Tea) 음료 전문 브랜드");
        restaurantVO.setLocation1(37.4475988f);
        restaurantVO.setLocation2(126.8833433f);
        restaurantVO.setAdminPhone("01067820989");
        restaurantVO.setActive(0);

        registerService.registerRest(restaurantVO);
    }
}
