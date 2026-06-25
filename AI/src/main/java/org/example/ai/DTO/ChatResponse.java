package org.example.ai.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ChatResponse {
    private String id;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
        // 流式会用到 delta，先忽略，我们用 message
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}