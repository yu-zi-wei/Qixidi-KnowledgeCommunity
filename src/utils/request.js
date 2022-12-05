import axios from 'axios'
import config from '../config/index' // 路径配置
// import {Msg} from './tools';

// 创建axios 实例
const service = axios.create({
    baseURL: config.baseURL, // api的base_url
    timeout: 10000 // 请求超时时间
})

// request 拦截器
service.interceptors.request.use(res => {
        // 这里可以自定义一些config 配置
        // 未设置状态码则默认成功状态
        // const code = res.data.code || 200;
        // // 获取错误信息
        // // const msg = errorCode[code] || res.data.msg || errorCode['default']
        // // 二进制数据则直接返回
        // if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        //     return res.data
        // }

        // Msg.loading()
        return res
    }, error => {
        //  这里处理一些请求出错的情况

        Promise.reject(error)
    }
)

// response 拦截器
service.interceptors.response.use(response => {
        const res = response.data
        // 这里处理一些response 正常放回时的逻辑

        return res
    }, error => {
        // 这里处理一些response 出错时的逻辑

        return Promise.reject(error)
    }
)

export default service
