package com.light.core.utils.word;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

/**
 * 敏感词组封装类
 */
@Data
public class WordNode extends HashedMap<Character, WordNode> {

    /**
     * 最后一个字符
     */
    private boolean isLast;

    public WordNode() {
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public WordNode(boolean isLast) {
        this.isLast = isLast;
    }

    public WordNode addChild(Character key, WordNode wordNode) {
        return put(key, wordNode);
    }

    public WordNode removeChild(Character key) {
        return remove(key);
    }

    public WordNode getChild(Character key) {
        return get(key);
    }

    public int childSize() {
        return size();
    }

    @Override
    public String toString() {
        return "WordNode{" + "isLast=" + isLast + "," + super.toString() + '}';
    }
}
