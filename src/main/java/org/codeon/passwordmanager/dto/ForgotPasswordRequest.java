package org.codeon.passwordmanager.dto;

public class ForgotPasswordRequest {

    private String username;
    private String recoveryAnswer;
    private String newPassword;

    public ForgotPasswordRequest() {}

    public ForgotPasswordRequest(String username, String recoveryAnswer, String newPassword) {
        this.username = username;
        this.recoveryAnswer = recoveryAnswer;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecoveryAnswer() {
        return recoveryAnswer;
    }

    public void setRecoveryAnswer(String recoveryAnswer) {
        this.recoveryAnswer = recoveryAnswer;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
