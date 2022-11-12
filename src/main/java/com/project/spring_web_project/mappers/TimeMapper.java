package com.project.spring_web_project.mappers;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TimeMapper {
    //@Select("select sysdate from dual")
    public String getTime();
}
