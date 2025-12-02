package org.codeon.passwordmanager.service;

import org.codeon.passwordmanager.model.Credential;
import org.codeon.passwordmanager.model.User;
import org.codeon.passwordmanager.repository.CredentialRepository;
import org.codeon.passwordmanager.repository.UserRepository;
import org.codeon.passwordmanager.util.AesEncryptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    public CredentialService(CredentialRepository credentialRepository,
                             UserRepository userRepository) {
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            // 401 instead of 500
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated user");
        }

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found: " + username
                ));
    }

    public Credential saveCredential(String serviceName, String username, String password, String note) {
        User currentUser = getCurrentUser();
        try {
            String encryptedPassword = AesEncryptionUtil.encrypt(password);
            Credential credential = new Credential(serviceName, username, encryptedPassword, note, currentUser);
            return credentialRepository.save(credential);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save credential",
                    ex
            );
        }
    }

    public List<Credential> getAllForCurrentUser() {
        User currentUser = getCurrentUser();
        return credentialRepository.findAll().stream()
                .filter(c -> c.getOwner() != null && c.getOwner().getId().equals(currentUser.getId()))
                .toList();
    }
    public Credential updateCredential(Long id,String servicename,String username,String password,String note){
        User user = getCurrentUser();
        Credential credential = credentialRepository.findById(id).orElseThrow(()-> throw new ResponseStatusException(HttpStatus.NOT_FOUND,"credential not found"));
        if(credential.getOwner() == null || !credential.getOwner().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Acces Denied");
        }
        credential.setServicename(servicename);
        credential.setUsername(username);
        credential.setNote(note);
        if(password != null && !password.isBlank()){
            String encryptedPassword = AesEncryptionUtil.encrypt(password);
            credential.setEncryptedPassword(encryptedPassword);
        }
        return credentialRepository.save(credential);
    }
    public void deleteCredential(Long id){
        User currentUser = getCurrentUser();
        Credential credential = credentialReposirory.findById().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Credential not forund"));
        if(credential.getOwner()==null|| !credential.getOwner().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acces dened for the credential");
        }
        return credentialRepository.delete(credential);
    }
    public String decryptPassword(Long credentialId) {
        User currentUser = getCurrentUser();

        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Credential not found"
                ));

        if (credential.getOwner() == null ||
                !credential.getOwner().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied for this credential"
            );
        }

        try {
            return AesEncryptionUtil.decrypt(credential.getEncryptedPassword());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to decrypt password",
                    ex
            );
        }
    }
}
