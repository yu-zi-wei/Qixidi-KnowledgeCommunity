// https://nuxt.com/docs/api/configuration/nuxt-config
import tailwindcss from '@tailwindcss/vite'
import { readdirSync, statSync, unlinkSync } from 'node:fs'
import { join } from 'node:path'

export default defineNuxtConfig({
    compatibilityDate: '2025-01-01',
    devtools: {enabled: true},

    // 开发服务器配置
    devServer: {
        // 监听所有 IPv4 接口：127.0.0.1 / localhost / 局域网 IP 都能访问，手机连同一 WiFi 可调试
        // 不设 host 时 Node 18+ 会把 localhost 解析成 IPv6 ::1，导致 127.0.0.1 连不上
        host: '127.0.0.1',
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

    css: [
        // 字体策略：中文字体不自托管（全字重分片 ~23MB 全量进构建产物，部署包过大——用户定版），
        // 走 main.css 字体栈的系统兜底：body PingFang/雅黑、display STSong/宋体、logo 楷体；
        // 栈首保留 Noto/LXGW 等字体名，访客系统若装有同名字体则优先命中（表现更近原设计）。
        // 例外：大序号 Playfair Display italic 是纯拉丁字体，两字重 woff2 仅 ~0.1MB，自托管保留
        '@fontsource/playfair-display/400-italic.css',
        '@fontsource/playfair-display/600-italic.css',
        '~/assets/css/tailwind.css',
        '~/assets/css/main.css'
    ],

    runtimeConfig: {
        apiBase: 'http://127.0.0.1:9001',
        public: {
            apiBase: '/api',
            wsBase: 'http://127.0.0.1:9001',
            appName: 'qixidi-blog',
            siteName: '栖息地',
            piniaPluginPersistedstate: {
                cookieOptions: {
                    maxAge: 60 * 60 * 24 * 300  // 300 天（浏览器上限400天）
                }
            }
        }
    },

    ssr: true,

    app: {
        head: {
            titleTemplate: '%s - 栖息地',
            title: '首页',
            htmlAttrs: {
                lang: 'zh-CN'
            },
            meta: [
                {charset: 'utf-8'},
                {name: 'viewport', content: 'width=device-width, initial-scale=1'},
                {name: 'description', content: '栖息地 — 记录想法、分享灵感、沉淀时光的个人博客'}
            ],
            link: [
                {rel: 'icon', type: 'image/svg+xml', href: '/favicon.svg'},
                {rel: 'alternate', type: 'application/rss+xml', title: '栖息地 RSS', href: '/rss.xml'}
            ]
        }
    },

    build: {
        transpile: ['naive-ui', 'vueuc', '@css-render/vue3-ssr']
    },

    // 混合渲染策略
    routeRules: {
        // RSS 订阅 —— 纯 XML，不走 Vue 渲染
        '/rss.xml': { ssr: false },
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
        '/write': {ssr: false, prefetch: true},
        '/write/**': {ssr: false, prefetch: true},

        // 需要登录的页面 —— CSR
        '/news': {ssr: false},
        '/news/**': {ssr: false},

        // 后台管理页面 —— CSR（纯客户端渲染，无需 SEO）
        '/login': {ssr: false},
        '/register': {ssr: false},
        '/admin/**': {ssr: false},
        '/user/**': {ssr: false},
        '/settings': {ssr: false}
    },

    nitro: {
        // 生产构建时内联所有依赖，避免 Windows junction/symlink 导致 zip 复制丢文件
        // dev 模式下不能开启，否则会导致模块加载异常
        noExternals: process.env.NODE_ENV === 'production',
        devProxy: {
            '/api': {
                target: 'http://127.0.0.1:9001',
                changeOrigin: true
            }
        }
    },

    hooks: {
        // 构建产物清理：字体 css 均为 woff2 优先的多源引用（如 KaTeX 三源），
        // woff/ttf 只是 2014 年前老浏览器的回退源，全量复制纯占部署体积。
        // woff2 全现代浏览器支持；css 里的回退 url 保留无害（浏览器命中 woff2 不会发起回退请求）
        close() {
            const publicDir = join(process.cwd(), '.output', 'public')
            let freed = 0
            const walk = (dir: string) => {
                for (const name of readdirSync(dir)) {
                    const p = join(dir, name)
                    const st = statSync(p)
                    if (st.isDirectory()) walk(p)
                    else if (/\.(woff|ttf)$/.test(name)) {
                        freed += st.size
                        unlinkSync(p)
                    }
                }
            }
            try {
                walk(publicDir)
                if (freed > 0) console.log(`[build] 已清理字体 woff/ttf 回退源 ${(freed / 1048576).toFixed(1)} MB`)
            } catch {
                // dev / generate 以外的场景无 .output/public，忽略
            }
        }
    }
})
