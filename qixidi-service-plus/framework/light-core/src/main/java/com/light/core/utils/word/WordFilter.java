package com.light.core.utils.word;

import org.assertj.core.util.Lists;

import java.util.*;

/**
 * 敏感词过滤器
 */
public class WordFilter {

    private WordNode wordNode;

    private WordNode wordFilter = null;

    /**
     * 构造函数
     */
    public WordFilter(WordContext context) {
        this.wordNode = context.getWordNode();
    }

    public WordFilter() {
        this.wordFilter = this.wordNode;
    }

    public WordFilter(List<String> blackWords) {
        WordContext context = WordContext.getWordContext(blackWords);
        this.wordNode = context.getWordNode();
    }


    /**
     * 替换敏感词
     *
     * @param text 输入文本
     */
    public String replace(final String text) {
        return replace(text, 0, '*');
    }

    /**
     * 替换敏感词
     *
     * @param text   输入文本
     * @param symbol 替换符号
     */
    public String replace(final String text, final char symbol) {
        return replace(text, 0, symbol);
    }

    /**
     * 替换敏感词
     *
     * @param text   输入文本
     * @param skip   文本距离
     * @param symbol 替换符号
     */
    public String replace(final String text, final int skip, final char symbol) {
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            WordIndex wordIndex = getWordIndex(charset, i, skip);
            if (wordIndex.isBlackWord()) {
                for (int index : wordIndex.getIndex()) {
                    charset[index] = symbol;
                }
            }
        }
        return new String(charset);
    }

    /**
     * 是否包含敏感词
     *
     * @param text 输入文本
     */
    public boolean include(final String text) {
        return include(text, 0);
    }

    /**
     * 是否包含敏感词
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public boolean include(final String text, final int skip) {
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            WordIndex wordIndex = getWordIndex(charset, i, skip);
            if (wordIndex.isBlackWord()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取敏感词数量
     *
     * @param text 输入文本
     */
    public int wordCount(final String text) {
        return wordCount(text, 0);
    }

    /**
     * 获取敏感词数量
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public int wordCount(final String text, final int skip) {
        int count = 0;
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            WordIndex wordIndex = getWordIndex(charset, i, skip);
            if (wordIndex.isBlackWord()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取敏感词列表
     *
     * @param text 输入文本
     */
    public Set<String> wordList(final String text) {
        return wordList(text, 0);
    }

    /**
     * 获取敏感词列表
     *
     * @param text 输入文本
     * @param skip 文本距离
     */
    public Set<String> wordList(final String text, final int skip) {
        Set<String> wordList = new HashSet<>();
        char[] charset = text.toCharArray();
        for (int i = 0; i < charset.length; i++) {
            WordIndex wordIndex = getWordIndex(charset, i, skip);
            if (wordIndex.isBlackWord()) {
                wordList.add(wordIndex.getWord());
            }
        }
        return wordList;
    }

    /**
     * 获取标记索引
     *
     * @param charset 输入文本
     * @param begin   检测起始
     * @param skip    文本距离
     */
    private WordIndex getWordIndex(final char[] charset, final int begin, final int skip) {
        WordIndex wordIndex = new WordIndex();
        WordNode current = wordNode;
        boolean flag = false;
        int count = 0;
        List<Integer> index = new ArrayList<>();
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = begin; i < charset.length; i++) {
            char word = charset[i];
            WordNode childNode = current.getChild(word);
            // 匹配的字不在黑名单里
            if (count > skip || (i == begin && Objects.isNull(childNode))) {
                break;
            }
            if (Objects.nonNull(childNode)) {
                current = childNode;
                count = 0;
                index.add(i);
                stringBuffer.append(word);
            } else {
                count++;
                if (flag && count > skip) {
                    break;
                }
            }
            flag = current.isLast();
        }

        wordIndex.setBlackWord(flag);
        wordIndex.setIndex(index);
        wordIndex.setWord(stringBuffer.toString());
        return wordIndex;
    }

    public static void main(String[] args) {

        WordFilter filter = new WordFilter(Lists.newArrayList("妓女", "妓女槽", "菊花洞", "包夜"));

        String text = "利于妓女槽行业菊花上游发展的包夜政策逐渐发布";
        System.out.println(filter.wordList(text));
        System.out.println(filter.wordCount(text));
        System.out.println(filter.replace(text));

        //context.removeWord(Collections.singletonList("妓女"));
        //System.out.println(filter.wordList(text));
    }
}
