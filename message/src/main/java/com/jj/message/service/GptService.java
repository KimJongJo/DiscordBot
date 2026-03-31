package com.jj.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${spring.ai.openai.api-key}")
    private String gptKey;

    // Spring AI가 자동으로 생성해주는 ChatModel
    private final OpenAiChatModel chatModel;

    public String getPrompt(String weatherInfo, String messages) {

        String prompt = String.format(
                "### [시스템 역할]\n" +
                "너는 백엔드 개발자 지망생들을 위한 '면접 코치'야\n" +
                "매일 아침 날씨 정보와 어제 나눈 대화 내용을 분석해서 따뜻한 인사와 전문적인 피드백을 해줘야 해.\n\n"+
                "### [1. 오늘의 데이터]\n" +
                "- 오늘의 날씨: " + weatherInfo + "\n" +
                "- 어제 대화 내용(JSON): " + messages + "\n\n" +
                "### [2. 사용자 정보]\n" +
                "- 조옹: 면접자\n" +
                "- jonymake: 면접관\n\n" +
                "### [3. 요청 사항]\n" +
                "1. 인사말: 오늘의 날씨에 맞는 활기찬 응원 멘트로 시작해줘.\n" +
                "2. 피드백: '조옹'님이 남긴 메시지들을 분석해서 면접 답변으로서의 태도나 내용을 칭찬/보완해줘.\n" +
                "3. 오늘의 질문: 어제 대화 맥락과 관련된 인성면접 또는 백엔드 면접 질문을 3개 새로 던져줘. 만약 어제 대화내용이 없거나 비어있다면, 피드백 말고 새로운 백엔드 개발 면접 질문 3개를 던져줘\n" +
                "4. 말투: 친절하지만 전문성이 느껴지는 '선배 개발자' 톤으로 작성해줘."
        );

        return prompt;
    }

    public String askGpt(String prompt) {

        // 간단하게 호출하고 결과 문자열 받기
        return chatModel.call(prompt);

    }
}
