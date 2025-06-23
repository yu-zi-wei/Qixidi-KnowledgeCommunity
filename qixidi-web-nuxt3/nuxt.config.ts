// https://nuxt.com/docs/api/configuration/nuxt-config
import AutoImport from 'unplugin-auto-import/vite';
import Components from 'unplugin-vue-components/vite';
import {NaiveUiResolver} from 'unplugin-vue-components/resolvers';


export default defineNuxtConfig({
    compatibilityDate: '2024-11-01',
    devtools: {enabled: true},
    build: {
        transpile: [
            'vueuc',
            'naive-ui',
            '@css-render/vue3-ssr'
        ]
    },
    modules: ['nuxtjs-naive-ui', '@pinia/nuxt'],
    css: ['~/assets/css/nh.scss'],
    $production: {
        // @ts-ignore
        routeRules: {
            '/**': {isr: true}
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
    }, app: {
        // 自定义页面头段内容
        head: {
            title: 'Nuxt3+Vue3+Ts'
        }
    }
});
