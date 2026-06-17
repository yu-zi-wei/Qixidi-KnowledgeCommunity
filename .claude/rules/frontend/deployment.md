# 部署配置规范

qixidi-web-nuxt3 生产环境部署的强制规范。

---

## 🔴 SSR API 调用必须用 127.0.0.1（绝对规则）

**绝对规则**：SSR 阶段调用后端 API 必须使用 `http://127.0.0.1:9001`，**绝对不能用公网 IP**。

### 为什么

| 维度 | 127.0.0.1（推荐） | 公网 IP（禁止） |
|------|-------------------|-----------------|
| 网络延迟 | 最低（loopback） | 较高（可能绕外网） |
| 稳定性 | 永远可达 | 依赖防火墙/路由，可能突然失效 |
| 安全 | 不会被拦截 | 可能被各种安全策略拦截 |
| 性能 | 走 loopback，不经网卡 | 走网卡/网关 |

### 踩过的坑（2026-06-17）

**现象**：刷新 `https://qixidi.top/` 时，导航栏和二级导航栏不显示，PM2 无错误日志，dev 模式正常。

**根因**：服务器 `ecosystem.config.cjs` 中 `NUXT_API_BASE: 'http://116.198.203.132:9001'`（公网 IP）。SSR 阶段从服务器发起请求到公网 IP 的 9001 端口，TCP 能连上但返回 `Content-Length: 0`（空响应），被 `useAsyncData` 的 try/catch 吞掉，最终 layout 渲染空数据。

**关键诊断**：
```bash
# 服务器本机 curl
curl -v 'http://127.0.0.1:9001/...'      # ✅ 返回完整 JSON
curl -v 'http://116.198.203.132:9001/...' # ❌ Content-Length: 0（即使后端监听 :::9001）

# 浏览器（外部）
http://116.198.203.132:9001/...           # ✅ 正常（可能走了反代）
```

**关键诊断方法**（在 SSR handler 中打印 baseURL 和完整 result）：
```javascript
const config = useRuntimeConfig()
console.log('SSR baseURL:', import.meta.server ? config.apiBase : config.public.apiBase)
console.log('result:', { type: typeof result, json: JSON.stringify(result).substring(0, 200) })
// 如果 result 是字符串 "" 或非预期格式，说明 SSR 请求被拦截或访问了错误地址
```

**修复**：把 `NUXT_API_BASE` 改为 `http://127.0.0.1:9001`，重启 PM2。

---

## 🔴 PM2 重启必须 delete + start

修改 `ecosystem.config.cjs` 的 `env` 后，必须：

```bash
pm2 delete qixidi-web
pm2 start /path/to/ecosystem.config.cjs
pm2 save
```

**绝对禁止** `pm2 restart`：restart 可能复用旧进程的环境变量，新的 env 配置不生效。

---

## 🔴 客户端 baseURL 必须走 nginx 反代

**禁止**：让浏览器直接访问后端端口（`http://ip:9001` 或 `http://公网IP:9001`）。

**必须**：通过 nginx 反向代理 `/api/` 路径。

`nginx.conf` 示例：
```nginx
location /api/ {
    proxy_pass http://127.0.0.1:9001/;  # 末尾 / 用于去掉 /api 前缀
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

**原因**：
- 浏览器访问用户公网 IP（`http://116.198.203.132:9001`）会失败（9001 可能被防火墙拦截）
- HTTPS 站点访问 HTTP 接口会被浏览器拦截（Mixed Content）
- 通过 nginx 反代可以统一域名、统一 HTTPS、隐藏后端端口

---

## 完整配置示例

`ecosystem.config.cjs`（生产环境）：
```javascript
module.exports = {
    apps: [{
        name: 'qixidi-web',
        script: '.output/server/index.mjs',
        env: {
            HOST: '0.0.0.0',
            PORT: 9007,
            // ✅ SSR 用：本机回环（绝对不能改公网 IP）
            NUXT_API_BASE: 'http://127.0.0.1:9001',
            // ✅ 客户端用：通过 nginx 反向代理（绝对 URL 或相对路径 /api 均可）
            NUXT_PUBLIC_API_BASE: 'https://qixidi.top/api',
            NUXT_PUBLIC_WS_BASE: 'https://qixidi.top/api'
        }
    }]
}
```

`nuxt.config.ts`（默认值，dev 模式生效）：
```javascript
runtimeConfig: {
    apiBase: 'http://127.0.0.1:9001',  // SSR 用
    public: {
        apiBase: '/api',  // 客户端用（dev 模式通过 devProxy 代理）
        // ...
    }
}

nitro: {
    devProxy: {
        '/api': {
            target: 'http://127.0.0.1:9001',
            changeOrigin: true
        }
    }
}
```

---

## 调试 SSR 数据问题

当 SSR 数据异常（接口正常但 SSR 拿不到）时，按此顺序排查：

1. **在 SSR handler 中打印 baseURL**：
   ```javascript
   const config = useRuntimeConfig()
   console.log('SSR baseURL:', config.apiBase)
   ```

2. **打印完整 result（不只是 result.xxx）**：
   ```javascript
   const result = await api.getData()
   console.log('result:', { 
     type: typeof result,
     keys: result && typeof result === 'object' ? Object.keys(result) : null,
     json: JSON.stringify(result).substring(0, 500)
   })
   ```

3. **服务器本机 curl 测试**：
   ```bash
   curl -v 'http://127.0.0.1:9001/your/api'
   curl -v 'http://公网IP:9001/your/api'  # 如果这个失败，SSR 必然失败
   ```

4. **检查 payload**：
   ```javascript
   // 浏览器控制台
   console.log('payload.data keys:', Object.keys(window.__NUXT__?.payload?.data || {}))
   ```

如果 payload.data 是空对象 `{}`，说明 SSR 阶段所有 useAsyncData 都没拿到数据，最可能是 SSR baseURL 配置错误。

---

## 检查清单

部署前/排查 SSR 问题时确认：

- [ ] `ecosystem.config.cjs` 中 `NUXT_API_BASE` 是 `http://127.0.0.1:9001`？
- [ ] 修改 env 后用了 `pm2 delete + start`（不是 restart）？
- [ ] nginx 配置了 `/api/` 反向代理到 `127.0.0.1:9001`？
- [ ] 客户端 baseURL 是 `/api` 或 `https://域名/api`（不是 `http://公网IP:9001`）？
- [ ] 服务器本机 `curl http://127.0.0.1:9001` 能返回正常 JSON？

---

**核心原则**：SSR 走 loopback，客户端走 nginx，**永远不要让任何代码直接访问后端公网端口**。
