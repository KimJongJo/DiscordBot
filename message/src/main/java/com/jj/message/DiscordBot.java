package com.jj.message;

import com.jj.message.service.DiscordService;
import com.jj.message.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component // -> 이 어노테이션이 있어야 스프링 부트가 실행 시 run()을 호출한다.
@RequiredArgsConstructor
public class DiscordBot implements CommandLineRunner {

    private final WeatherService weatherService;
    private final DiscordService discordService;



    @Override
    public void run(String... args) throws Exception {

        // 1. 오늘 날짜와 날씨
        String weatherInfo = weatherService.getWeather();
        // 2. 어제 답변에 대한 피드백
        // 3. 오늘 새로운 면접 내용




    }
}
