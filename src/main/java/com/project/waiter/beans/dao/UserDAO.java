package com.project.waiter.beans.dao;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.UserVO;
import com.project.waiter.mappers.UserMapper;
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
