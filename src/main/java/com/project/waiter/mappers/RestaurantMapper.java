package com.project.waiter.mappers;

import com.project.waiter.beans.vo.RestaurantVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    public List<RestaurantVO> getList(int num, double loc1, double loc2);
    public RestaurantVO get(String uuid);
    public int add(RestaurantVO restaurantVO);
}
