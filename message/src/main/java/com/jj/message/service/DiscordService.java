package com.jj.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscordService {

    @Value("${discord.channel}")
    private String channel;

    private final JDA jda;


    public List<Message> getHistory(){
        TextChannel textChannel = jda.getTextChannelById(channel);

        if(textChannel == null){
            throw new RuntimeException("채널을 찾을 수 없습니다. ID를 확인해주세요");
        }

        // 최근 15개의 메시지를 가져오는데 필터링을 통해서 어제 메시지만 가져오기

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate yesterday = today.minusDays(1);

        return textChannel.getHistory().retrievePast(15).complete().stream()
                .filter(msg -> {
                    LocalDate msgDate = msg.getTimeCreated()
                            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                            .toLocalDate();
                    return msgDate.equals(yesterday) || msgDate.equals(today);
                })
                .collect(Collectors.toList());

    }

    public Map<String, List<String>> getGroupHistory() {
        List<Message> allMessages = getHistory();
        Collections.reverse(allMessages);


        return allMessages.stream()
                .collect(Collectors.groupingBy(
                        msg -> msg.getAuthor().getEffectiveName(),
                        Collectors.mapping(Message::getContentRaw, Collectors.toList())
                ));
    }

    public String getJsonForGpt(Map<String, List<String>> groupedMessages){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            // GPT가 읽기 편하도록 들여쓰기 설정
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Map 에서 JSON String으로 변환
            return objectMapper.writeValueAsString(groupedMessages);

        }catch(Exception e){
            System.err.println("JSON 변환 중 오류 발생 : " + e.getMessage());
            return "JSON 변환 중 오류 발생";
        }
    }


    // 디스코드에서 메시지 보내기
    public void sendMessage(String gptAnswer) {

        TextChannel textChannel = jda.getTextChannelById(channel);

        if(channel != null){
            textChannel.sendMessage(gptAnswer).queue();
        }else{
            System.out.println("채널 ID가 존재하지 않습니다.");
        }

    }
}
