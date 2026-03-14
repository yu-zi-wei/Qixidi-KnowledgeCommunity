import { format, formatDistanceToNow, parseISO } from 'date-fns'
import { zhCN } from 'date-fns/locale'

/**
 * 日期处理 Composable
 */
export const useDate = () => {
  const formatDate = (date: Date | string | number, formatStr: string = 'yyyy-MM-dd HH:mm:ss') => {
    const dateObj = typeof date === 'string' ? parseISO(date) : new Date(date)
    return format(dateObj, formatStr, { locale: zhCN })
  }

  const formatRelativeTime = (date: Date | string | number) => {
    const dateObj = typeof date === 'string' ? parseISO(date) : new Date(date)
    return formatDistanceToNow(dateObj, { addSuffix: true, locale: zhCN })
  }

  const formats = {
    date: (d: Date | string | number) => formatDate(d, 'yyyy-MM-dd'),
    time: (d: Date | string | number) => formatDate(d, 'HH:mm:ss'),
    datetime: (d: Date | string | number) => formatDate(d, 'yyyy-MM-dd HH:mm:ss'),
    relative: formatRelativeTime
  }

  return { formatDate, formatRelativeTime, formats }
}
