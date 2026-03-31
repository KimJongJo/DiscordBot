package com.jj.message.config;

import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DiscordConfig {

    @Value("${discord.token}")
    private String token;

    @Bean
    public JDA jda() throws InterruptedException {
        // JDA 빌드
        return JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady(); // 연결이 완전히 완료될 때까지 기다림
    }

}
