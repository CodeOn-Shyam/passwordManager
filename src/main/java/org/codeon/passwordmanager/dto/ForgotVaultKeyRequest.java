package org.codeon.passwordmanager.dto;

public class ForgotVaultKeyRequest {

    private String username;
    private String recoveryAnswer;
    private String newVaultKey;

    public ForgotVaultKeyRequest() {}

    public ForgotVaultKeyRequest(String username, String recoveryAnswer, String newVaultKey) {
        this.username = username;
        this.recoveryAnswer = recoveryAnswer;
        this.newVaultKey = newVaultKey;
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

    public String getNewVaultKey() {
        return newVaultKey;
    }

    public void setNewVaultKey(String newVaultKey) {
        this.newVaultKey = newVaultKey;
    }
}
