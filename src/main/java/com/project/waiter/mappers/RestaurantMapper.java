package com.project.waiter.mappers;

import com.project.waiter.beans.vo.RestaurantVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    public List<RestaurantVO> getList(int num, double loc1, double loc2);
    public List<RestaurantVO> search(int num, String query);
    public RestaurantVO get(String uuid);
    public RestaurantVO get2(String uuid, String phone);
    public int add(RestaurantVO restaurantVO);
    public int cSts(String uuid, int type);
}
