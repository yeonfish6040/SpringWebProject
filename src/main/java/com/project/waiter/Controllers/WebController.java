package com.project.waiter.Controllers;

import com.project.waiter.DevController.logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

    private final logger log = new logger();

    @GetMapping("")
    public String main() {
        return "main";
    }
}
