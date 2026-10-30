/**
 * 权限码判定（RBAC 简化模型：权限归数据库、菜单归代码）
 *
 * 登录时 /getInfo 返回的 permissions 存入 adminStore：
 * - 超管后端返回 ['*']，通配一切权限码
 * - 普通用户为角色挂载的权限码集合（sys_role_perm）
 * 菜单可见性与按钮级控制统一走 hasPerm
 */
export const usePerm = () => {
  const adminStore = useAdminStore();

  /** 超管（持有通配码 *） */
  const isAdmin = computed(() => adminStore.permissions.includes('*'));

  /** 是否持有权限码；perm 缺省视为公开（数据看板、占位页等） */
  const hasPerm = (perm?: string) =>
    !perm || isAdmin.value || adminStore.permissions.includes(perm);

  return { isAdmin, hasPerm };
};
