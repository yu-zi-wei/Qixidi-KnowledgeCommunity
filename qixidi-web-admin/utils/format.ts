/**
 * 时间展示工具（admin 全 CSR，无 SSR 顾虑）
 */

/** 完整时间：yyyy-MM-dd HH:mm */
export const getFullDateTime = (time?: string | number | Date | null): string => {
  if (!time) return '';
  const d = new Date(time);
  if (Number.isNaN(d.getTime())) return String(time);
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
};
