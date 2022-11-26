package com.project.waiter.Controllers;

import com.project.waiter.DevController.logger;
import com.project.waiter.dto.RestaurantDTO;
import com.project.waiter.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class mainController {
    private final logger log = new logger();

    @Autowired
    GeneralService service;

    @GetMapping("get/restaurant/{loc1}/{loc2}")
    public List<RestaurantDTO> getRestaurant(@PathVariable("loc1") String loc1, @PathVariable("loc2") String loc2) {
        List<RestaurantDTO> restaurantDTO = new ArrayList<RestaurantDTO>();
        service.getList_rest(20, Double.valueOf(loc1), Double.valueOf(loc2)).forEach(e -> {
            RestaurantDTO dtoTemp = new RestaurantDTO();
            dtoTemp.setUuid(e.getUuid());
            dtoTemp.setName(e.getName());
            dtoTemp.setCall(e.getCall());
            dtoTemp.setWaits(e.getWaits());
            dtoTemp.setC_wait_time(e.getC_wait_time());
            dtoTemp.setInfo(e.getInfo());
            dtoTemp.setLocation(String.valueOf(e.getLocation1())+"|"+String.valueOf(e.getLocation2()));
            dtoTemp.setPictures(e.getPictures());

            restaurantDTO.add(dtoTemp);
        });
        return restaurantDTO;
    }
}
