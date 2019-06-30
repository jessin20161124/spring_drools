package com.jessin.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jessin on 17-7-22.
 */
@Controller
public class HelloController {


    @RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String sayHello(@RequestParam Integer one){
        return "input : " + one;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String  error() {
        return "访问出错了";
    }
}
