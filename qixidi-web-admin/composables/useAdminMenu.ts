/**
 * 后台菜单树与面包屑（单一数据源）
 *
 * RBAC 简化模型：菜单归代码、权限归数据库——本树是菜单唯一出处，
 * 叶子标 perm（后端 @SaCheckPermission 扫描产出的权限码）推导可见性（useAdminFilteredMenu）
 *
 * 侧栏 menuOptions 与 header 面包屑共用这棵树：
 * - 叶子 key 必须是路由路径（供 n-menu 高亮匹配）
 * - 父级 key 用非路由唯一标识
 * - 一级菜单必须带子菜单（禁止纯一级叶子）；数据看板无菜单入口——首页 / 即看板页
 */
import type { Component } from 'vue';
import {
  FileText,
  MessageCircle,
  Users,
  Settings,
  Adjustments,
  Notebook,
  Feather,
  Folders,
  Book2,
  Shield,
  IdBadge,
  Activity,
  History,
  ReportAnalytics,
  Photo,
  ShieldLock,
  Tags,
  Bookmarks
} from '@vicons/tabler';

export interface AdminMenuItem {
  label: string;
  /** 叶子=路由路径；父级=非路由唯一标识 */
  key: string;
  icon?: Component;
  children?: AdminMenuItem[];
  /** 访问所需权限码（{模块}:{实体}:{动作}）；缺省视为公开 */
  perm?: string;
}

export const ADMIN_MENU: AdminMenuItem[] = [
  {
    label: '文章管理',
    key: 'article-mgmt',
    icon: FileText,
    children: [
      { label: '文章列表', key: '/article', icon: FileText, perm: 'article:information:query' },
      { label: '评论管理', key: '/comment', icon: MessageCircle, perm: 'article:comment:query' },
      { label: '标签管理', key: '/label', icon: Tags, perm: 'labelInfo:query' },
      { label: '标签分类', key: '/grouping', icon: Bookmarks, perm: 'groupingInfo:query' }
    ]
  },
  {
    label: '小记管理',
    key: 'time-notes-mgmt',
    icon: Notebook,
    children: [
      { label: '小记列表', key: '/time-notes', icon: Notebook, perm: 'timeNotes:query' },
      { label: '小记评论', key: '/time-notes/comment', icon: MessageCircle, perm: 'timeNotes:comment:query' }
    ]
  },
  {
    label: '随笔管理',
    key: 'dictum-mgmt',
    icon: Feather,
    children: [
      { label: '随笔列表', key: '/dictum', icon: Feather, perm: 'dictum:info:list' },
      { label: '随笔分组', key: '/dictum-group', icon: Folders, perm: 'dictum:group:list' },
      { label: '随笔专辑', key: '/dictum-album', icon: Book2, perm: 'dictum:album:list' }
    ]
  },
  {
    label: '前台用户',
    key: 'user-mgmt',
    icon: Users,
    children: [
      { label: '用户列表', key: '/user', icon: Users, perm: 'tripartiteUser:query' }
    ]
  },
  {
    label: '监控审计',
    key: 'monitor',
    icon: Activity,
    children: [
      { label: '操作日志', key: '/monitor/operlog', icon: History, perm: 'monitor:operlog:list' },
      { label: '登录日志', key: '/monitor/logininfor', icon: ReportAnalytics, perm: 'monitor:logininfor:list' },
      { label: 'IP 黑名单', key: '/monitor/black-list', icon: ShieldLock, perm: 'blackList:list' }
    ]
  },
  {
    label: '系统管理',
    key: 'system',
    icon: Settings,
    children: [
      { label: '角色管理', key: '/system/role', icon: Shield, perm: 'role:query' },
      { label: '后台账号', key: '/system/account', icon: IdBadge, perm: 'user:query' },
      { label: '系统设置', key: '/setting', icon: Adjustments },
      { label: 'OSS 文件', key: '/oss', icon: Photo, perm: 'oss:list' }
    ]
  }
];

/** 权限过滤后的菜单：叶子按 perm 过滤（超管 '*' 通配），子项全空的父级一并收起 */
export const useAdminFilteredMenu = () => {
  const { hasPerm } = usePerm();
  const filter = (nodes: AdminMenuItem[]): AdminMenuItem[] =>
    nodes
      .map((n) => (n.children ? { ...n, children: filter(n.children) } : n))
      .filter((n) => (n.children ? n.children.length > 0 : hasPerm(n.perm)));
  return computed(() => filter(ADMIN_MENU));
};

/** 面包屑项 */
export interface AdminCrumb {
  label: string;
  key: string;
  current: boolean;
}

/** 深度优先查路由在菜单树中的路径链，生成面包屑（首页项由组件自行补） */
const buildCrumbs = (path: string): AdminCrumb[] => {
  const dfs = (nodes: AdminMenuItem[], trail: AdminMenuItem[]): AdminMenuItem[] | null => {
    for (const node of nodes) {
      if (node.key === path) return [...trail, node];
      if (node.children) {
        const hit = dfs(node.children, [...trail, node]);
        if (hit) return hit;
      }
    }
    return null;
  };
  return (dfs(ADMIN_MENU, []) || []).map((n, i, arr) => ({
    label: n.label,
    key: n.key,
    current: i === arr.length - 1
  }));
};

export const useAdminBreadcrumbs = () => {
  const route = useRoute();
  return computed(() => buildCrumbs(route.path));
};
