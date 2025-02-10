package com.light.core.utils.word;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 词库上下文
 */
public class WordContext {

    private static volatile WordContext wordContext = null;
    private static volatile WordNode rootWordNode = null;

    private WordContext() {
    }

    public static WordContext getWordContext(List<String> blackList) {
        if (wordContext == null) {
            synchronized (WordContext.class) {
                if (wordContext == null) {
                    wordContext = new WordContext();
                    rootWordNode = new WordNode();
                    // 将敏感词库加入到HashMap中
                    addWord(blackList);
                }
            }
        }
        return wordContext;
    }


    public WordNode getWordNode() {
        return rootWordNode;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = { isEnd = 0 国 = {<br>
     * isEnd = 1 人 = {isEnd = 0 民 = {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd = 1 }
     * } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = { isEnd = 0 旗 = { isEnd = 1 } } } }
     */
    public static void addWord(Iterable<String> wordList) {
        WordNode wordNode;
        // 迭代keyWordSet
        for (String key : wordList) {
            wordNode = rootWordNode;
            for (int i = 0; i < key.length(); i++) {
                // 转换成char型
                char keyChar = key.charAt(i);
                // 获取
                WordNode childNode = wordNode.getChild(keyChar);
                // 如果存在该key，直接赋值
                if (childNode == null) {
                    boolean isLast = i == key.length() - 1;
                    childNode = new WordNode(isLast);
                    // 不是最后一个
                    wordNode.addChild(keyChar, childNode);
                }
                wordNode = childNode;
            }
        }
    }

    public void removeWord(Iterable<String> wordList) {
        WordNode wordNode;
        for (String key : wordList) {
            List<WordNode> cacheList = new ArrayList<>();
            wordNode = rootWordNode;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                WordNode childNode = wordNode.getChild(keyChar);
                if (childNode == null) {
                    return;
                }
                wordNode = childNode;
                cacheList.add(wordNode);

                if (i == key.length() - 1) {
                    char[] keys = key.toCharArray();
                    boolean cleanable = false;
                    char lastChar = 0;
                    for (int j = cacheList.size() - 1; j >= 0; j--) {
                        WordNode wordNode1 = cacheList.get(j);
                        if (j == cacheList.size() - 1) {
                            wordNode1.setLast(false);
                            if (wordNode1.childSize() == 0) {
                                cleanable = true;
                                continue;
                            }
                        }
                        if (cleanable) {
                            if (wordNode1.isLast()) {
                                cleanable = false;
                            }
                            wordNode1.removeChild(lastChar);
                        }
                        lastChar = keys[j];
                    }

                    if (cleanable) {
                        rootWordNode.removeChild(lastChar);
                    }
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    private Set<String> readWordFile(String file) throws Exception {
        Set<String> set;
        // 字符编码
        String encoding = "UTF-8";
        try (InputStreamReader read = new InputStreamReader(
                this.getClass().getResourceAsStream(file), encoding)) {
            set = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt;
            // 读取文件，将文件内容放入到set中
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(txt);
            }
        }
        // 关闭文件流
        return set;
    }
}
