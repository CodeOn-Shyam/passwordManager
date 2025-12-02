package org.codeon.passwordmanager.dto;

public class CredentialResponse{
    private Long id;
    private String servicename;
    private String username;
    private String note;
    private String createdAt;
    private String updatedAt;

    public CredentialResponse(Long id, String servicename, String username, String note, String createdAt, String updatedAt){
        this.id = id;
        this.servicename = servicename;
        this.username = username;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getServicename() {
        return servicename;
    }

    public String getUsername() {
        return username;
    }

    public String getNote() {
        return note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}