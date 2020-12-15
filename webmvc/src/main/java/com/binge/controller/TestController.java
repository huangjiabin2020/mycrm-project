package com.binge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JiaBin Huang
 * @date 2020/10/24
 **/
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping(produces = "text/html;charset=utf-8")
    public String test() {
        return "测试OK";
    }
}
