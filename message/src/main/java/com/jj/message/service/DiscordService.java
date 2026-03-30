package com.jj.message.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DiscordService {

    @Value("${discord.channel}")
    private String channel;





}
