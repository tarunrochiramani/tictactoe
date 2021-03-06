package com.tr.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tr.game.GameBoard;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.utils.SlackMessageAction;
import org.apache.commons.lang3.builder.Builder;

import static com.tr.utils.Constants.SLACK_ACTIONS;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_CALLBACK_ID;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_COLOR;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_COLOR_VALUE;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_FALLBACK;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_PRETEXT;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_TEXT;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_TITLE;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_TITLE_LINK;
import static com.tr.utils.Constants.SLACK_ATTACHMENT_TYPE;


public class GameBoardMediaTypeBuilder implements Builder<GameBoardMediaType> {
    private GameBoardMediaType gameBoardMediaType = new GameBoardMediaType();

    public static GameBoardMediaTypeBuilder aGameBoardMediaTypeBuilder() {
        return new GameBoardMediaTypeBuilder();
    }

    public GameBoardMediaTypeBuilder withGameBoard(GameBoard gameBoard) {
        StringBuilder stringBuilder = new StringBuilder();
        if (gameBoardMediaType.getText() != null && !gameBoardMediaType.getText().isEmpty()) {
            stringBuilder.append(gameBoardMediaType.getText());
            stringBuilder.append("\n");
        }
        stringBuilder.append(gameBoard.getBoard());
        stringBuilder.append("\n\nTurn - ");
        stringBuilder.append(gameBoard.getTurn());

        gameBoardMediaType.setText(stringBuilder.toString());
        return this;
    }

    public GameBoardMediaTypeBuilder withAttachment(String fallback, String pretext, String title, String title_link, String text) {
        List<Map<String, Object>> attachments = this.gameBoardMediaType.getAttachments();
        if (attachments == null) {
            attachments = new ArrayList<>();
            attachments.add(new HashMap<>());
            this.gameBoardMediaType.setAttachments(attachments);
        }

        Map<String, Object> attachmentProperties = attachments.get(0);
        attachmentProperties.put(SLACK_ATTACHMENT_FALLBACK, fallback);
        attachmentProperties.put(SLACK_ATTACHMENT_PRETEXT, pretext);
        attachmentProperties.put(SLACK_ATTACHMENT_TITLE, title);
        attachmentProperties.put(SLACK_ATTACHMENT_TITLE_LINK, title_link);
        attachmentProperties.put(SLACK_ATTACHMENT_TEXT, text);
        attachmentProperties.put(SLACK_ATTACHMENT_COLOR, SLACK_ATTACHMENT_COLOR_VALUE);

        return this;
    }

    public GameBoardMediaTypeBuilder withMessageButtonAttachment(String headerText, String text, String fallback, String callbackid, String color, SlackMessageAction... actions) {
        this.gameBoardMediaType.setText(headerText);
        List<Map<String, Object>> attachments = this.gameBoardMediaType.getAttachments();
        if (attachments == null) {
            attachments = new ArrayList<>();
            attachments.add(new HashMap<>());
            this.gameBoardMediaType.setAttachments(attachments);
        }

        Map<String, Object> attachmentProperties = attachments.get(0);
        attachmentProperties.put(SLACK_ATTACHMENT_TEXT, text);
        attachmentProperties.put(SLACK_ATTACHMENT_FALLBACK, fallback);
        attachmentProperties.put(SLACK_ATTACHMENT_CALLBACK_ID, callbackid);
        attachmentProperties.put(SLACK_ATTACHMENT_COLOR, color);
        attachmentProperties.put(SLACK_ATTACHMENT_TYPE, "default");

        List<SlackMessageAction> slackMessageActions = new ArrayList<>();
        attachmentProperties.put(SLACK_ACTIONS, slackMessageActions);
        for (SlackMessageAction action : actions) {
            slackMessageActions.add(action);
        }

        return this;
    }

    public GameBoardMediaTypeBuilder withResponseType(Boolean isInChannel) {
        this.gameBoardMediaType.setResponse_type("in_channel");
        return this;
    }

    public GameBoardMediaTypeBuilder withText(String text) {
        if (text != null && !text.isEmpty()) {
            this.gameBoardMediaType.setText(text);
        }
        return this;
    }

    public GameBoardMediaTypeBuilder addText(String text) {
        String originalText = gameBoardMediaType.getText();
        StringBuilder stringBuilder = new StringBuilder();
        if (originalText != null && !originalText.isEmpty()) {
            stringBuilder.append(originalText);
        }
        stringBuilder.append(text);
        this.gameBoardMediaType.setText(stringBuilder.toString());
        return this;

    }

    @Override
    public GameBoardMediaType build() {
        return gameBoardMediaType;
    }
}
