package com.yeonfish.waiter.services;

import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.beans.vo.UserVO;
import com.yeonfish.waiter.beans.vo.WaitsVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GeneralServiceInter {
    public UserVO get_user(String phone);
    public RestaurantVO get_rest(String uuid);
    public RestaurantVO get_rest(String uuid, String phone);
    public List<RestaurantVO> getList_rest(int num, double loc1, double loc2);
    public List<RestaurantVO> search(int num, String name);
    public int lineUp(WaitsVO waitsVO);
    public boolean deLineUp(String uuid, String r_uuid);
    public List<WaitsVO> get_waitList(String r_uuid);
    public WaitsVO get_waitMe(String uuid, String r_uuid);
    public int cSts(String uuid, int type);
}
