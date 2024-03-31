const {defineConfig} = require('@vue/cli-service')
const path = require('path');
const webpack = require('webpack')
let timeStamp = new Date().getTime();

function resolve(dir) {
    return path.join(__dirname, './', dir)
}

module.exports = defineConfig({
    transpileDependencies: true,
})

module.exports = {
    filenameHashing: false,
    outputDir: 'dist',
    assetsDir: 'assets',
    // publicPath: './',
    transpileDependencies: true,
    lintOnSave: false, // 是否开启编译时是否不符合eslint提示   关闭Eslint代码检查
    configureWebpack: {
        devServer: {
            host: '0.0.0.0',  // 解决在局域网下无法访问
            port: 1029,
            proxy: {
                ['/api']: {
                    target: 'http://127.0.0.1:9001',
                    changeOrigin: true, // 这个参数可以让target参数是域名
                    pathRewrite: {
                        ['^' + '/api']: ''
                    }
                }
            },
        },
        output: { // 输出重构 打包编译后的js文件名称,添加时间戳.
            filename: `js/js[name].${timeStamp}.js`,
            chunkFilename: `js/chunk.[id].${timeStamp}.js`,
        },
    },
    css: {
        extract: { // 打包后css文件名称添加时间戳
            filename: `css/[name].${timeStamp}.css`,
            chunkFilename: `css/chunk.[id].${timeStamp}.css`,
        }
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
    },
}
