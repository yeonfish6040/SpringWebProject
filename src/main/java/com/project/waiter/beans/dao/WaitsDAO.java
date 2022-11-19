package com.project.waiter.beans.dao;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.mappers.RestaurantMapper;
import com.project.waiter.mappers.WaitsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitsDAO {
    private final logger log = new logger();

    @Autowired
    private WaitsMapper waitsMapper;

    public boolean register(WaitsVO waitsVO) {
        return waitsMapper.add(waitsVO) > 0;
    }

    public List<WaitsVO> getList(String r_uuid) {
        return waitsMapper.get(r_uuid);
    }

    public boolean remove(String uuid) { return waitsMapper.delete(uuid) > 0; }
}
