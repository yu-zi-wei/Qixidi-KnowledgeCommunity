// 一些全局的config配置
const modeUrlObj = {
    // 开发环境
    'development': {
        authBaseURL: '/api'
    },
    // 生产环境
    'production': {
        authBaseURL: '/api'
    },
}
export default modeUrlObj[process.env.NODE_ENV]
