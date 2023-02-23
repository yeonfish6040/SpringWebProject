package com.yeonfish.waiter.services;

import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.beans.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public interface RegisterServiceInter {
    public boolean registerRest(RestaurantVO restaurantVO);

    public boolean registerUser(UserVO userVO);
}
