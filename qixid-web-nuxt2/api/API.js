export default ({$axios}, inject) => {
  inject("API", (url, method, params, data) => {
    const fullUrl = "/api" + url;
    const options = {
      method: method,
      params: params,
      data: data
    };
    return $axios(fullUrl, options);
  });

}
