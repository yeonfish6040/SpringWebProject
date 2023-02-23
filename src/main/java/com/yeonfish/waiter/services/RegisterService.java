package com.yeonfish.waiter.services;

import com.yeonfish.waiter.beans.dao.RestaurantDAO;
import com.yeonfish.waiter.beans.dao.UserDAO;
import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.beans.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements RegisterServiceInter {

    @Autowired
    RestaurantDAO restaurantDAO;

    @Autowired
    UserDAO userDAO;

    @Override
    public boolean registerRest(RestaurantVO restaurantVO) {
        return restaurantDAO.register(restaurantVO);
    }

    @Override
    public boolean registerUser(UserVO userVO) {
        return userDAO.register(userVO);
    }
}
