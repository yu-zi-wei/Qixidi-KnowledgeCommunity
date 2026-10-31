/**
 * OSS 文件管理 API（SysOssController，/system/oss）
 * 列表 + 删除 + 下载；上传走前台用户端（后台不做上传入口）
 */

/** OSS 行（SysOssVo 字段子集） */
export interface OssRow {
  ossId: number;
  /** 文件名（服务端生成） */
  fileName?: string;
  /** 原名（用户上传时的文件名） */
  originalName?: string;
  /** 后缀（含点，如 .png） */
  fileSuffix?: string;
  url?: string;
  /** 存储服务商（如 aliyun） */
  service?: string;
  createBy?: string;
  createTime?: string;
}

/** 图片后缀集合（缩略图直显，其余显示文件占位） */
export const IMAGE_SUFFIXES = ['.png', '.jpg', '.jpeg', '.gif', '.webp', '.bmp', '.svg'];

export const isImageSuffix = (suffix?: string) => !!suffix && IMAGE_SUFFIXES.includes(suffix.toLowerCase());

export const useOssAdminApi = () => {
  const api = useApi();

  return {
    /** 分页列表：POST /system/oss/list（originalName/fileName like，fileSuffix eq，createTime desc） */
    getOssList: (params: Record<string, any>) => api.postPage<OssRow>('/system/oss/list', params),

    /** 批量删除：DELETE /system/oss/{ossIds}（后端 deleteWithValidByIds 校验引用） */
    deleteOss: (ids: (number | string)[]) =>
      api.request<void>(`/system/oss/${ids.join(',')}`, { method: 'DELETE' }),

    /** 下载：GET /system/oss/download/{ossId}（token 在 header，须 blob 手动触发保存） */
    downloadOss: async (row: OssRow) => {
      const blob = await api.request<Blob>(`/system/oss/download/${row.ossId}`, {
        method: 'GET',
        responseType: 'blob'
      });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = row.originalName || row.fileName || `${row.ossId}`;
      a.click();
      URL.revokeObjectURL(url);
    }
  };
};
