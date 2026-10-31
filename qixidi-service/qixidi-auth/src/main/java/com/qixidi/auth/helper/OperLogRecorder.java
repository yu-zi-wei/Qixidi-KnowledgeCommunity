package com.qixidi.auth.helper;

import com.light.core.core.domain.dto.OperLogDTO;
import com.light.core.core.service.OperLogService;
import com.light.core.enums.BusinessStatus;
import com.light.core.enums.BusinessType;
import com.light.core.enums.OperatorType;
import com.light.core.utils.ServletUtils;
import com.light.core.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 非接口场景的操作日志通用记录器（MCP tool 等）。
 * <p>
 * HTTP Controller 走 @Log 注解 + LogAspect 自动记录；MCP tool 不经 Controller，
 * 注解切面不生效，需在方法内手动调用本记录器，与 LogAspect 共用 OperLogService 底座。
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OperLogRecorder {

    private final OperLogService operLogService;

    /**
     * 记录一条操作日志（异步落库，失败只打日志，绝不影响主流程）
     *
     * @param module    模块名（同 @LogModule，建议 MCP 场景统一 "MCPxx" 前缀便于筛选）
     * @param title     操作名+对象（同 @Log.title，如"创建文章"）
     * @param type      业务类型
     * @param operName  操作人（调用方从登录态取好传入；无登录态传 null）
     * @param operParam 简短入参描述（如 "id=1, title=xxx"，内部截断 2000 字符）
     * @param e         失败异常（成功传 null）
     */
    public void record(String module, String title, BusinessType type, String operName, String operParam, Exception e) {
        try {
            OperLogDTO operLog = new OperLogDTO();
            operLog.setModule(module);
            operLog.setTitle(title);
            operLog.setBusinessType(type.ordinal());
            operLog.setOperatorType(OperatorType.OTHER.ordinal());
            operLog.setOperName(operName);
            operLog.setOperParam(StringUtils.substring(operParam, 0, 2000));
            operLog.setStatus(e == null ? BusinessStatus.SUCCESS.ordinal() : BusinessStatus.FAIL.ordinal());
            if (e != null) {
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 有请求上下文则取真实 IP/URL/方法（MCP 走 /mcp HTTP 端点同步执行时可取到），取不到置空
            HttpServletRequest request = ServletUtils.getRequest();
            if (request != null) {
                operLog.setOperIp(ServletUtils.getClientIP(request));
                operLog.setOperUrl(request.getRequestURI());
                operLog.setRequestMethod(request.getMethod());
            } else {
                operLog.setRequestMethod("MCP");
            }
            operLogService.recordOper(operLog);
        } catch (Exception ex) {
            log.error("记录操作日志失败：module={}, title={}", module, title, ex);
        }
    }

}
