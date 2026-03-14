package com.light.mybatisPlus.p6Spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.FormattedLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zi-wei
 * @create 2025/2/10 15:45
 */
public class ColoredSqlLogger extends FormattedLogger {

    private static final Logger logger = LoggerFactory.getLogger("p6Spy");
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[32m";// 设置 SQL 颜色为蓝色


    @Override
    public void logText(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(ANSI_BLUE);
        sb.append(text);
        sb.append(ANSI_RESET); // 重置颜色
        logger.info(sb.toString());
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return true;
    }

    @Override
    public void logException(Exception e) {
        logger.error("", e);
    }
}
