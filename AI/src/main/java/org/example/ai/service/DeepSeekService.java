package org.example.ai.service;

import org.example.ai.DTO.ChatResponse;
import org.example.ai.DTO.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class DeepSeekService {

    private final WebClient deepSeekWebClient;
    private final ObjectMapper objectMapper; // Jackson 已自动配置

    // 1. 非流式调用：返回完整响应
    public Mono<ChatResponse> chat(String userMessage) {
        ChatRequest request = ChatRequest.create(userMessage);//在后端创建json请求，准备发送到deepseek api

        return deepSeekWebClient.post()
                // 发送POST请求到deepseek api
                .uri("/v1/chat/completions")
                //v1代表deepseek api的v1版本
                .bodyValue(request)
                //将request对象转换为json字符串。这叫做序列化
                .retrieve()
                // 发送请求体，并返回响应体的Mono
                .bodyToMono(ChatResponse.class)
                //解析响应体，将json字符串转换为ChatResponse对象


                .doOnNext(resp -> {
                //resp是deepseek api返回的ChatResponse对象，包含deepseek的回复
                //该方法用于记录日志，不会改变deepseek的回复内容
                    if (resp.getChoices() != null && !resp.getChoices().isEmpty()) {
                        String content = resp.getChoices().get(0).getMessage().getContent();
                        //get(0).getMessage()代表获取第一个message对象，即deepseek的回复

                        //resp的格式如下
//                        {
//                            "id": "...",
//                            "choices": [
//                            {
//                                "index": 0,
//                                "message": {
//                                      "role": "assistant",
//                                      "content": "你好！我是 DeepSeek..."
//                            }
//                            }
//                            ]
//                        }


                        System.out.println("DeepSeek 回复: " + content);
                    }
                });
    }

    // 2. 流式调用 (SSE) ：逐块返回 content 字符串
    public Flux<String> chatStream(String userMessage) {
        ChatRequest request = ChatRequest.createStream(userMessage);

        return deepSeekWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)   // 接收 SSE
                .retrieve()
                .bodyToFlux(String.class)               // 原始 SSE 行
                .filter(line -> !line.isEmpty())
                .filter(line -> line.startsWith("data:"))
                .map(line -> line.substring(5).trim())  // 去掉 "data:" 前缀
                .filter(data -> !data.equals("[DONE]")) // 忽略结束标记
                .flatMap(data -> {
                    // 解析每个 JSON 块，提取 choices[0].delta.content
                    try {
                        JsonNode node = objectMapper.readTree(data);
                        JsonNode contentNode = node.at("/choices/0/delta/content");
                        if (contentNode.isMissingNode() || contentNode.isNull()) {
                            return Mono.empty();
                        }
                        return Mono.just(contentNode.asText());
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("解析流式数据失败", e));
                    }
                });
    }
}