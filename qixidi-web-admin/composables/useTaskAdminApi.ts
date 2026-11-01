/**
 * 系统任务 API（SystemTaskController，/system/task）
 * 任务清单只读，手动执行走各任务端点（GET）
 */

/** 系统任务配置行（SystemTaskConfigVo） */
export interface SystemTaskRow {
  taskName?: string;
  /** 任务执行时间（cron 描述） */
  taskExecutionTime?: string;
  /** 任务执行次数 */
  taskExecutionSum?: string;
  /** 手动执行接口地址（相对路径） */
  taskUrl?: string;
}

export const useTaskAdminApi = () => {
  const api = useApi();

  return {
    /** 任务清单：GET /system/task/list（非分页） */
    getSystemTasks: () => api.get<SystemTaskRow[]>('/system/task/list'),

    /** 手动执行任务：GET {taskUrl}（如 /system/task/update_website_daily_data） */
    runSystemTask: (taskUrl: string) => api.get<void>(taskUrl)
  };
};
