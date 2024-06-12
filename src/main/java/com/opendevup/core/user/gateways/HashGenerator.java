package com.opendevup.core.user.gateways;

public interface HashGenerator {
    String generate(String input);

    boolean compare(String plainText, String hash);
}
