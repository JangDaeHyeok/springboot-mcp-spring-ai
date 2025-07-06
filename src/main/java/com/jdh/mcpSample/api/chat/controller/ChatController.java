package com.jdh.mcpSample.api.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String message) {
        return ResponseEntity.ok(
                chatClient.prompt()
                        .user(message)
                        .call()
                        .content()
        );
    }

}
