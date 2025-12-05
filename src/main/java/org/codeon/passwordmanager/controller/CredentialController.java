package org.codeon.passwordmanager.controller;

import org.codeon.passwordmanager.model.Credential;
import org.codeon.passwordmanager.dto.CreateCredentialRequest;
import org.codeon.passwordmanager.dto.CredentialResponse;
import org.codeon.passwordmanager.dto.RevealPasswordRequest;
import org.codeon.passwordmanager.service.CredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public CredentialResponse create(@RequestBody CreateCredentialRequest request) {
        Credential saved = credentialService.saveCredential(
                request.getServicename(),   // matches your DTO getter
                request.getUsername(),
                request.getPassword(),
                request.getNote()
        );
        return toResponse(saved);
    }

    @GetMapping
    public List<CredentialResponse> getAllForCurrentUser() {
        return credentialService.getAllForCurrentUser()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // 🔐 New: reveal password requires vault key in body
    @PostMapping("/{id}/reveal")
    public String revealPassword(@PathVariable Long id,
                                 @RequestBody RevealPasswordRequest request) {
        return credentialService.decryptPassword(id, request.getVaultKey());
    }

    @PutMapping("/{id}")
    public CredentialResponse updateCredential(@PathVariable Long id,
                                               @RequestBody CreateCredentialRequest request) {
        Credential updated = credentialService.updateCredential(
                id,
                request.getServicename(),
                request.getUsername(),
                request.getPassword(),
                request.getNote()
        );
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCredential(@PathVariable Long id) {
        credentialService.deleteCredential(id);
    }

    private CredentialResponse toResponse(Credential c) {
        String created = c.getCreatedAt() != null ? c.getCreatedAt().format(formatter) : null;
        String updated = c.getUpdatedAt() != null ? c.getUpdatedAt().format(formatter) : null;

        return new CredentialResponse(
                c.getId(),
                c.getServicename(),   // matches your entity getter
                c.getUsername(),
                c.getNote(),
                created,
                updated
        );
    }
}
