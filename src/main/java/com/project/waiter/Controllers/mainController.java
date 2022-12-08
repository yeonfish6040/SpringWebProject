package com.project.waiter.Controllers;

import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.WaitsVO;
import com.project.waiter.dto.RestaurantDTO;
import com.project.waiter.services.GeneralService;
import com.project.waiter.util.Messages;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String verify(@RequestParam("phone") String phone, @RequestParam(value = "chain", required = false) boolean chain, @RequestParam(value = "step", required = false) String step, @RequestParam(value = "code", required = false) String code, @CookieValue(value = "sessionId", required = false) Cookie sCookie, HttpServletRequest req) throws FileNotFoundException, NoSuchAlgorithmException {
        if (chain == true) {
            if (step.equals("1")) {
                int verifyCode = (int) Math.floor(Math.random()*1000000);
                String verifyCodeFixed = ("000000"+String.valueOf(verifyCode)).substring(("000000"+String.valueOf(verifyCode)).length()-6, ("000000"+String.valueOf(verifyCode)).length());
                Messages msg = new Messages();
                String msgContent = "인증요청이 발생하였습니다 - Waiter. \n" +
                        "인증번호는 ["+verifyCodeFixed+"] 입니다.";
                msg.send(phone, "01067820989", msgContent, "SMS");

                HttpSession session = req.getSession();
                session.setAttribute("verify_"+phone, encrypt(verifyCodeFixed));
                return "";
            } else if (step.equals("2")) {
                HttpSession session = req.getSession();
                log.info(phone);
                log.info(session.getAttribute("verify_"+phone));
                log.info(code);
                log.info(encrypt(code));
                try {
                    if (session.getAttribute("verify_"+phone).toString().equals(encrypt(code))) {
                        session.setAttribute("verify_"+phone, "");
                        session.setAttribute(sCookie.getValue().toString(), System.currentTimeMillis());
                        return "true";
                    }else {
                        return "false";
                    }
                } catch (NullPointerException e) {
                    return "false";
                }
            }
        }else {
            int verifyCode = (int) Math.floor(Math.random()*1000000);
            String verifyCodeFixed = ("000000"+String.valueOf(verifyCode)).substring(("000000"+String.valueOf(verifyCode)).length()-6, ("000000"+String.valueOf(verifyCode)).length());
            Messages msg = new Messages();
            String msgContent = "인증요청이 발생하였습니다 - Waiter. \n" +
                    "인증번호는 ["+verifyCodeFixed+"] 입니다.";
            msg.send(phone, "01067820989", msgContent, "SMS");

            return encrypt(verifyCodeFixed);
        }
        return null;
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

    @GetMapping("do/deLineUp")
    public boolean deLineUp(@RequestParam("uuid") String uuid, @RequestParam("r_uuid") String r_uuid) {
        return generalService.deLineUp(uuid, r_uuid);
    }

    @PostMapping("admin/csts")
    public void cSts(@RequestParam("uuid") String uuid, @RequestParam("type") int type) {
        generalService.cSts(uuid, type);
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
