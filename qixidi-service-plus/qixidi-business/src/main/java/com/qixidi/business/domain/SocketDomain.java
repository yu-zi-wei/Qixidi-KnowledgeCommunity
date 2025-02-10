package com.qixidi.business.domain;

/**
 * @author ziwei
 * @date 2024年01月05日
 */

import jakarta.websocket.Session;
import lombok.Data;

@Data
public class SocketDomain {
    private Session session;

    private String uri;
}
