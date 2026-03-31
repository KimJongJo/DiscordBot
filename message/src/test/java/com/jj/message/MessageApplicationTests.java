package com.jj.message;

import com.jj.message.service.DiscordService;
import com.jj.message.service.GptService;
import com.jj.message.service.WeatherService;
import net.dv8tion.jda.api.entities.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
class MessageApplicationTests {

	@Value("${discord.webhook.url}")
	private String webhookUrl;

	@Autowired
	private WeatherService weatherService;
	@Autowired
	private DiscordService discordService;
	@Autowired
	private GptService gptService;

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

	@Test
	void 최근_20개의_디스코드_메시지_가져오기(){

		List<Message> messages = discordService.getHistory();

		for(Message msg : messages){
			System.out.println(msg.getContentRaw());
		}

	}

	@Test
	void 디스코드_메시지를_그룹화해서_가져오기(){

		Map<String, List<String>> map = discordService.getGroupHistory();

		System.out.println(map);

	}

	@Test
	void 디스코드_메시지_JSON_변환(){
		Map<String, List<String>> map = discordService.getGroupHistory();
		System.out.println(discordService.getJsonForGpt(map));
	}

	@Test
	void gptRequest(){

		Map<String, List<String>> map = discordService.getGroupHistory();

		System.out.println(gptService.askGpt(gptService.getPrompt(weatherService.getWeather(), discordService.getJsonForGpt(map))));

	}

}
