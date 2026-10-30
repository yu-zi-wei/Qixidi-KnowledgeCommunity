/**
 * 后台菜单树与面包屑（单一数据源）
 *
 * 侧栏 menuOptions 与 header 面包屑共用这棵树：
 * - 叶子 key 必须是路由路径（供 n-menu 高亮匹配）
 * - 父级 key 用非路由唯一标识
 */
import type { Component } from 'vue';
import {
  Dashboard,
  FileText,
  MessageCircle,
  Users,
  Settings,
  Adjustments,
  Folders
} from '@vicons/tabler';

export interface AdminMenuItem {
  label: string;
  /** 叶子=路由路径；父级=非路由唯一标识 */
  key: string;
  icon?: Component;
  children?: AdminMenuItem[];
}

export const ADMIN_MENU: AdminMenuItem[] = [
  { label: '数据看板', key: '/', icon: Dashboard },
  {
    label: '内容管理',
    key: 'content',
    icon: Folders,
    children: [
      { label: '文章管理', key: '/article', icon: FileText },
      { label: '评论管理', key: '/comment', icon: MessageCircle }
    ]
  },
  { label: '前台用户', key: '/user', icon: Users },
  {
    label: '系统管理',
    key: 'system',
    icon: Settings,
    children: [{ label: '系统设置', key: '/setting', icon: Adjustments }]
  }
];

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
