package org.codeon.passwordmanager.dto;

public class CreateCredentialRequest{
    private String servicename;
    private String username;
    private String password;
    private String note;

    public CreateCredentialRequest(){
    }
    public CreateCredentialRequest(String servicename,String username,String password,String note){
        this.servicename = servicename;
        this.username = username;
        this.password = password;
        this.note = note;
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
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getNote(){
        return note;
    }
    public void setNote(String note){
        this.note = note;
    }
}