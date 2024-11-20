package com.aurora.common.domain;

/**
 * @author ziwei
 * @date 2024年01月05日
 */

import lombok.Data;

import javax.websocket.Session;

@Data
public class SocketDomain {
    private Session session;

    private String uri;
}
