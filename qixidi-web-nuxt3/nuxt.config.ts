// https://nuxt.com/docs/api/configuration/nuxt-config
import tailwindcss from '@tailwindcss/vite'

// 后端 API 地址常量
const API_BASE_URL = 'http://127.0.0.1:9001'

export default defineNuxtConfig({
    compatibilityDate: '2025-01-01',
    devtools: {enabled: true},

    // 开发服务器配置
    devServer: {
        port: 9007
    },

    // 禁用原生绑定（Windows 兼容性）
    experimental: {
        inlineSSRStyles: true
    },

    modules: [
        '@bg-dev/nuxt-naiveui',
        '@pinia/nuxt',
        'pinia-plugin-persistedstate/nuxt',
        '@vueuse/nuxt',
        '@nuxtjs/color-mode'
    ],

    colorMode: {
        classSuffix: '',
        preference: 'system',
        fallback: 'light',
        storageKey: 'qixidi-color-mode'
    },

    naiveui: {
        // 主题背景色由 CSS 控制（main.css）
        // 在 html 元素设置背景，body 透明，绕过内联样式
    },

    vite: {
        plugins: [tailwindcss()],
        build: {
            rollupOptions: {
                onwarn(warning, warn) {
                    // 忽略特定警告
                    if (warning.code === 'EVAL') return
                    warn(warning)
                }
            }
        }
    },

    typescript: {
        strict: true
    },

    css: ['~/assets/css/tailwind.css', '~/assets/css/main.css'],

    runtimeConfig: {
        // 服务端环境变量（SSR 时直接请求后端）
        apiBase: process.env.NUXT_API_SERVER_URL || API_BASE_URL,
        public: {
            // 客户端环境变量（通过 devProxy 代理）
            apiBase: process.env.NUXT_PUBLIC_API_BASE || '/api',
            appName: 'qixidi-blog'
        }
    },

    ssr: true,

    app: {
        head: {
            title: '四叶集 — 在文字里，找到栖身之所',
            meta: [
                {charset: 'utf-8'},
                {name: 'viewport', content: 'width=device-width, initial-scale=1'},
                {name: 'description', content: '四叶集 — 记录想法、分享灵感、沉淀时光的个人博客'}
            ],
            link: [
                {rel: 'icon', type: 'image/svg+xml', href: '/favicon.svg'},
                {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'},
                {rel: 'preconnect', href: 'https://fonts.googleapis.com'},
                {rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: ''},
                {rel: 'preconnect', href: 'https://fonts.googleapis.com'},
                {rel: 'preconnect', href: 'https://fonts.gstatic.com', crossorigin: ''},
                {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@400;500;700&family=Noto+Serif+SC:wght@400;700&display=optional'}
            ]
        }
    },

    build: {
        transpile: ['naive-ui', 'vueuc', '@css-render/vue3-ssr']
    },

    // 混合渲染策略
    routeRules: {
        // 前台展示页面 —— SSR（SEO 友好）
        '/': {ssr: true},
        '/latest': {ssr: true},
        '/follow': {ssr: true},         // 改为 SSR，确保刷新时立即显示导航栏和侧边栏
        '/featured': {ssr: true},       // 精选页面
        '/category': {ssr: true},         // 分类列表页
        '/category/**': {ssr: true},       // 分类详情页
        '/articles': {ssr: true},
        '/articles/**': {ssr: true},
        '/categories': {ssr: true},
        '/categories/**': {ssr: true},
        '/tags': {ssr: true},
        '/tags/**': {ssr: true},
        '/about': {ssr: true},
        '/public/**': {ssr: true},           // 公开页面（专栏等）
        '/user-home/**': {ssr: true},       // 个人主页（用户信息 SSR）

        // 写作页面 —— CSR（需要登录，客户端交互）
        '/write': {ssr: false},
        '/write/**': {ssr: false},

        // 后台管理页面 —— CSR（纯客户端渲染，无需 SEO）
        '/login': {ssr: false},
        '/register': {ssr: false},
        '/admin/**': {ssr: false},
        '/user/**': {ssr: false}
    },

    nitro: {
        devProxy: {
            '/api': {
                target: process.env.NUXT_API_SERVER_URL || API_BASE_URL,
                changeOrigin: true
            }
        }
    }
})
