package com.aurora.business.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付 Service
 */
public interface AliAayService {
    void pay(HttpServletResponse response, String uid, Long payId) throws Exception;

    void callbacks(HttpServletRequest request) throws Exception;

    void payOrder(HttpServletResponse response, Long id) throws Exception;
}
