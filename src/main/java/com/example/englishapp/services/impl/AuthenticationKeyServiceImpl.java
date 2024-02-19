package com.example.englishapp.services.impl;

import com.example.englishapp.models.AuthenticationKey;
import com.example.englishapp.repositories.AuthenticationKeyRepository;
import com.example.englishapp.services.AuthenticationKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationKeyServiceImpl implements AuthenticationKeyService {

    private final AuthenticationKeyRepository authenticationKeyRepository;

    @Override
    public void saveAuthenticationKey(String encodedAuthKey) {
        AuthenticationKey entity = new AuthenticationKey();
        entity.setEncodedAuthKey(encodedAuthKey);
        authenticationKeyRepository.save(entity);
    }

    @Override
    public String getEncodedAuthKeyById(Integer id) {
        return authenticationKeyRepository.findById(id)
                .map(AuthenticationKey::getEncodedAuthKey)
                .map((encodedKey) -> new String(Base64.getDecoder().decode(encodedKey)))
                .orElse(null);
    }
}
