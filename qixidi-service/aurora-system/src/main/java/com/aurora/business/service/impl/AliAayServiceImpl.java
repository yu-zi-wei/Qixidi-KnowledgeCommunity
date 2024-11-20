package com.aurora.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.aurora.business.domain.entity.user.UserOrder;
import com.aurora.business.domain.vo.configure.ToRechargeInfoVo;
import com.aurora.business.domain.vo.user.UserOrderVo;
import com.aurora.business.mapper.configure.ToRechargeInfoMapper;
import com.aurora.business.mapper.user.UserOrderMapper;
import com.aurora.business.service.AliAayService;
import com.aurora.common.config.AlipayConfig;
import com.aurora.common.enums.PaymentStateEnums;
import com.aurora.common.utils.DateUtils;
import com.aurora.common.utils.RandomNumberUtils;
import com.aurora.system.mapper.TripartiteUserMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AliAayServiceImpl implements AliAayService {
    @Autowired
    private AlipayConfig alipayConfig;
    private final TripartiteUserMapper tripartiteUserMapper;
    private final ToRechargeInfoMapper toRechargeInfoMapper;
    private final UserOrderMapper userOrderMapper;


    /**
     * 发起支付
     *
     * @param response
     * @param uid
     * @param payId
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(HttpServletResponse response, String uid, Long payId) throws Exception {
        try {
//            生成订单
            UserOrder userOrder = generateOrder(uid, payId);
//            生成 支付页面
            generatePaymentPage(response, userOrder);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("支付申请失败：{}", e.getMessage());
            throw new Exception("支付申请异常!");
        }
    }


    /**
     * 生成 支付页面
     *
     * @param response
     * @param userOrder 订单对象
     * @throws Exception
     */
    public void generatePaymentPage(HttpServletResponse response, UserOrder userOrder) throws Exception {
        // 创建client，通过阿里SDK提供的client，负责调用支付宝的API
        AlipayClient alipayClient = alipayConfig.PayInitial();
        // 创建request，并设置request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());// 设置异步地址
        request.setReturnUrl(alipayConfig.getReturnUrl());// 设置支付成功后的跳转地址

        JSONObject jsonObject = new JSONObject();
        log.info("商户订单号:out_trade_no：{}", userOrder.getId());
        jsonObject.put("out_trade_no", userOrder.getId());// 商户订单号
        jsonObject.put("total_amount", userOrder.getRechargeMoney());// 商品价格
        jsonObject.put("subject", userOrder.getCommodityName());// 商品标题
        jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");//固定写法
        request.setBizContent(jsonObject.toString());
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = alipayClient.pageExecute(request).getBody();
        // 设置响应结果，将返回的内容写出到浏览器
        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        response.getWriter().write(form);// 直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * // 接收支付宝返回的请求参数
     * Map<String, String[]> map = request.getParameterMap();
     * String out_trade_no = map.get("out_trade_no")[0];//订单id
     * String trade_status = map.get("trade_status")[0];//交易状态
     * String subject = map.get("subject")[0];//主题
     * String buyer_pay_amount = map.get("buyer_pay_amount")[0];//买方支付价格
     * String invoice_amount = map.get("invoice_amount")[0];//发票金额
     * String seller_id = map.get("seller_id")[0];//卖家id
     * String buyer_id = map.get("buyer_id")[0];//购买者id
     * String auth_app_id = map.get("auth_app_id")[0];//身份验证应用程序id
     * String notify_id = map.get("notify_id")[0];//通知id
     * String app_id = map.get("app_id")[0];//appId
     * String sign_type = map.get("sign_type")[0];//签名类型
     *
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void callbacks(HttpServletRequest request) throws Exception {
        // 接收支付宝返回的请求参数
        Map<String, String[]> map = request.getParameterMap();
        String out_trade_no = map.get("out_trade_no")[0];//订单id
        String trade_status = map.get("trade_status")[0];//交易状态
        log.info("out_trade_no:{}，trade_status：{}", out_trade_no, trade_status);
        if (trade_status.equals("TRADE_SUCCESS")) {
            //支付成功之后的业务处理
            UserOrderVo userOrderVo = userOrderMapper.selectVoById(Long.valueOf(out_trade_no));
//            数据校验
            paymentInformationVerification(userOrderVo);
            userOrderMapper.update(null, new UpdateWrapper<UserOrder>()
                .set("payment_state", PaymentStateEnums.PAYMENT_COMPLETED.getCode())
                .set("payment_time", new Date())
                .set("payment_method", "支付宝")//当前系统只对接了支付宝支付
                .eq("id", out_trade_no));
            tripartiteUserMapper.updateACurrency(userOrderVo.getUid(), userOrderVo.getRechargeCurrency());
        } else {
            //支付失败后的业务处理
            userOrderMapper.update(null, new UpdateWrapper<UserOrder>()
                .set("payment_state", PaymentStateEnums.PAYMENT_FAILED.getCode()).set("payment_time",
                    new Date()).eq("id", out_trade_no));
            throw new Exception("交易异常！");
        }
    }

    @Override
    public void payOrder(HttpServletResponse response, Long id) throws Exception {
        UserOrderVo userOrderVo = userOrderMapper.selectVoById(id);
//        数据校验
        paymentInformationVerification(userOrderVo);
//        生成支付页面
        UserOrder userOrder = BeanUtil.toBean(userOrderVo, UserOrder.class);
        generatePaymentPage(response, userOrder);
    }

    /**
     * 支付信息校验
     *
     * @param userOrderVo
     */
    public void paymentInformationVerification(UserOrderVo userOrderVo) throws Exception {
        if (userOrderVo.getPaymentState().equals(PaymentStateEnums.PAYMENT_COMPLETED.getCode()) || userOrderVo.getPaymentState().equals(PaymentStateEnums.PAYMENT_FAILED.getCode())) {
            throw new Exception("该订单已完成！");
        }
        if (userOrderVo.getPaymentState().equals(PaymentStateEnums.TO_EXPIRED.getCode())) {
            throw new Exception("该订单已过期！");
        }
    }

    /**
     * 生成订单
     *
     * @param payId 支付信息id
     */
    public UserOrder generateOrder(String uid, Long payId) {
        ToRechargeInfoVo toRechargeInfoVo = toRechargeInfoMapper.selectVoById(payId);
        toRechargeInfoVo.setFinalMoney(toRechargeInfoVo.getMoney());
        boolean meetDiscounts = toRechargeInfoVo.getIsDiscount() == 1 && DateUtils.isEffectiveDate(new Date(), toRechargeInfoVo.getStartTime(), toRechargeInfoVo.getEndTime());
        if (meetDiscounts) {
            toRechargeInfoVo.setFinalMoney(
                BigDecimal.valueOf(toRechargeInfoVo.getMoney().intValue())
                    .multiply((BigDecimal.valueOf(toRechargeInfoVo.getDiscount())).divide(BigDecimal.valueOf(10))));
            toRechargeInfoVo.setIsFinish(false);
        }
//        会员打折
//        if (toRechargeInfoVo.getMemberDiscount() > 0) {
//            toRechargeInfoVo.setMemberMoney(BigDecimal.valueOf(toRechargeInfoVo.getMoney().intValue())
//                .multiply((BigDecimal.valueOf(toRechargeInfoVo.getMemberDiscount())).divide(BigDecimal.valueOf(10))));
//        }

        UserOrder userOrder = new UserOrder()
            .setId(RandomNumberUtils.Snowflakes())
            .setUid(uid)
            .setRechargeInfoId(payId)
            .setRechargeMoney(toRechargeInfoVo.getFinalMoney())
            .setRechargeCurrency(toRechargeInfoVo.getCurrency())
            .setDiscount(meetDiscounts ? toRechargeInfoVo.getDiscount() : 0)
            .setCreateTime(new Date())
            .setCommodityName(toRechargeInfoVo.getCommodityName())
            .setPaymentState(1);
        userOrderMapper.insert(userOrder);
        return userOrder;
    }

}
