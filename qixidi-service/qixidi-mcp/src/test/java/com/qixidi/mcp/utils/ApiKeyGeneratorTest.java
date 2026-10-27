package com.qixidi.mcp.utils;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * API key 生成与哈希纯逻辑测试
 */
class ApiKeyGeneratorTest {

    @Test
    void generate_以qxmcp前缀开头且总长54() {
        String key = ApiKeyGenerator.generate();
        assertTrue(key.startsWith("qxmcp_"), "应以 qxmcp_ 开头: " + key);
        assertEquals(54, key.length(), "总长应为 6 前缀 + 48 随机 = 54: " + key);
    }

    @Test
    void generate_不含URL或头传输危险字符() {
        for (int i = 0; i < 100; i++) {
            String key = ApiKeyGenerator.generate();
            assertFalse(key.contains("/"), "不得含 /: " + key);
            assertFalse(key.contains("+"), "不得含 +: " + key);
            assertFalse(key.contains("="), "不得含 =: " + key);
        }
    }

    @Test
    void generate_100次无重复() {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            keys.add(ApiKeyGenerator.generate());
        }
        assertEquals(100, keys.size(), "100 次生成必须全部唯一");
    }

    @Test
    void sha256_已知值校验() {
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            ApiKeyGenerator.sha256("abc"));
    }

    @Test
    void sha256_输出为64位小写hex() {
        String hash = ApiKeyGenerator.sha256("qxmcp_test");
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]{64}"), "应为 64 位小写 hex: " + hash);
    }
}
