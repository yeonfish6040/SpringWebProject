package com.project.waiter.mappers;

import com.project.waiter.beans.dao.WaitsDAO;
import com.project.waiter.beans.vo.WaitsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WaitsMapper {
    public List<WaitsVO> get(String r_uuid);
    public WaitsVO getMe(WaitsVO waitsVO);
    public int add(WaitsVO waitsVO);
    public int delete(WaitsVO waitsVO);
}
