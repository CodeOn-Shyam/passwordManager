package org.codeon.passwordmanager.dto;

public class RevealPasswordRequest {

    private String vaultKey;

    public RevealPasswordRequest() {}

    public RevealPasswordRequest(String vaultKey) {
        this.vaultKey = vaultKey;
    }

    public String getVaultKey() {
        return vaultKey;
    }

    public void setVaultKey(String vaultKey) {
        this.vaultKey = vaultKey;
    }
}
