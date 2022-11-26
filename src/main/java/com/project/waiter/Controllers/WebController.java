package com.project.waiter.Controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.waiter.DevController.logger;
import com.project.waiter.beans.vo.RestaurantVO;
import com.project.waiter.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class WebController {

    private final logger log = new logger();

    @Autowired
    GeneralService generalService;

    @GetMapping("view")
    public String map(@RequestParam("uuid") String uuid, @RequestParam("loc1") double loc1, @RequestParam("loc2") double loc2, Model model) throws JsonProcessingException {


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

        return "view";
    }

    @GetMapping("")
    public String main() {
        return "main";
    }
}
