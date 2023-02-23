package com.yeonfish.waiter.mappers;

import com.yeonfish.waiter.beans.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserVO get(String phone);
    public int add(UserVO userVO);
}
