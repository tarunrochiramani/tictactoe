package com.tr.builder;

import com.tr.utils.SlackMessageAction;
import org.apache.commons.lang3.builder.Builder;

public class SlackMessageActionBuilder implements Builder<SlackMessageAction> {
    private SlackMessageAction slackMessageAction = new SlackMessageAction();

    public static SlackMessageActionBuilder aSlackMessageActionBuilder() {
        return new SlackMessageActionBuilder();
    }

    public SlackMessageActionBuilder withName(String name) {
        this.slackMessageAction.setName(name);
        return this;
    }

    public SlackMessageActionBuilder withText(String text) {
        this.slackMessageAction.setText(text);
        return this;
    }

    public SlackMessageActionBuilder withType(String type) {
        this.slackMessageAction.setType(type);
        return this;
    }

    public SlackMessageActionBuilder withValue(String value) {
        this.slackMessageAction.setValue(value);
        return this;
    }

    @Override
    public SlackMessageAction build() {
        return slackMessageAction;
    }
}
