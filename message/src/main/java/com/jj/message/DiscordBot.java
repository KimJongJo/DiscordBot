package com.jj.message;

import com.jj.message.service.DiscordService;
import com.jj.message.service.GptService;
import com.jj.message.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component // -> 이 어노테이션이 있어야 스프링 부트가 실행 시 run()을 호출한다.
@RequiredArgsConstructor
public class DiscordBot implements CommandLineRunner {

    private final WeatherService weatherService;
    private final DiscordService discordService;
    private final GptService gptService;



    @Override
    public void run(String... args) throws Exception {

        // 1. 오늘 날짜와 날씨 (기상청 api 쓰기)
        String weatherInfo = weatherService.getWeather();

        // 2. 어제 답변에 대한 피드백 (디스코드에서 채팅 내역을 가져와야함)
        // 2-1. 봇이 채팅했던 문제, 내가 입력한 채팅을 통해서 피드백 받기
        String messages = discordService.getJsonForGpt(discordService.getGroupHistory());

        // 3. gpt에 보낼 prompt 가져오기
        String prompt = gptService.getPrompt(weatherInfo, messages);

        // 4. 가져온 prompt로 gpt에 요청
        String gptAnswer = gptService.askGpt(prompt);

        // 5. gpt의 답변을 디스코드로 전송
        discordService.sendMessage(gptAnswer);



    }
}
