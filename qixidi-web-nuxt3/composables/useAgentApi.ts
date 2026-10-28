/**
 * MCP agent 管理 API（API key / 写作 skill / 接入信息，/agent 前缀需登录）
 */
export interface AgentApiKeyItem {
  id: number
  keyName: string
  /** 1 有效 */
  status: number
  /** null = 永久 */
  expireTime: string | null
  lastUsedTime?: string
  createTime: string
}

export interface AgentSkillItem {
  id: number
  name: string
  description?: string
  content: string
  /** 1 启用 0 停用 */
  status: number
  createTime: string
}

export interface AgentSkillPayload {
  name: string
  description?: string
  content: string
}

export const useAgentApi = () => {
  const api = useApi()

  /**
   * MCP 接入信息（endpoint / 请求头名 / 客户端配置片段）
   */
  const getMcpInfo = () => {
    return api.get<{
      endpoint: string
      headerName: string
      notice: string
      snippets: { claudeCode: string; codex: string; generic: string }
    }>('/agent/mcp-info')
  }

  /**
   * 我的 API key 列表（脱敏，明文仅创建时返回一次）
   */
  const getKeyList = () => {
    return api.get<AgentApiKeyItem[]>('/agent/apiKey/list')
  }

  /**
   * 创建 API key，返回明文 { apiKey }，仅本次响应可见。
   * validDays：0=永久；不传=默认 365；>0=N 天
   */
  const createKey = (data: { keyName: string; validDays?: number }) => {
    return api.post<{ apiKey: string }>('/agent/apiKey', data)
  }

  /**
   * key 改名
   */
  const renameKey = (id: number, data: { keyName: string }) => {
    return api.put(`/agent/apiKey/${id}`, data)
  }

  /**
   * 吊销 key（删行 + 会话登出，之后该 key 调 MCP 立即 401）
   */
  const revokeKey = (id: number) => {
    return api.delete(`/agent/apiKey/${id}`)
  }

  /**
   * 我的写作 skill 列表（不含平台官方，官方由平台统一分发）
   */
  const getSkillList = () => {
    return api.get<AgentSkillItem[]>('/agent/skill/list')
  }

  /**
   * 新建 skill
   */
  const createSkill = (data: AgentSkillPayload) => {
    return api.post('/agent/skill', data)
  }

  /**
   * 更新 skill
   */
  const updateSkill = (id: number, data: AgentSkillPayload) => {
    return api.put(`/agent/skill/${id}`, data)
  }

  /**
   * 删除 skill
   */
  const deleteSkill = (id: number) => {
    return api.delete(`/agent/skill/${id}`)
  }

  return {
    getMcpInfo,
    getKeyList,
    createKey,
    renameKey,
    revokeKey,
    getSkillList,
    createSkill,
    updateSkill,
    deleteSkill
  }
}
