package com.project.waiter.serviceTest;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.services.RegisterService;
import com.project.waiter.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class registerRest {

    private final logger log = new logger();

    @Autowired
    RegisterService registerService;

    @Test
    public void regist() {
        RestaurantVO vo = new RestaurantVO();
        vo.setUuid(UUID.generate());
        vo.setName("소과당");
        vo.setCall("025388188");
        vo.setInfo("소과당은 수플레팬케이크, 푸딩, 이태리피자 등의 달콤하고 향긋한 디저트카페 메뉴&다양한 커피음료를 제공하고 있습니다.");
        vo.setLocation1(37.4997687F);
        vo.setLocation2(127.0280046F);
        vo.setPictures(vo.getUuid());
        registerService.registerRest(vo);
    }
}
