<template>
  <AdminListPage ref="listRef" :search-fields="searchFields" :columns="columns" :fetch="getUsers" row-key="uuid" seq />

  <!-- 用户详情抽屉：一律调详情接口，先清空防残留（teleport 组件宽度必须 inline style） -->
  <n-drawer v-model:show="detailVisible" :width="480" placement="right">
    <n-drawer-content :title="detail?.nickname || detail?.username || '用户详情'" closable>
      <n-spin :show="detailLoading">
        <div v-if="detail" class="detail-body">
          <div class="detail-head">
            <AdminAvatar :src="detail.avatar" :name="detail.nickname || detail.username" :size="48" />
            <div class="detail-head-main">
              <div class="detail-name">{{ detail.nickname || '—' }}</div>
              <div class="detail-username">@{{ detail.username || '—' }}</div>
            </div>
            <AdminStateTag :value="detail.state" :map="USER_STATE_MAP" />
          </div>

          <div class="detail-meta">
            <div class="meta-row"><span class="meta-label">邮箱</span><span>{{ detail.email || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">手机</span><span>{{ detail.phone || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">来源</span><span>{{ detail.source || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">登录 IP</span><span>{{ detail.loginIp || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">注册时间</span><span>{{ getFullDateTime(detail.createTime) || '—' }}</span></div>
          </div>

          <n-divider>社区数据</n-divider>
          <div class="detail-stats">
            <div class="stat-item"><span class="stat-num">{{ detail.fabulousCount ?? 0 }}</span><span class="stat-label">获赞</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.collectionCount ?? 0 }}</span><span class="stat-label">收藏</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.followCount ?? 0 }}</span><span class="stat-label">关注</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.fansFollowCount ?? 0 }}</span><span class="stat-label">粉丝</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.commentCount ?? 0 }}</span><span class="stat-label">评论</span></div>
          </div>

          <template v-if="detail.introduce">
            <n-divider>个人介绍</n-divider>
            <p class="detail-introduce">{{ detail.introduce }}</p>
          </template>
        </div>
      </n-spin>
    </n-drawer-content>
  </n-drawer>

  <!-- 角色调整弹窗（teleport 组件宽度必须 inline style） -->
  <n-modal v-model:show="roleVisible" :style="{ width: '420px' }" class="user-role-modal-host" preset="card" title="调整用户角色">
    <div v-if="roleUser" class="role-head">
      <AdminAvatar :src="roleUser.avatar" :name="roleUser.nickname || roleUser.username" :size="32" />
      <div class="role-head-main">
        <div class="role-name">{{ roleUser.nickname || '—' }}</div>
        <div class="role-cur">当前角色：{{ USER_ROLE_MAP[roleUser.roleId ?? 1]?.label || '—' }}</div>
      </div>
    </div>
    <n-radio-group v-model:value="targetRoleId" class="role-group">
      <n-radio v-for="(r, code) in USER_ROLE_MAP" :key="code" :value="Number(code)">{{ r.label }}</n-radio>
    </n-radio-group>
    <p class="role-tip">调整后将邮件/站内信通知该用户，并使其下线重新登录。</p>
    <template #footer>
      <div class="role-footer">
        <n-button size="small" @click="roleVisible = false">取消</n-button>
        <n-button size="small" type="primary" :loading="roleSaving" :disabled="targetRoleId == null || targetRoleId === roleUser?.roleId" @click="doUpdateRole">
          确认调整
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { USER_ROLE_MAP, USER_STATE_MAP, useUserAdminApi, type UserRow } from '~/composables/useUserAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getUsers, getUser, updateUser } = useUserAdminApi();

// ---------- 搜索（nickname/username 后端 like；states in） ----------
const searchFields: AdminSearchField[] = [
  { key: 'nickname', label: '昵称', type: 'input', placeholder: '请输入昵称关键词' },
  { key: 'username', label: '用户名', type: 'input', placeholder: '请输入用户名关键词' },
  { key: 'states', label: '状态', type: 'multiple', options: [
    { label: '正常', value: 0 },
    { label: '已冻结', value: 1 },
    { label: '已删除', value: 2 }
  ] }
];

const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);

// ---------- 冻结/解冻（TripartiteUserBo 无 EditGroup 约束，只传 {uuid, state}） ----------
const switchingUuid = ref<string | null>(null);
const toggleState = (row: UserRow) => {
  const freezing = row.state !== 1;
  dialog.warning({
    title: freezing ? '冻结用户' : '解冻用户',
    content: freezing
      ? `冻结后「${row.nickname || row.username}」将无法登录社区，确认冻结？`
      : `解冻后「${row.nickname || row.username}」可正常登录，确认解冻？`,
    positiveText: freezing ? '冻结' : '解冻',
    negativeText: '取消',
    onPositiveClick: async () => {
      switchingUuid.value = row.uuid;
      try {
        await updateUser({ uuid: row.uuid, state: freezing ? 1 : 0 });
        message.success(freezing ? '已冻结' : '已解冻');
        await listRef.value?.refresh();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        switchingUuid.value = null;
      }
    }
  });
};

// ---------- 角色调整（updateUser 只传 {uuid, roleId}，后端变更后自动通知 + 踢下线） ----------
const roleVisible = ref(false);
const roleSaving = ref(false);
const roleUser = ref<UserRow | null>(null);
const targetRoleId = ref<number | null>(null);

