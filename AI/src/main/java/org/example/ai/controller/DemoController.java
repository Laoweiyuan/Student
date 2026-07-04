package org.example.ai.controller;

import org.example.ai.DTO.ChatResponse;
import org.example.ai.service.DeepSeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/deepseek")
@RequiredArgsConstructor
public class DemoController {

    private final DeepSeekService deepSeekService;

    // 普通请求：POST /api/deepseek/chat
    @PostMapping("/chat")
    public Mono<ChatResponse> chat(@RequestBody Map<String, String> request) {//Mono一般用于返回单个值，异步请求
        String message = request.get("message");//request只是一个变量名，不是请求体
        return deepSeekService.chat(message);
    }

    // 流式请求：POST /api/deepseek/chat/stream
    // 注意：返回 text/event-stream
    @PostMapping(value = "/chat/stream", produces = "text/event-stream")
    public Flux<String> chatStream(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return deepSeekService.chatStream(message);
    }
}