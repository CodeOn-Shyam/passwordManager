package org.codeon.passwordmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Credential{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private String userName;
    private String encryptedPassword;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Credential(){}
    public Credential(String serviceName,String userName,String encryptedPassword,String note){
        this.serviceName = serviceName;
        this.userName = userName;
        this.encryptedPassword = encryptedPassword;
        this.note = note;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
    public Long getId(){
        return id;
    }
    public String getServiceName(){
        return serviceName;
    }
    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getEncryptedPassword(){
        return encryptedPassword;
    }
    public void setEncryptedPassword(String encryptedPassword){
        this.encryptedPassword = encryptedPassword;
    }
    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
}