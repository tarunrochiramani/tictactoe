package com.tr.mediaType;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GameBoardMediaType {
    private String response_type;
    private String text;
    private List<Map<String, String>> attachments;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Map<String, String>> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Map<String, String>> attachments) {
        this.attachments = attachments;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }
}
