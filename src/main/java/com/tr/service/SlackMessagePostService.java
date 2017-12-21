package com.tr.service;

import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.utils.Constants;
import com.tr.utils.SlackMessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;
import static com.tr.builder.SlackMessageActionBuilder.aSlackMessageActionBuilder;

@Service
@Async
public class SlackMessagePostService {
    @Autowired private Environment environment;

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders httpHeaders = new HttpHeaders();
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(SlackMessagePostService.class);


    public ResponseEntity<String> sendEphemeralMessageToConfirmGame(String channelId, String initiatorUserId, String secondPlayerId) {
        String accessToken = environment.getProperty("ACCESS_TOKEN");
        String headerText = "You have been challenged";
        String text = "Would you like to play TicTacTao Game with - " + initiatorUserId;
        String fallbackText = "You are unable to accept the challenge";
        String callback_id = channelId + "-" + initiatorUserId + "-" + secondPlayerId;

        SlackMessageAction accept = aSlackMessageActionBuilder().withName("response").withText("Accept Challenge").withType("button").withValue(Constants.ACCEPT).build();
        SlackMessageAction reject = aSlackMessageActionBuilder().withName("response").withText("Reject").withType("button").withValue(Constants.REJECT).build();
        GameBoardMediaType gameBoardMediaType = aGameBoardMediaTypeBuilder().withMessageButtonAttachment(headerText, text, fallbackText, callback_id, "#3AA3E3", accept, reject).build();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("channel", channelId);
        requestMap.add("text", gameBoardMediaType.getText());
        requestMap.add("user", secondPlayerId);
        requestMap.add("as_user", Boolean.FALSE.toString());

        try {
            requestMap.add("attachments", objectMapper.writeValueAsString(gameBoardMediaType.getAttachments()));
        } catch (JsonProcessingException e) {
            logger.error("Error converting to string", e);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestMap, httpHeaders);
        return restTemplate.postForEntity(Constants.SLACK_EPHEMERAL_URL, request, String.class);
    }


    public ResponseEntity<String> sendMessage(GameBoardMediaType gameBoardMediaType, String responseUrl) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GameBoardMediaType> request = new HttpEntity<>(gameBoardMediaType, httpHeaders);

        return restTemplate.postForEntity(responseUrl, request, String.class);
    }
}
