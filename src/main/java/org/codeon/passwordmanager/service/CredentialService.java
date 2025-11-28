package org.codeon.passwordmanager.service;

import org.codeon.passwordmanager.util.AesEncryptionUtil;
import org.codeon.passwordmanager.model.Credential;
import org.codeon.passwordmanager.repository.CredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CredentialService{
    private final CredentialRepository credentialRepository;
    public CredentialService(CredentialRepository credentialRepository){
        this.credentialRepository = credentialRepository;
    }
    public Credential saveCredential(String serviceName,String userName,String password,String note){
        String encryptedPassword = AesEncryptionUtil.encrypt(password);
        Credential credential = new Credential(serviceName,userName,encryptedPassword,note);
        return credentialRepository.save(credential);
    }
    public List<Credential> getAll(){
        return credentialRepository.findAll();
    }
    public String decryptPassword(Long credentialId){
        Credential credential = credentialRepository.findById(credentialId).orElseThrow(()->new RuntimeException("Credential not found"));
        return AesEncryptionUtil.decrypt(credential.getEncryptedPassword());
    }
}