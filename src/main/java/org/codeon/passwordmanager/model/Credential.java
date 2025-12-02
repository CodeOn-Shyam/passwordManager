package org.codeon.passwordmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.codeon.passwordmanager.model.User;
@Entity
public class Credential{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String servicename;
    private String username;
    private String encryptedPassword;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User owner;

    public Credential(){}
    public Credential(String servicename,String username,String encryptedPassword,String note,User owner){
        this.servicename = servicename;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.note = note;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.owner = owner;
    }
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
    public Long getId(){
        return id;
    }
    public String getServicename(){
        return servicename;
    }
    public void setServicename(String servicename){
        this.servicename = servicename;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
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
    public User getOwner(){
        return owner;
    }
    public void setUser(User owner){
        this.owner = owner;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
}