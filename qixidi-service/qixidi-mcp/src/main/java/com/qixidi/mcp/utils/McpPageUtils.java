package com.qixidi.mcp.utils;

/**
 * MCP 分页入参钳制：页码缺省 1；每页条数缺省 100，上限 500
 *
 * @author zi-wei
 */
public final class McpPageUtils {

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 500;

    private McpPageUtils() {
    }

    public static int pageNum(Integer pageNum) {
        return pageNum == null || pageNum < 1 ? 1 : pageNum;
    }

    public static int pageSize(Integer pageSize) {
        return pageSize == null || pageSize < 1 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
    }
}
