// https://nuxt.com/docs/api/configuration/nuxt-config
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers';

export default defineNuxtConfig({
    compatibilityDate: '2024-11-01',
    devtools: { enabled: true },
    runtimeConfig: {
        public: {
            // 客户端经 devProxy（dev）/ nginx 反代（生产）访问后端
            apiBase: '/api'
        }
    },
    build: {
        transpile: [
            'vueuc',
            'naive-ui',
            '@css-render/vue3-ssr'
        ]
    },
    postcss: {
        plugins: {
            '@tailwindcss/postcss': {},
            autoprefixer: {}
        }
    },
    modules: ['nuxtjs-naive-ui', '@pinia/nuxt'],
    css: ['~/assets/css/tailwind.scss', '~/assets/css/admin-table.scss'],
    // 管理后台全 CSR：无 SEO 需求，免 SSR 水合与 Naive UI SSR 处理
    routeRules: {
        '/**': { ssr: false }
    },
    nitro: {
        devProxy: {
            '/api': {
                // 验证新后端代码时用 NUXT_DEV_API_TARGET 指向 9002 验证实例，避免动 9001 常驻实例
                target: process.env.NUXT_DEV_API_TARGET || 'http://127.0.0.1:9001',
                changeOrigin: true
            }
        }
    },
    vite: {
        ssr: {
            noExternal: ['naive-ui', 'vueuc', '@css-render/vue3-ssr']
        },
        css: {
            preprocessorOptions: {
                scss: {
                    additionalData: '@use "@/assets/css/constant.scss" as *;'
                }
            }
        },
        plugins: [
            AutoImport({
                imports: [
                    {
                        'naive-ui': ['useDialog', 'useMessage', 'useNotification', 'useLoadingBar']
                    },
                    'vue',
                    'vue-router'
                ]
            }),
            Components({
                resolvers: [NaiveUiResolver()]
            })
        ]
    },
    app: {
        head: {
            title: '栖息地 · 管理后台'
        }
    }
});
