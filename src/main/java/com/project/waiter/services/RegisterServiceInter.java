package com.project.waiter.services;

import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.beans.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegisterServiceInter {
    public boolean registerRest(RestaurantVO restaurantVO);

    public boolean registerUser(UserVO userVO);
}
