module.exports = {
    apps: [
        {
            name: 'qixidi-web',
            script: '.output/server/index.mjs',
            env: {
                HOST: '0.0.0.0',
                PORT: 9007,
                // 后端地址（SSR + 客户端 + WebSocket 共用）
                NUXT_API_BASE: 'http://127.0.0.1:9001',
                NUXT_PUBLIC_API_BASE: 'http://127.0.0.1:9001',
                NUXT_PUBLIC_WS_BASE: 'http://127.0.0.1:9001'
            }
        }
    ]
}
