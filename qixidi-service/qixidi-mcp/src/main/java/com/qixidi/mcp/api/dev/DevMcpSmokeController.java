package com.qixidi.mcp.api.dev;

import com.light.core.core.domain.R;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.mcp.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 【dev 专用】MCP 冒烟辅助：为当前登录用户签发真实 API key（DB 行 + Sa-Token 会话桥）。
 * 仅 dev profile 装配，生产不暴露；供本地端到端冒烟拿 key（Task 6~9 复用）。
 *
 * @author zi-wei
 */
@Profile("dev")
@RequiredArgsConstructor
@RestController
@RequestMapping("/dev/mcp")
public class DevMcpSmokeController {

    private final ApiKeyService apiKeyService;

    @PostMapping("/smoke-key")
    public R<String> createSmokeKey(@RequestBody(required = false) Map<String, String> body) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("需携带前台登录 token 调用");
        }
        String keyName = body == null ? "dev-smoke" : body.getOrDefault("keyName", "dev-smoke");
        // R.ok(String) 命中 msg 重载，key 会落进 msg；显式 ok() + setData 才是 data
        R<String> result = R.ok();
        result.setData(apiKeyService.createKey(user, keyName, 365L));
        return result;
    }
}
