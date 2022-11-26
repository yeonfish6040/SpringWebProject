package com.project.waiter.beans.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestaurantVO {
    private String uuid;
    private String name;
    private float location1;
    private float location2;
    private String call;
    private String info;
    private int waits;
    private int c_wait_time;
    private String pictures;

//    @Override
//    public String toString() {
//        return "{'uuid': '"+uuid+"', " +
//                "'name': '"+name+"', " +
//                "'location1': '"+location1+"', " +
//                "'location2': '"+location2+"', " +
//                "'call': '"+call+"', " +
//                "'info': '"+info+"', " +
//                "'waits': '"+waits+"', " +
//                "'c_wait_time': '"+c_wait_time+"', " +
//                "'pictures': '"+pictures+"'}";
//    }
}