package org.codeon.passwordmanager.controller;

import org.codeon.passwordmanager.model.Credential;
import org.codeon.passwordmanager.dto.CreateCredentialRequest;
import org.codeon.passwordmanager.service.CredentialService;
import org.springframework.web.bind.annotation.*;
import org.codeon.passwordmanager.dto.CredentialResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RestController
@RequestMapping("/api/credentials")
public class CredentialController{
    private final CredentialService credentialService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public CredentialController(CredentialService credentialService){
        this.credentialService = credentialService;
    }
    @PostMapping
    public CredentialResponse create(@RequestBody CreateCredentialRequest request){
        Credential saved = credentialService.saveCredential(
                request.getServicename(),
                request.getUsername(),
                request.getPassword(),
                request.getNote()
        );
        return toResponse(saved);
    }
    @GetMapping
    public List<CredentialResponse> getAllForCurrentUser(){
        return credentialService.getAllForCurrentUser().stream().map(this::toResponse).toList();
    }
    @GetMapping("/{id}/password")
    public String getDecryptPassword(@PathVariable Long id){
        return credentialService.decryptPassword(id);
    }
    private CredentialResponse toResponse(Credential c){
        String created = c.getCreatedAt() != null ? c.getCreatedAt().format(formatter) : null;
        String updated = c.getUpdatedAt() != null ? c.getUpdatedAt().format(formatter) : null;

        return new CredentialResponse(
                c.getId(),
                c.getServicename(),
                c.getUsername(),
                c.getNote(),
                created,
                updated
        );
    }
    @PutMapping("/{id}")
    public CredentialResponse updateCredential(@PathVariable Long id,@RequestBody CreateCredentialRequest request){
        Credential updated = credentialService.updateCredntial(
                id,
                request.getServicename(),
                request.getUsername(),
                request.getPassword(),
                request.getNote(),
        )
                return toResponse(updated);
    }
    @DeleteMapping("/{id}")
    public
}