package com.yeonfish.waiter.beans.dao;

import com.yeonfish.waiter.DevController.logger;
import com.yeonfish.waiter.beans.vo.WaitsVO;
import com.yeonfish.waiter.mappers.WaitsMapper;
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

    public WaitsVO get(WaitsVO waitsVO) {
        WaitsVO me = waitsMapper.getMe(waitsVO);
        if (me == null){
            WaitsVO nullVO = new WaitsVO();
            nullVO.setUuid("NULL");
            nullVO.setR_uuid("NULL");
            nullVO.setWaitNum(0);
            return nullVO;
        }else {
            return me;
        }
    }

    public boolean remove(WaitsVO waitsVO) { return waitsMapper.delete(waitsVO) > 0; }
}
