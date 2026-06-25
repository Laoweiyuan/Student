package org.example.ai.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private boolean stream = false;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }


//         -d'{
//            "model": "deepseek-v4-pro",
//            "messages": [
//             {"role": "system", "content": "You are a helpful assistant."},没引入system角色
//             {"role": "user", "content": "Hello!"}
//            ],
//            "thinking": {"type": "enabled"},
//            "reasoning_effort": "high",
//            "stream": false
//           }'

    public static ChatRequest create(String userMessage) {
        return new ChatRequest(
                "deepseek-chat",
                List.of(new Message("user", userMessage)),
                false
        );
    }

    public static ChatRequest createStream(String userMessage) {
        return new ChatRequest(
                "deepseek-chat",
                List.of(new Message("user", userMessage)),
                true
        );
    }
}