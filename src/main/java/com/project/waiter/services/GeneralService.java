package com.project.waiter.services;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.dao.RestaurantDAO;
import com.project.waiter.beans.dao.UserDAO;
import com.project.waiter.beans.dao.WaitsDAO;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.beans.vo.UserVO;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneralService implements GeneralServiceInter {
    private final logger log = new logger();

    @Autowired
    RegisterService registerService;

    @Autowired
    RestaurantDAO restaurantDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    WaitsDAO waitsDAO;

    @Override
    public UserVO get_user(int phone) {
        UserVO user = userDAO.get(phone);
        if (user == null) {
            UserVO register = new UserVO();
            register.setPhone(phone);
            register.setUuid(UUID.generate());
            if (userDAO.register(register)) {
                return register;
            }else {
                return null;
            }
        }else {
            return user;
        }
    }

    @Override
    public RestaurantVO get_rest(String uuid) {
        return restaurantDAO.get(uuid);
    }

    @Override
    public List<RestaurantVO> getList_rest(int num, int loc1, int loc2) {
        return null;
    }

    @Override
    public int lineUp(WaitsVO waitsVO) {
        return 0;
    }

    @Override
    public boolean deLineUp(String uuid) {
        return false;
    }

    @Override
    public List<WaitsVO> get_waitList(String r_uuid) {
        return null;
    }
}