const openRole = (row: UserRow) => {
  roleUser.value = row;
  targetRoleId.value = row.roleId ?? null;
  roleVisible.value = true;
};

const doUpdateRole = async () => {
  if (!roleUser.value || targetRoleId.value == null) return;
  roleSaving.value = true;
  try {
    await updateUser({ uuid: roleUser.value.uuid, roleId: targetRoleId.value });
    message.success(`已将「${roleUser.value.nickname || roleUser.value.username}」调整为${USER_ROLE_MAP[targetRoleId.value].label}`);
    roleVisible.value = false;
    await listRef.value?.refresh();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    roleSaving.value = false;
  }
};

// ---------- 详情抽屉（昵称点击打开） ----------
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<UserRow | null>(null);
const openDetail = async (row: UserRow) => {
  detailVisible.value = true;
  detailLoading.value = true;
  detail.value = null; // 先清空，避免残留上一个用户内容
  try {
    detail.value = await getUser(row.uuid);
  } catch {
    detail.value = null; // useApi 已弹错，失败留空态，不回退列表数据
  } finally {
    detailLoading.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<UserRow> = [
  {
    title: '用户',
    key: 'nickname',
    minWidth: 200,
    render: (row) =>
      h('div', { class: 'cell-user' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname || row.username, size: 32 }),
        h('div', { class: 'cell-user-main' }, [
          // 昵称即详情入口；样式类在 admin-table.scss
          h('span', { class: 'cell-user-name is-link', onClick: () => openDetail(row), title: '查看详情' }, row.nickname || '—'),
          row.username ? h('span', { class: 'cell-user-username' }, `@${row.username}`) : null
        ])
      ])
  },
  {
    title: '邮箱',
    key: 'email',
    minWidth: 170,
    ellipsis: { tooltip: true },
    render: (row) => row.email || '—'
  },
  {
    title: '来源',
    key: 'source',
    width: 90,
    ellipsis: { tooltip: true },
    render: (row) => row.source || '—'
  },
  {
    // 表头紧凑无空格防折行（180px 内装不下带空格版本）
    title: '获赞/关注/粉丝/评论',
    key: 'fabulousCount',
    width: 180,
    render: (row) =>
      h('span', { class: 'cell-stats' }, `${row.fabulousCount ?? 0} / ${row.followCount ?? 0} / ${row.fansFollowCount ?? 0} / ${row.commentCount ?? 0}`)
  },
  {
    title: '角色',
    key: 'roleId',
    minWidth: 90,
    render: (row) => h(AdminStateTag, { value: row.roleId ?? 1, map: USER_ROLE_MAP })
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: USER_STATE_MAP })
  },
  {
    title: '注册时间',
    key: 'createTime',
    width: 150,
    render: (row) => getFullDateTime(row.createTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 110,
    align: 'center',
    fixed: 'right',
    render: (row) => {
      if (row.state === 2) return h('span', { style: 'font-size: 12px; opacity: 0.4;' }, '—');
      const freezing = row.state !== 1;
      return h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          {
            size: 'small',
            text: true,
            type: freezing ? 'error' : 'success',
            loading: switchingUuid.value === row.uuid,
            onClick: () => toggleState(row)
          },
          { default: () => (freezing ? '冻结' : '解冻') }
        ),
        h(
          NButton,
          { size: 'small', text: true, onClick: () => openRole(row) },
          { default: () => '角色' }
        )
      ]);
    }
  }
];
</script>

<style scoped lang="scss">
/* n-modal teleport 到 body，scoped 选不中——角色弹窗样式走全局类（页面非 scoped 块见下） */
.detail-body {
  .detail-head {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .detail-head-main {
    flex: 1;
    min-width: 0;
  }

  .detail-name {
    font-weight: 600;
  }

  .detail-username {
    font-size: 12px;
    opacity: 0.5;
  }

  .detail-meta {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 14px;
  }

  .meta-row {
    display: flex;
    align-items: baseline;
    gap: 12px;
    font-size: 13px;
  }

  .meta-label {
    width: 60px;
    flex-shrink: 0;
    opacity: 0.5;
  }

  .detail-stats {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    min-width: 56px;
    padding: 10px 8px;
    // 抽屉 teleport 到 body，布局根主题变量不可用；中性半透明灰双主题通用
    background: rgba(127, 127, 127, 0.08);
    border-radius: 8px;
  }

  .stat-num {
    font-size: 16px;
    font-weight: 600;
    font-variant-numeric: tabular-nums;
  }

  .stat-label {
    font-size: 12px;
    opacity: 0.55;
  }

  .detail-introduce {
    margin: 0;
    font-size: 13.5px;
    line-height: 1.7;
    white-space: pre-wrap;
    word-break: break-word;
    opacity: 0.85;
  }
}
</style>

<!-- n-modal teleport 到 body，scoped 样式选不中节点：角色弹窗样式放非 scoped 块（页面前缀防跨页覆盖） -->
<style lang="scss">
.user-role-modal-host {
  .role-head {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 14px;
  }

  .role-head-main {
    flex: 1;
    min-width: 0;
  }

  .role-name {
    font-weight: 600;
  }

  .role-cur {
    font-size: 12px;
    opacity: 0.5;
  }

  .role-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .role-tip {
    margin: 14px 0 0;
    font-size: 12px;
    opacity: 0.55;
  }

  .role-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
