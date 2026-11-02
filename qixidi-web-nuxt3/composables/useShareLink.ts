/**
 * 分享链接统一入口：复制「标题 + 地址」到剪贴板。
 */
export const useShareLink = () => {
  const message = useMessage()

  const shareLink = async (title: string, path: string) => {
    const url = `${window.location.origin}${path}`

    try {
      await navigator.clipboard.writeText(`${title}\n${url}`)
      message.success('已复制标题与链接')
    } catch {
      message.error('复制失败')
    }
  }

  return { shareLink }
}
