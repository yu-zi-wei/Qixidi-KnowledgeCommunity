module.exports = {
    apps: [
        {
            name: 'qixidi-web',
            script: '.output/server/index.mjs',
            env: {
                HOST: '0.0.0.0',
                PORT: 9007,
                // SSR 用：服务器本机回环访问后端
                // ⚠️ 必须使用 127.0.0.1，不能用公网 IP（后端 9001 端口只监听 127.0.0.1，公网访问会返回空响应）
                NUXT_API_BASE: 'http://127.0.0.1:9001',
                // 客户端用：通过 nginx 反向代理（生产环境用绝对 URL，本地 dev 用 nuxt.config.ts 默认值 '/api'）
                NUXT_PUBLIC_API_BASE: 'https://qixidi.top/api',
                NUXT_PUBLIC_WS_BASE: 'https://qixidi.top/api'
            }
        }
    ]
}
