package com.jj.message;

import com.jj.message.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
class MessageApplicationTests {

	@Value("${discord.webhook.url}")
	private String webhookUrl;

	@Autowired
	private WeatherService weatherService;

	@Test
	void 디스코드_웹훅_전송_테스트(){
		RestTemplate restTemplate = new RestTemplate();

		Map<String, String> body = Map.of("content", "[김종조] JUnit 테스트 성공");

		try{

			restTemplate.postForEntity(webhookUrl, body, String.class);
			System.out.println("디스코드 확인");

		}catch(Exception e){
			e.printStackTrace();
			System.err.println("실패 : " + e.getMessage());
		}

	}

	@Test
	void 날씨정보_조회_테스트(){

		System.out.println(weatherService.getWeather());
	}

}
