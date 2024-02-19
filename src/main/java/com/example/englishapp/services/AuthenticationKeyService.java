package com.example.englishapp.services;

public interface AuthenticationKeyService {
    void saveAuthenticationKey(String encodedAuthKey);

    String getEncodedAuthKeyById(Integer id);
}
