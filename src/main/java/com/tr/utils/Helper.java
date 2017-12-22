package com.tr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Helper {
    public List<String> tokenizeEscapedUser(String text) {
        return getSubStrings(text, "<@[a-zA-Z0-9]+\\|[a-zA-Z0-9\\._]+>");
    }

    public List<String> tokenizeEscapedChannel(String text) {
        return getSubStrings(text, "<#[a-zA-Z0-9]+\\|[a-zA-Z0-9\\._]+>");
    }

    public String getUserId(String input) {
        return input.substring(input.indexOf("<@") + 2, input.indexOf('|'));
    }

    public String getChannelID(String input) {
        return input.substring(input.indexOf("<#") + 2, input.indexOf('|'));
    }


    private List<String> getSubStrings(String text, String patternText) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternText);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;

    }
}
