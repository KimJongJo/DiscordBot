package com.jj.message.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    @Value("${api.public-data.url}")
    private String apiUrl;

    public String getWeather(){

        // 오늘 날짜 구하기
        LocalDate now = LocalDate.now();

        // 20260330 형식 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // String으로 변환
        String todayStr = now.format(formatter);

        // api 호출
        String getUrl = apiUrl + "&base_date=" + todayStr;

        RestTemplate restTemplate = new RestTemplate();

        try{
            String response = restTemplate.getForObject(URI.create(getUrl), String.class);

            return getFilteredWeather(response);
        }catch(Exception e){
            System.err.println("기상청 API 호출 중 에러 : " + e.getMessage());
            return "날씨 정보를 가져오지 못했습니다.";
        }

    }

    public String getFilteredWeather(String jsonResponse){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            // 가장 빠른 예보 시간(첫 번째)
            String targetTime = items.get(0).get("fcstTime").asText();

            StringBuilder sb = new StringBuilder();
//            sb.append("시간 : ").append(targetTime).append("\n");

            // 리스트 돌면서 필터링
            for(JsonNode item : items){
                String fcstTime = item.get("fcstTime").asText();
                String category = item.get("category").asText();
                String value = item.get("fcstValue").asText();

                // 시간이 일치하고 내가 원하는 카테고리일 경우의 조건문
                if(fcstTime.equals(targetTime)){
                    switch(category){
                        case "T1H" : sb.append("기온: ").append(value).append("도, "); break;
                        case "PTY": sb.append("강수형태: ").append(translatePty(value)).append(", "); break;
                        case "SKY": sb.append("하늘상태: ").append(translateSky(value)).append(", "); break;
                        case "WSD": sb.append("풍속: ").append(value).append("m/s"); break;
                    }
                }
            }
            return sb.toString();

        }catch(Exception e){
            return "데이터 파싱중 에러 발생";
        }
    }

    private String translatePty(String value) {
        return switch (value) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            default -> "알 수 없음";
        };
    }

    private String translateSky(String value) {
        return switch (value) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "알 수 없음";
        };
    }
}
