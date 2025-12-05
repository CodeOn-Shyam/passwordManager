package org.codeon.passwordmanager.dto;

public class SetVaultKeyRequest {

    private String vaultKey;
    private String currentPassword; // to verify identity

    public SetVaultKeyRequest() {}

    public SetVaultKeyRequest(String vaultKey, String currentPassword) {
        this.vaultKey = vaultKey;
        this.currentPassword = currentPassword;
    }

    public String getVaultKey() {
        return vaultKey;
    }

    public void setVaultKey(String vaultKey) {
        this.vaultKey = vaultKey;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
