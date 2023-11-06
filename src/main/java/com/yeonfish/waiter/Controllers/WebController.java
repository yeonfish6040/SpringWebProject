package com.yeonfish.waiter.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeonfish.waiter.DevController.logger;
import com.yeonfish.waiter.beans.vo.RestaurantVO;
import com.yeonfish.waiter.services.GeneralService;
import com.yeonfish.waiter.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebController {

    private final logger log = new logger();

    @Autowired
    GeneralService generalService;

    @GetMapping("view")
    public String map(@RequestParam("uuid") String uuid, @RequestParam("loc1") double loc1, @RequestParam("loc2") double loc2, @CookieValue(value = "sessionId") Cookie cookie, Model model, HttpServletRequest req) throws JsonProcessingException {

        List<RestaurantVO> rests = generalService.getList_rest(30, loc1, loc2);
        RestaurantVO rest = generalService.get_rest(uuid);

        log.info(rests);
        log.info(rest);

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("list", mapper.writeValueAsString(rests));
        model.addAttribute("rest", mapper.writeValueAsString(rest));
        model.addAttribute("uuid", rest.getUuid());
        model.addAttribute("loc1", loc1);
        model.addAttribute("loc2", loc2);
        model.addAttribute("msg", "");

        HttpSession session = req.getSession();
        if (session.getAttribute("verified" + cookie.getValue()) == null)
            session.setAttribute("verified" + cookie.getValue(), false);


        return "view";
    }

    @GetMapping("search")
    public String search(@RequestParam("q") String query, Model model) throws JsonProcessingException {

        StringBuffer queryString = new StringBuffer();

        for (String i : query.split(" ")) {
            queryString.append(i+"|");
        }

        List<RestaurantVO> rests = generalService.search(20, queryString.toString().substring(0, queryString.toString().lastIndexOf("|")));
        RestaurantVO rest;
        String msg;
        if (rests.size() == 0) {
            msg = "검색 결과가 없습니다.";
            rest = new RestaurantVO();
        }else {
            rest = rests.get(0);
            msg = String.valueOf(rests.size())+"개의 검색결과를 찾았습니다.";
        }

        log.info(rests);
        log.info(rest);

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("list", mapper.writeValueAsString(rests));
        model.addAttribute("rest", mapper.writeValueAsString(rest));
        model.addAttribute("uuid", rest.getUuid());
        model.addAttribute("loc1", rest.getLocation1());
        model.addAttribute("loc2", rest.getLocation2());
        model.addAttribute("msg", msg);

        return "view";
    }

    @GetMapping("admin")
    public String admin(@RequestParam("uuid") String uuid, @RequestParam("phone") String phone, @RequestParam("code") String code, @RequestParam("zeroCount") int zeroCount, @CookieValue(value = "sessionId") Cookie sCookie, Model model, HttpServletRequest req) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("code", code);
        model.addAttribute("phone", phone);
        model.addAttribute("zeroCount", zeroCount);
        model.addAttribute("uuid", uuid);
        model.addAttribute("rest", generalService.get_rest(uuid, phone));
        model.addAttribute("restStr", mapper.writeValueAsString(generalService.get_rest(uuid)));
        if (req.getSession().getAttribute(sCookie.getValue()+"_"+uuid) != null) {
            if ((System.currentTimeMillis()-Long.valueOf(req.getSession().getAttribute(sCookie.getValue()+"_"+uuid).toString())) < 21600*1000) {
                model.addAttribute("pass", true);
            }else {
                model.addAttribute("pass", false);
            }
        }
        return "admin";
    }

    @GetMapping("")
    public String main(@CookieValue(value = "sessionId", required = false) Cookie cookie, HttpServletRequest req, HttpServletResponse res) {
        addCookie(cookie, req, res);

        return "main";
    }

    private boolean addCookie(Cookie c, HttpServletRequest req, HttpServletResponse res) {
        if (c == null) {
            String uuid = UUID.generate();
            res.addCookie(new Cookie("sessionId", uuid));
            req.getSession().setAttribute("verified" + uuid, false);
            return false;
        }else {
            return true;
        }
    }
}
