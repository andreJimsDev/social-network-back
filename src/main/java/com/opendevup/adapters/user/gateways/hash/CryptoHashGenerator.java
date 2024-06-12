package com.opendevup.adapters.user.gateways.hash;

import com.opendevup.core.user.gateways.HashGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@Service
@RequiredArgsConstructor
public class CryptoHashGenerator implements HashGenerator {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generate(String input) {
        return passwordEncoder.encode(CharBuffer.wrap(input));
    }

    @Override
    public boolean compare(String plainText, String hash) {
        return passwordEncoder.matches(plainText, hash);
    }
}
