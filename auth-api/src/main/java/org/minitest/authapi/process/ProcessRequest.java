package org.minitest.authapi.process;

public class ProcessRequest {
    private String text;

    public ProcessRequest() {
    }

    public ProcessRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
