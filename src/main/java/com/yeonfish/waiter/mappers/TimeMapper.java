package com.yeonfish.waiter.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TimeMapper {
    //@Select("select sysdate from dual")
    public String getTime();
}
