export {};
declare global {
  // 后端统一返回结构（ResponseAdvice 包装）
  interface R<T = any> {
    code: number;
    msg: string;
    data: T;
  }

  // 分页返回结构（TableDataInfo，Advice 放行不包装）
  interface PageResult<T = any> {
    total: number;
    rows: T[];
  }
}
