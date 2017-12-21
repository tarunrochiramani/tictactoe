package com.tr;

import com.tr.builder.GameBoardMediaTypeBuilder;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.service.SlackMessagePostService;
import com.tr.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@RunWith(JUnit4.class)
public class SlackMessagePostServiceTest {

    private SlackMessagePostService slackMessagePostService = new SlackMessagePostService();

    @Test
    public void canPostMessage() {
        GameBoardMediaType message = aGameBoardMediaTypeBuilder().withAttachment("someFallback", "somePreText", "sometitle", "somelink", "someText").build();
        slackMessagePostService.sendMessage(message, Constants.SLACK_WEBHOOK_URL);
    }
}
