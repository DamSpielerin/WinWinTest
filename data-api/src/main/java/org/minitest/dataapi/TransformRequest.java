package org.minitest.dataapi;

public class TransformRequest {
    private String text;

    public TransformRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
