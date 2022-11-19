package com.project.waiter.mappers;

import com.project.waiter.beans.vo.UserVO;

public interface UserMapper {
    public UserVO get(int phone);
    public int add(UserVO userVO);
}
