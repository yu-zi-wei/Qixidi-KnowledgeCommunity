package com.aurora.web.controller.payment;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.aurora.business.service.AliAayService;
import com.aurora.common.config.AlipayConfig;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * 【前台】充值接口控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/white/payment")
public class AliAayController {

    @Autowired
    private AlipayConfig alipayConfig;

    private final AliAayService aliAayService;

    /**
     * 支付
     *
     * @param response 请求体
     * @param payId    支付信息id
     */
    @GetMapping("/pay/{uid}/{payId}")
    public void pay(HttpServletResponse response, @PathVariable("uid") String uid, @PathVariable("payId") Long payId) throws Exception {
        aliAayService.pay(response, uid, payId);
    }

    /**
     * 订单支付
     */
    @GetMapping("/order/pay/{id}")
    public void payOrder(HttpServletResponse response,
                         @ApiParam("主键")
                         @NotNull(message = "主键不能为空")
                         @PathVariable("id") Long id) throws Exception {
        aliAayService.payOrder(response, id);
    }

    /**
     * 支付成功回调
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/aply", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST})
    public void callbacks(HttpServletRequest request) throws Exception {
        aliAayService.callbacks(request);
    }


    /**
     * 自定义二维码
     *
     * @return
     */
    @PostMapping("/sandboxPay")
    public String sandboxPay() {
        try {
            // 创建client，通过阿里SDK提供的client，负责调用支付宝的API
            AlipayClient alipayClient = alipayConfig.PayInitial();

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo("2022112223227"); // 自定义订单号
            model.setTotalAmount("0.01");// 支付金额
            model.setSubject("Aurora支付");// 支付的产品名称
            model.setTimeoutExpress("20m");// 支付的超时时间

            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
            alipayRequest.setReturnUrl(alipayConfig.getNotifyUrl());
            alipayRequest.setBizModel(model);

            AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);
            String qrCode = response.getQrCode();
            return qrCode;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
