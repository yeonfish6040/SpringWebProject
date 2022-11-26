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

import java.util.ArrayList;
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
    public UserVO get_user(String phone) {
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
    public List<RestaurantVO> getList_rest(int num, double loc1, double loc2) {
        return restaurantDAO.getList(num, loc1, loc2);
    }

    @Override
    public int lineUp(WaitsVO waitsVO) {
        List<Integer> standing = new ArrayList<Integer>();
        if (waitsDAO.register(waitsVO)) {
            waitsDAO.getList(waitsVO.getR_uuid()).forEach(e -> {
                if (e.getUuid() == waitsVO.getUuid()) {
                    standing.add(waitsVO.getWaitNum());
                }
            });
        }
        return standing.get(0);
    }

    @Override
    public boolean deLineUp(String uuid) {
        return waitsDAO.remove(uuid);
    }

    @Override
    public List<WaitsVO> get_waitList(String r_uuid) {
        return waitsDAO.getList(r_uuid);
    }
}
