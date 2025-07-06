package com.jdh.mcpSample.api.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatFrontController {

    @GetMapping
    public String index() {
        return "chat";
    }

}
