package com.jj.message.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GptService {

    @Value("${spring.ai.openai.api-key}")
    private String gptKey;

}
