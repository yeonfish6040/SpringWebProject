package com.yeonfish.waiter.beans.dao;

import com.yeonfish.waiter.DevController.logger;
import com.yeonfish.waiter.beans.vo.UserVO;
import com.yeonfish.waiter.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDAO {
    private final logger log = new logger();

    @Autowired
    private UserMapper userMapper;

    public boolean register(UserVO userVO) {
        return userMapper.add(userVO) > 0;
    }

    public UserVO get(String phone) {
        return userMapper.get(phone);
    }
}
