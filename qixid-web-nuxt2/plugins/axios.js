import {Message} from 'element-ui'

//token 前缀
let tokenPrefix = "Bearer "
export default ({$axios, store}) => {
  //请求之前 请求拦截 ，设置token
  $axios.onRequest((config) => {
    if (store.state.token != undefined && store.state.token != null) {
      config.headers["Authorization"] = tokenPrefix + store.state.token;
    }
  }, (error) => {
    return Promise.reject(error);
  });

  //请求之后
  $axios.onResponse((response) => {
    const {data} = response;
    if (data && data.code === 500) {
      // console.error("errorMsg:", data)
      // Message.error("服务器异常：" + data.code)
      Message.error(data.msg)
      return;
    }
    if (data && data.code === 404) {
      console.log("404:", data)
      Message.error("服务器异常：" + data.code)
      return;
    }
    return data;
  }, (error) => {
    return Promise.reject(error);
  });
}
