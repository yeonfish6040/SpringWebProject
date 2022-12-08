package com.project.waiter.services;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.dao.RestaurantDAO;
import com.project.waiter.beans.dao.UserDAO;
import com.project.waiter.beans.dao.WaitsDAO;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.beans.vo.UserVO;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.util.UUID;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public List<RestaurantVO> search(int num, String name) {
        name = name.replaceAll("([/\\*?-_~'\",.:;%^&$()])", "\\\\$1");
        return restaurantDAO.search(num, name);
    }

    @Override
    public int lineUp(WaitsVO waitsVO) {
        if (waitsDAO.get(waitsVO).getR_uuid().equals(waitsVO.getR_uuid()))
            return -2;
        if (waitsDAO.register(waitsVO)) {
            return waitsDAO.get(waitsVO).getWaitNum();
        }else {
            return -1;
        }
    }

    @Override
    public boolean deLineUp(String uuid, String r_uuid) {
        WaitsVO waitsVO = new WaitsVO();

        waitsVO.setUuid(uuid);
        waitsVO.setR_uuid(r_uuid);

        return waitsDAO.remove(waitsVO);
    }

    @Override
    public List<WaitsVO> get_waitList(String r_uuid) {
        return waitsDAO.getList(r_uuid);
    }

    @Override
    public int cSts(String uuid, int type) {
        log.info(uuid);
        return restaurantDAO.cSts(uuid, type);
    }
}
