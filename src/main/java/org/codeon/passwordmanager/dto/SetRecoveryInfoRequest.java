package org.codeon.passwordmanager.dto;

public class SetRecoveryInfoRequest {

    private String question;
    private String answer;
    private String currentPassword;

    public SetRecoveryInfoRequest() {}

    public SetRecoveryInfoRequest(String question, String answer, String currentPassword) {
        this.question = question;
        this.answer = answer;
        this.currentPassword = currentPassword;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
