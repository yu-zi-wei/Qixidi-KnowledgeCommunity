/**
 * OSS 文件上传 API
 */
export const useOssApi = () => {
  /**
   * 上传文件到 OSS
   * @param file 文件对象
   * @param onProgress 上传进度回调
   * @returns 文件 URL
   */
  const uploadFile = async (
    file: File,
    onProgress?: (percent: number) => void
  ): Promise<string> => {
    const config = useRuntimeConfig()
    const baseURL = import.meta.server
      ? (config.apiBase as string)
      : (config.public.apiBase as string)

    const authStore = useAuthStore()

    // 构建 FormData
    const formData = new FormData()
    formData.append('file', file)

    // 使用 XMLHttpRequest 支持 multipart/form-data 和进度回调
    return new Promise<string>((resolve, reject) => {
      const xhr = new XMLHttpRequest()

      // 监听上传进度
      xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable && onProgress) {
          const percent = Math.round((e.loaded / e.total) * 100)
          onProgress(percent)
        }
      })

      // 监听完成
      xhr.addEventListener('load', () => {
        if (xhr.status === 200) {
          try {
            // 接口返回：{ code, msg, data: { url, src, ... } }
            const result = JSON.parse(xhr.responseText)
            if (result.code === 200 && result.data?.url) {
              resolve(result.data.url)
            } else {
              reject(new Error(result.msg || '上传失败'))
            }
          } catch (e) {
            reject(new Error('解析响应失败'))
          }
        } else {
          reject(new Error(`上传失败: ${xhr.status}`))
        }
      })

      // 监听错误
      xhr.addEventListener('error', () => {
        reject(new Error('网络错误'))
      })

      // 发送请求
      xhr.open('POST', `${baseURL}/system/oss/upload`)

      // 设置 Authorization header
      if (authStore.token) {
        xhr.setRequestHeader('Authorization', `Bearer ${authStore.token}`)
      }

      // 不设置 Content-Type，让浏览器自动设置 multipart/form-data boundary
      xhr.send(formData)
    })
  }

  return {
    uploadFile
  }
}
