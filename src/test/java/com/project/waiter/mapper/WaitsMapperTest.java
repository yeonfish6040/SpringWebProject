package com.project.waiter.mapper;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.mappers.WaitsMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WaitsMapperTest {
    private final logger log = new logger();

    @Autowired
    private WaitsMapper waitsMapper;

    @Test
    @Disabled
    public void insert() {
        WaitsVO waitsVO = new WaitsVO();

        waitsVO.setUuid("b");
        waitsVO.setR_uuid("a");
        waitsVO.setWaitNum(1);

        log.info(waitsMapper.add(waitsVO));
    }

    @Test
    @Disabled
    public void get() {
        log.info(waitsMapper.get("a"));
    }

    @Test
    @Disabled
    public void delete() {
//        log.info(waitsMapper.delete("a"));
    }
}
