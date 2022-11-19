package com.project.waiter.mappers;

import com.project.waiter.beans.vo.RestaurantVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestaurantMapper {
    public List<RestaurantVO> getList(int num);
    public RestaurantVO get(String uuid);
    public int add(RestaurantVO restaurantVO);
}
