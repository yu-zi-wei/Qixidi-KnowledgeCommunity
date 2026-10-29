/**
 * 时间展示工具（admin 全 CSR，无 SSR 顾虑）
 * 相对时间 + 完整时间（title/tooltip 用），语义对齐门户 utils/formatTime.ts
 */

/** 完整时间：yyyy-MM-dd HH:mm */
export const getFullDateTime = (time?: string | number | Date | null): string => {
  if (!time) return '';
  const d = new Date(time);
  if (Number.isNaN(d.getTime())) return String(time);
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
};

/** 相对时间：刚刚 / N 分钟前 / N 小时前 / N 天前 / 超过 30 天回落日期 */
export const formatTime = (time?: string | number | Date | null): string => {
  if (!time) return '—';
  const d = new Date(time);
  if (Number.isNaN(d.getTime())) return String(time);

  const diff = Date.now() - d.getTime();
  const minute = 60_000;
  const hour = 60 * minute;
  const day = 24 * hour;

  if (diff < minute) return '刚刚';
  if (diff < hour) return `${Math.floor(diff / minute)} 分钟前`;
  if (diff < day) return `${Math.floor(diff / hour)} 小时前`;
  if (diff < 30 * day) return `${Math.floor(diff / day)} 天前`;
  return getFullDateTime(d).split(' ')[0];
};
