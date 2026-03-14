package com.qixidi.business.domain.bo.alipay;

import lombok.Data;

@Data
public class AliPayBo {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
