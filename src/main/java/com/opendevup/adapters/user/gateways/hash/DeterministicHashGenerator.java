package com.opendevup.adapters.user.gateways.hash;

import com.opendevup.core.user.gateways.HashGenerator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeterministicHashGenerator implements HashGenerator {
    private final String hashPrefix;

    @Override
    public String generate(String input) {
        return hashPrefix.concat(":").concat(input);
    }

    @Override
    public boolean compare(String plainText, String hash) {
        String hashedInput = generate(plainText);
        return hashedInput.equals(hash);
    }
}
