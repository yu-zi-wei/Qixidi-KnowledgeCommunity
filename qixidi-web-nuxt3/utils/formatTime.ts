/**
 * 时间格式化工具方法
 * @param dateStr - 日期字符串
 * @returns 格式化后的时间字符串
 */
export const formatTime = (dateStr: string): string => {
  if (!dateStr) return ''

  const date = new Date(dateStr)
  const now = new Date()
  const diffMs = now.getTime() - date.getTime()
  const diffMin = Math.floor(diffMs / 60000)
  const diffHour = Math.floor(diffMs / 3600000)
  const diffDay = Math.floor(diffMs / 86400000)

  if (diffMin < 1) return '刚刚'
  if (diffMin < 60) return `${diffMin}分钟前`
  if (diffHour < 24) return `${diffHour}小时前`
  if (diffDay < 30) return `${diffDay}天前`
  if (diffDay < 365) return `${Math.floor(diffDay / 30)}个月前`

  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

/**
 * 获取完整的日期时间字符串（用于 title 属性）
 * @param dateStr - 日期字符串
 * @returns 完整的日期时间字符串
 */
export const getFullDateTime = (dateStr: string): string => {
  if (!dateStr) return ''

  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  const s = String(date.getSeconds()).padStart(2, '0')

  return `${y}-${m}-${d} ${h}:${min}:${s}`
}

/**
 * 获取完整的日期字符串（中文格式，用于详情页等需要完整日期的场景）
 * @param dateStr - 日期字符串
 * @returns 完整的日期字符串（如：2024年3月14日）
 */
export const getFullDateCN = (dateStr: string): string => {
  if (!dateStr) return ''

  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

/**
 * 格式化日期 - 日（如：14）
 */
export const formatDay = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return String(date.getDate())
}

/**
 * 格式化日期 - 月（如：MAR）
 */
export const formatMonth = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('en-US', { month: 'short' }).toUpperCase()
}

/**
 * 格式化日期 - 年（如：2024）
 */
export const formatYear = (dateStr: string): string => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return String(date.getFullYear())
}
