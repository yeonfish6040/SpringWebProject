package com.project.waiter.Controllers;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.dto.RestaurantDTO;
import com.project.waiter.services.GeneralService;
import com.project.waiter.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class mainController {
    private final logger log = new logger();

    @Autowired
    GeneralService generalService;

    @GetMapping("get/restaurant/{loc1}/{loc2}")
    public List<RestaurantDTO> getRestaurant(@PathVariable("loc1") String loc1, @PathVariable("loc2") String loc2) {
        List<RestaurantDTO> restaurantDTO = new ArrayList<RestaurantDTO>();
        generalService.getList_rest(20, Double.valueOf(loc1), Double.valueOf(loc2)).forEach(e -> {
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

    @GetMapping("get/verify")
    public String verify(@RequestParam("phone") String phone) throws FileNotFoundException, NoSuchAlgorithmException {
        int verifyCode = (int) Math.floor(Math.random()*1000000);
        Messages msg = new Messages();
        msg.send(phone, "01067820989", String.valueOf(verifyCode));
        return encrypt(String.valueOf(verifyCode));
    }

    @GetMapping("get/user")
    public String getUser(@RequestParam("phone") String phone) {
        return generalService.get_user(phone).getUuid();
    }

    @GetMapping("do/book")
    public String book(@RequestParam("r_uuid") String r_uuid, @RequestParam("uuid") String uuid) {
        WaitsVO waitsVO = new WaitsVO();
        waitsVO.setR_uuid(r_uuid);
        waitsVO.setUuid(uuid);
        int num = generalService.lineUp(waitsVO);
        return String.valueOf(num);
    }

    public String encrypt(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
