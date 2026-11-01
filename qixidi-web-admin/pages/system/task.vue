<template>
  <div class="task-page">
    <div class="task-card">
      <div class="task-toolbar">
        <div class="task-toolbar-left">
          <span class="task-count">共 {{ tasks.length }} 个任务</span>
        </div>
        <n-button size="small" :loading="loading" @click="loadTasks">刷新</n-button>
      </div>

      <n-data-table
        class="task-table"
        flex-height
        size="small"
        :columns="columns"
        :data="tasks"
        :loading="loading"
        :row-key="rowKeyFn"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import { useTaskAdminApi, type SystemTaskRow } from '~/composables/useTaskAdminApi';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getSystemTasks, runSystemTask } = useTaskAdminApi();

const loading = ref(false);
const tasks = ref<SystemTaskRow[]>([]);
/** 正在执行的任务名（按名称匹配行 loading） */
const runningTask = ref<string | null>(null);

const rowKeyFn = (row: SystemTaskRow) => row.taskName || row.taskUrl || Math.random();

const loadTasks = async () => {
  loading.value = true;
  try {
    const list = await getSystemTasks();
    tasks.value = list || [];
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    loading.value = false;
  }
};
onMounted(loadTasks);

const doRun = (row: SystemTaskRow) => {
  if (!row.taskUrl) {
    message.warning('该任务未配置执行地址');
    return;
  }
  dialog.info({
    title: '手动执行任务',
    content: `将立即执行「${row.taskName}」，执行期间相关统计数据会被重算，确认执行？`,
    positiveText: '执行',
    negativeText: '取消',
    onPositiveClick: async () => {
      runningTask.value = row.taskName || null;
      try {
        await runSystemTask(row.taskUrl);
        message.success(`「${row.taskName}」执行完成`);
        await loadTasks();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        runningTask.value = null;
      }
    }
  });
};

const columns: DataTableColumns<SystemTaskRow> = [
  { title: '任务名称', key: 'taskName', minWidth: 220, ellipsis: { tooltip: true } },
  { title: '执行时间', key: 'taskExecutionTime', minWidth: 160 },
  { title: '累计执行次数', key: 'taskExecutionSum', width: 120, align: 'center' },
  {
    title: '操作',
    key: 'actions',
    width: 100,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h(
        NButton,
        {
          size: 'small',
          text: true,
          type: 'primary',
          disabled: !row.taskUrl,
          loading: runningTask.value != null && runningTask.value === row.taskName,
          onClick: () => doRun(row)
        },
        { default: () => '执行' }
      )
  }
];
</script>

<style scoped lang="scss">
/* 与 AdminListPage 同款骨架：外层零滚动、单纸片卡片 */
.task-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
}

.task-card {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 12px 16px 14px;
  background: var(--panel-bg);
  border-radius: 10px;
  box-shadow: var(--panel-shadow, 0 1px 3px rgba(180, 170, 150, 0.18));
}

.task-toolbar {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 12px;
}

.task-count {
  font-size: 13px;
}

.task-table {
  flex: 1;
  min-height: 0;
}

.task-table :deep(.n-data-table-th) {
  font-weight: 600;
}

@media (max-width: 768px) {
  .task-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }
}
</style>
