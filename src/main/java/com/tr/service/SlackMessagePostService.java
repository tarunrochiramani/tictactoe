package com.tr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.utils.Constants;
import com.tr.utils.SlackMessageAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private RestTemplate restTemplate = new RestTemplate();
    private HttpHeaders httpHeaders = new HttpHeaders();
    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(SlackMessagePostService.class);


    public ResponseEntity<String> sendEphermalMessageToConfirmGame(String channelId, String initiatorUserId, String secondPlayerId, String responseURL) {
        String accessToken = "xoxp-285958995910-285090349714-290032004358-d20f1fd6c10f96a405cbb1868d9afe75";
        String headerText = "You have been challenged";
        String text = "Would you like to play TicTacTao Game with - " + initiatorUserId;
        String fallbackText = "You are unable to accept the challenge";
        String callback_id = channelId + "-" + initiatorUserId + "-" + secondPlayerId;

        SlackMessageAction accept = aSlackMessageActionBuilder().withName("response").withText("Accept Challenge").withType("button").withValue("accept").build();
        SlackMessageAction reject = aSlackMessageActionBuilder().withName("response").withText("Reject").withType("button").withValue("reject").build();
        GameBoardMediaType gameBoardMediaType = aGameBoardMediaTypeBuilder().withMessageButtonAttachment(headerText, text, fallbackText, callback_id, "#3AA3E3", accept, reject).build();

        ResponseEntity<String> stringResponseEntity = null;

        try {
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.set("Authorization", "bearer " + accessToken);
            MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
            requestMap.add("channel", channelId);
            requestMap.add("text", objectMapper.writeValueAsString(gameBoardMediaType));
            requestMap.add("user", secondPlayerId);
            requestMap.add("as_user", false);

            HttpEntity<MultiValueMap> request = new HttpEntity<>(requestMap, httpHeaders);
            stringResponseEntity = restTemplate.postForEntity(Constants.SLACK_EPHEMERAL_URL, request, String.class);
        } catch (JsonProcessingException e) {
            logger.error("Unable to send message", e);
            sendMessage(aGameBoardMediaTypeBuilder().withText("Unable to send game request message").build(), responseURL);
        }

        return stringResponseEntity;
    }


    public ResponseEntity<String> sendMessage(GameBoardMediaType gameBoardMediaType, String responseUrl) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GameBoardMediaType> request = new HttpEntity<>(gameBoardMediaType, httpHeaders);

        return restTemplate.postForEntity(responseUrl, request, String.class);
    }
}
