package com.tr.utils;

public class Constants {
    public static int EMPTY = -1;
    public static String DRAW = "Draw - No winner";
    public static final int SIZE = 3;

    public static final String SLACK_REQUEST_PARAM_CHANNEL_ID = "channel_id";
    public static final String SLACK_REQUEST_PARAM_USER_ID = "user_id";
    public static final String SLACK_REQUEST_PARAM_TEXT = "text";
    public static final String SLACK_REQUEST_PARAM_RESPONSE_URL = "response_url";
    public static final String SLACK_REQUEST_PARAM_TOKEN = "token";

    public static final String SLACK_ATTACHMENT_FALLBACK = "fallback";
    public static final String SLACK_ATTACHMENT_PRETEXT = "pretext";
    public static final String SLACK_ATTACHMENT_TITLE = "title";
    public static final String SLACK_ATTACHMENT_TITLE_LINK = "title_link";
    public static final String SLACK_ATTACHMENT_TEXT = "text";
    public static final String SLACK_ATTACHMENT_COLOR = "color";
    public static final String SLACK_ATTACHMENT_COLOR_VALUE = "#F35A00";
    public static final String SLACK_ATTACHMENT_TYPE = "attachment_type";
    public static final String SLACK_ATTACHMENT_CALLBACK_ID = "callback_id";
    public static final String SLACK_ACTIONS = "actions";

    public static final String PAYLOAD = "payload";


    public static final String SLACK_EPHEMERAL_URL = "https://slack.com/api/chat.postEphemeral";
    public static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/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";

    public static final String ACCEPT = "accept";
    public static final String REJECT = "reject";

    public static final String SLACK_INTERACTIVE_URI = "/rest/slack/interactive";
    public static final String REQUEST_GAME_URI = "/rest/requestGame";
    public static final String CURRENT_GAME_URI = "/rest/currentGame";
    public static final String PLAY_MOVE_URI = "/rest/playMove";
    public static final String ABORT_GAME_URI = "/rest/abortGame";

    public static final String APPLICATION_PROP_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String APPLICATION_PROP_SLASH_CMD_TOKEN = "SLASH_CMD_TOKEN";

}
