package com.yeonfish.waiter.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeonfish.waiter.DevController.logger;
import com.yeonfish.waiter.beans.vo.WaitsVO;
import com.yeonfish.waiter.dto.RestaurantDTO;
import com.yeonfish.waiter.services.GeneralService;
import com.yeonfish.waiter.util.HttpUtil;
import com.yeonfish.waiter.util.Messages;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public String verify(
            @RequestParam("phone") String phone, @RequestParam(value = "chain", required = false) boolean chain,
            @RequestParam(value = "step", required = false) String step, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "uuid", required = false) String uuid,
            @CookieValue(value = "sessionId", required = false) Cookie sCookie, HttpServletRequest req) throws IOException, NoSuchAlgorithmException {
        if (chain == true) {
            if (step.equals("1")) {
                int verifyCode = (int) Math.floor(Math.random()*1000000);
                String verifyCodeFixed = ("000000"+String.valueOf(verifyCode)).substring(("000000"+String.valueOf(verifyCode)).length()-6, ("000000"+String.valueOf(verifyCode)).length());
                Messages msg = new Messages(new ClassPathResource("secret/message.apiKey").getInputStream());
                String msgContent = "인증요청이 발생하였습니다 - Waiter. \n" +
                        "인증번호는 ["+verifyCodeFixed+"] 입니다.";
                msg.send(phone, "01067820989", msgContent, "SMS");

                HttpSession session = req.getSession();
                session.setAttribute("verify_"+phone, encrypt(verifyCodeFixed));
                log.info(verifyCodeFixed);
                return "";
            } else if (step.equals("2")) {
                HttpSession session = req.getSession();
                try {
                    if (session.getAttribute("verify_"+phone).toString().equals(encrypt(code))) {
                        session.setAttribute("verify_"+phone, "");
                        session.setAttribute(sCookie.getValue().toString()+"_"+uuid, System.currentTimeMillis());
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
            Messages msg = new Messages(new ClassPathResource("secret/message.apiKey").getInputStream());
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

    @GetMapping("get/bookers")
    public String getBookers(@RequestParam("uuid") String uuid) throws JsonProcessingException {
        StringBuffer result = new StringBuffer();
        result.append("[");
        generalService.get_waitList(uuid).forEach(e -> {
            result.append("{\"uuid\": \""+e.getUuid()+"\", \"phone\": \""+generalService.get_user(e.getUuid()).getPhone()+"\", \"waitNum\": \""+e.getWaitNum()+"\"},");
        });
        return result.length() == 1 ? "[]" : result.substring(0, result.length()-1)+"]";
    }

    @GetMapping("get/verified")
    public boolean isVerified(@CookieValue(value = "sessionId") Cookie cookie, HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (boolean) (session.getAttribute("verified" + cookie.getValue()));
    }

    @GetMapping("do/book")
    public String book(@CookieValue(value = "sessionId") Cookie cookie, WaitsVO waitsVO, HttpServletRequest req) {
        log.info(waitsVO);
        int num = generalService.lineUp(waitsVO);

        HttpSession session = req.getSession();
        session.setAttribute("verified" + cookie.getValue(), true);

        return String.valueOf(num);
    }


    @GetMapping("do/deLineUp")
    public boolean deLineUp(@RequestParam("uuid") String uuid, @RequestParam("r_uuid") String r_uuid) {
        return generalService.deLineUp(uuid, r_uuid);
    }

    @GetMapping("do/sendNotification")
    public String send(@RequestParam("uuid") String uuid, @RequestParam("r_uuid") String r_uuid, @RequestParam("data") String data) throws IOException, ParseException {
        WaitsVO user = generalService.get_waitMe(uuid, r_uuid);
        Resource resource = new ClassPathResource("secret/private_push");
        Scanner secret = new Scanner(resource.getInputStream());
        HttpUtil sendNotification = new HttpUtil();
        JSONObject param = new JSONObject();
        JSONObject subscription = new JSONObject();
        JSONObject keys = new JSONObject();
        JSONObject applicationKeys = new JSONObject();
        keys.put("p256dh", user.getP256dh());
        keys.put("auth", user.getAuth());
        subscription.put("keys", keys);
        subscription.put("endpoint", user.getEndpoint());
        subscription.put("expirationTime", "null");
        applicationKeys.put("public", secret.next());
        applicationKeys.put("private", secret.next());
        param.put("data", data);
        param.put("subscription", subscription);
        param.put("applicationKeys", applicationKeys);
        log.info(param.toString());

        Messages msg = new Messages(new ClassPathResource("secret/message.apiKey").getInputStream());
        msg.send(generalService.get_user(user.getUuid()).getPhone(), "01067820989", data, "SMS");
        return sendNotification.callApi("https://web-push-codelab.glitch.me/api/send-push-msg", param);
    }

    @PostMapping("admin/csts")
    public void cSts(@RequestParam("uuid") String uuid, @RequestParam("type") int type) {
        generalService.cSts(uuid, type);
    }

    @GetMapping("test")
    public String test() {
        return "test";
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
