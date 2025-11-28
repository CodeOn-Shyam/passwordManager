package org.codeon.passwordmanager.controller;

import org.codeon.passwordmanager.model.Credential;
import org.codeon.passwordmanager.service.CredentialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/credentials")
public class CredentialController{
    private final CredentialService credentialService;
    public CredentialController(CredentialService credentialService){
        this.credentialService = credentialService;
    }
    public static class CreateCredentialRequest{
        public String serviceName;
        public String userName;
        public String password;
        public String note;
    }
    @PostMapping
    public Credential create(@RequestBody CreateCredentialRequest request){
        return credentialService.saveCredential(
                request.serviceName,
                request.userName,
                request.password,
                request.note
        );
    }
    @GetMapping
    public List<Credential> getAll(){
        return credentialService.getAll();
    }
    @GetMapping("/{id}/password")
    public String getDecryptPassword(@PathVariable Long id){
        return credentialService.decryptPassword(id);
    }
}