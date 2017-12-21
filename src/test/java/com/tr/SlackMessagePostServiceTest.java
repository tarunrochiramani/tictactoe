package com.tr;

import com.tr.mediaType.GameBoardMediaType;
import com.tr.service.SlackMessagePostService;
import com.tr.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SlackMessagePostServiceTest {

    @Autowired
    private SlackMessagePostService slackMessagePostService;

    @Test
    public void canPostMessage() {
        GameBoardMediaType message = aGameBoardMediaTypeBuilder().withAttachment("someFallback", "somePreText", "sometitle", "somelink", "someText").build();
        slackMessagePostService.sendMessage(message, Constants.SLACK_WEBHOOK_URL);
    }

    @Test
    public void canGameConfirm() {
        ResponseEntity<String> stringResponseEntity = slackMessagePostService.sendEphemeralMessageToConfirmGame("C8EFDRDLP", "U8D2NA9M0", "U8D2NA9M0");
    }
}
