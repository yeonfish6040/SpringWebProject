package com.project.waiter.beans.dao;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.mappers.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantDAO {
    private final logger log = new logger();

    @Autowired
    private RestaurantMapper restaurantMapper;

    public boolean register(RestaurantVO restaurantVO) {
        return restaurantMapper.add(restaurantVO) > 0;
    }

    public List<RestaurantVO> getList(int num, double loc1, double loc2) {
        return restaurantMapper.getList(num, loc1, loc2);
    }

    public List<RestaurantVO> search(int num, String name) {
        return restaurantMapper.search(num, name);
    }

    public RestaurantVO get(String uuid) {
        return restaurantMapper.get(uuid);
    }
    public RestaurantVO get(String uuid, String phone) {
        return restaurantMapper.get2(uuid, phone);
    }

    public int cSts(String uuid, int type) {
        log.info(uuid);
        return restaurantMapper.cSts(uuid, type);
    }
}
