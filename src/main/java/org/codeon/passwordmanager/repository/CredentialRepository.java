package org.codeon.passwordmanager.repository;

import org.codeon.passwordmanager.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByServicenameContainingIgnoreCase(String servicename);
}
