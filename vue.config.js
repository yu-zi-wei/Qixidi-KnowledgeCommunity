const {defineConfig} = require('@vue/cli-service')
const path = require('path');


function resolve(dir) {
    return path.join(__dirname, './', dir)
}

module.exports = defineConfig({
    transpileDependencies: true,
})

module.exports = {
    assetsDir: 'assets',
    // publicPath: './',
    transpileDependencies: true,
    lintOnSave: true, // 是否开启编译时是否不符合eslint提示
    configureWebpack: {
        devServer: {
            host: '0.0.0.0',  // 解决在局域网下无法访问
            port: 1029,
            proxy: {
                '/prod-api': {
                    target: 'http://127.0.0.1:9001',
                    changeOrigin: true, // 这个参数可以让target参数是域名

                }
            },
        },
    },
    pwa: {
        iconPaths: {
            favicon32: 'favicon.ico',
            favicon16: 'favicon.ico',
            appleTouchIcon: 'favicon.ico',
            maskIcon: 'favicon.ico',
            msTileImage: 'favicon.ico'
        }
    },
    // css: {
    //     loaderOptions: {
    //         // pass options to sass-loader
    //         sass: {
    //             // @/ is an alias to src/
    //             // so this assumes you have a file named `src/variables.scss`
    //             data: `
    //              @import "@/assets/css/variable.scss";
    //              @import "@/assets/css/common.scss";
    //              @import "@/assets/css/mixin.scss";
    //             `
    //         }
    //     }
    // },

    // 配置
    chainWebpack: (config) => {
// 配置别名
        config.resolve.alias
            .set('@', resolve('src'))
            .set('assets', resolve('src/assets'))
            .set('components', resolve('src/components'))
            .set('router', resolve('src/router'))
            .set('utils', resolve('src/utils'))
            .set('static', resolve('src/static'))
            .set('store', resolve('src/store'))
            .set('views', resolve('src/views'))
    }
}