package com.tr.service;

import com.tr.mediaType.GameBoardMediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Async
public class SlackMessagePostService {

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders httpHeaders = new HttpHeaders();

    private static Logger logger = LoggerFactory.getLogger(SlackMessagePostService.class);


    public void sendMessage(GameBoardMediaType gameBoardMediaType, String responseUrl) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GameBoardMediaType> request = new HttpEntity<>(gameBoardMediaType, httpHeaders);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(responseUrl, request, String.class);

        logger.info(stringResponseEntity.getStatusCode().toString());

    }
}
