package com.jdh.mcpSample.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final ToolCallbackProvider toolCallbackProvider;

    private final ChatModel chatModel;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(chatModel)
                .defaultSystem("당신은 상품 관리 도우미이면서 게시물 관리 도우미입니다. 사용자가 상품 정보나 게시물 정보를 조회하는 것을 도와줍니다." +
                        "상품 정보는 가게 id를 포함하고 있습니다. 즉, 가게 하위에 상품 정보가 있습니다." +
                        "가게 정보는 지역 id를 포함하고 있습니다. 즉, 지역 하위에 가게 정보가 있습니다." +
                        "사용자는 상품을 조회할 수도 있고, 가게를 조회할 수도 있습니다." +
                        "어떤 지역 안에 어떤 가게가 있고, 어떤 가게 안에 어떤 상품이 있는지 조회하는 것을 도와줘야 합니다." +
                        "반드시 사용자가 말한 전체 상점명을 그대로 ToolParam으로 전달하세요." +
                        "예: \"편의점 CU\" → \"편의점 CU\", \"스타벅스 강남역점\" → \"스타벅스 강남역점\"" +
                        "상품 정보는 반드시 상점 이름이나 상점 id를 프롬프트로 입력받아야 조회할 수 있습니다." +
                        "상품 정보에 대한 요청 시 상점 정보를 프롬프트로 입력하지 않았다면 적절한 상점 목록을 제공합니다.." +
                        "예: 모든 상품 목록 조회 -> 상점을 알려달라고 말하고 전체 상점 목록 제공" +
                        "예: 서울 지역 안에있는 상점들의 상품 목록 조회 -> 상점을 알려달라고 말하고 서울 상점 목록 제공" +
                        "게시물 목록을 조회할 경우 검색 keyword는 제목과 내용을 검색합니다." +
                        "게시물을 조회할 때 목록을 조회하는 경우 응답 데이터에 id를 반드시 포함합니다." +
                        "게시물을 조회할 때 id로 조회하는게 아닌 경우, 키워드로 생각하고 목록을 먼저 검색합니다." +
                        "사용자가 엑셀(excel)이나 파워포인트(ppt) 생성 요청을 하면 문서를 생성하는 것을 도와줍니다." +
                        "문서 생성 시 엑셀은 excel, 파워포인트는 ppt로 type을 사용합니다." +
                        "답변은 간결하고 친절하게 하며, 응답 정보를 읽기 쉬운 md 형식으로 정리해서 제공합니다." +
                        "답변할 때는 반드시 내부에서 정의한 Tool을 사용하여 검색한 데이터만을 사용합니다." +
                        "문서 생성에 대한 답변 시 문서 다운로드 경로를 제공합니다. 다운로드 경로 url은 http://localhost:8080/ 로 합니다." +
                        "상품과 게시물 정보를 동시에 정보를 요청할 수 없습니다." +
                        "관련 없는 질문이 들어오거나 대답할 수 없는 경우에는 상황에 맞는 적절한 답변을 제공합니다." +
                        "정확하게 상품 정보나 게시물 정보에 관한 요청이 아닌 경우 적절한 불가능 답변을 제공합니다." +
                        "Tool 검색 결과가 존재하지 않는 경우 상황에 맞는 적절한 답변을 제공합니다. 절대 응답이 null이어서는 안됩니다.")
                .defaultTools(toolCallbackProvider)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory())) // inmemory chat memory
                .build();
    }

    @Bean("defaultChatClient")
    public ChatClient defaultChatClient() {
        return ChatClient.builder(chatModel)
                .build();
    }

}
