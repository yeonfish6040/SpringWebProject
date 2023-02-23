package com.yeonfish.waiter.services;

import com.yeonfish.waiter.DevController.logger;
import com.yeonfish.waiter.beans.dao.RestaurantDAO;
import com.yeonfish.waiter.beans.dao.UserDAO;
import com.yeonfish.waiter.beans.dao.WaitsDAO;
import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.beans.vo.UserVO;
import com.yeonfish.waiter.beans.vo.WaitsVO;
import com.yeonfish.waiter.util.UUID;
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
    public RestaurantVO get_rest(String uuid, String phone) {
        return restaurantDAO.get(uuid, phone);
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
    public WaitsVO get_waitMe(String uuid, String r_uuid) {
        WaitsVO waitsVO = new WaitsVO();
        waitsVO.setUuid(uuid);
        waitsVO.setR_uuid(r_uuid);
        return waitsDAO.get(waitsVO);
    }

    @Override
    public int cSts(String uuid, int type) {
        log.info(uuid);
        return restaurantDAO.cSts(uuid, type);
    }
}
