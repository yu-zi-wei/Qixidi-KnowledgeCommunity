// 一些全局的config配置
const modeUrlObj = {
    // 生产环境
    'production': {
        baseURL: 'https://huibao.love/prod-api',
        authBaseURL: '/prod-api'
    },
    // 开发环境
    'development': {
        baseURL: 'http://127.0.0.1:8082',
        authBaseURL: '/dev-api'
    },
    // 测试环境
    'test': {
        baseURL: 'http://127.0.0.1:8082/',
        authBaseURL: ''
    }
}
export default modeUrlObj[process.env.NODE_ENV]
