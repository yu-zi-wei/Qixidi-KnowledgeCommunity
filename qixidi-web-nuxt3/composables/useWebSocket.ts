import type { NewsUserSumVo, PrivateUserVo } from '~/types'

/**
 * WebSocket 消息格式（与后端 WebSocketMessage<T> 对应）
 */
interface WsMessage<T> {
  type: number
  data: T
}

// ==================== 全局单例（仅客户端） ====================
let ws: WebSocket | null = null
const connected = ref(false)
const unreadMap = ref<Record<number, number>>({})

// type=2 私信红点数据
const privateUserList = ref<PrivateUserVo[]>([])

// 事件回调（页面注册后，收到消息时触发）
let onPrivateMessage: ((data: PrivateUserVo[]) => void) | null = null

let reconnectTimer: ReturnType<typeof setTimeout> | null = null
let reconnectAttempts = 0
let listening = false
const maxReconnectAttempts = 5

const totalUnread = computed(() =>
  Object.values(unreadMap.value).reduce((sum, n) => sum + n, 0)
)

/**
 * 全局 WebSocket 单例
 */
export const useWebSocket = () => {
  const getUnread = (type: number) => unreadMap.value[type] || 0

  // SSR 直接返回空壳
  if (import.meta.server) {
    return {
      connected: ref(false),
      unreadMap: ref<Record<number, number>>({}),
      totalUnread: ref(0),
      privateUserList: ref<PrivateUserVo[]>([]),
      getUnread: (_type: number) => 0,
      onPrivateMessage: (_cb: (data: PrivateUserVo[]) => void) => {},
      offPrivateMessage: () => {}
    }
  }

  // ===== 以下仅客户端执行 =====

  const authStore = useAuthStore()
  const config = useRuntimeConfig()

  const buildWsUrl = () => {
    const userId = authStore.user?.uuid
    if (!userId) return null

    const wsBaseUrl = (config.public.wsBase as string) || ''

    let wsBase: string
    if (wsBaseUrl) {
      wsBase = wsBaseUrl
        .replace('https://', 'wss://')
        .replace('http://', 'ws://')
    } else {
      const protocol = location.protocol === 'https:' ? 'wss:' : 'ws:'
      wsBase = `${protocol}//${location.host}`
    }

    return `${wsBase}/websocket/${userId}`
  }

  const doConnect = () => {
    if (!authStore.isLoggedIn) return
    if (ws && ws.readyState === WebSocket.OPEN) return

    const url = buildWsUrl()
    if (!url) return

    try {
      const socket = new WebSocket(url)

      socket.onopen = () => {
        connected.value = true
        reconnectAttempts = 0
      }

      socket.onmessage = (event) => {
        try {
          const msg: WsMessage<any> = JSON.parse(event.data)

          // type=1 站内通知汇总
          if (msg.type === 1 && Array.isArray(msg.data)) {
            const map: Record<number, number> = {}
            ;(msg.data as NewsUserSumVo[]).forEach((item) => {
              map[item.type] = item.newsSum
            })
            unreadMap.value = map
          }

          // type=2 私信红点（用户列表含未读数）
          if (msg.type === 2 && msg.data) {
            const rows = (msg.data as any)?.rows || msg.data
            if (Array.isArray(rows)) {
              privateUserList.value = rows
              onPrivateMessage?.(rows)
            }
          }
        } catch {
          // 忽略非 JSON 消息
        }
      }

      socket.onclose = () => {
        connected.value = false
        ws = null
        scheduleReconnect()
      }

      socket.onerror = () => {
        socket.close()
      }

      ws = socket
    } catch {
      scheduleReconnect()
    }
  }

  const doDisconnect = () => {
    if (reconnectTimer) {
      clearTimeout(reconnectTimer)
      reconnectTimer = null
    }
    reconnectAttempts = maxReconnectAttempts
    if (ws) {
      ws.close()
      ws = null
    }
    connected.value = false
    unreadMap.value = {}
    privateUserList.value = []
  }

  const scheduleReconnect = () => {
    if (!authStore.isLoggedIn) return
    if (reconnectAttempts >= maxReconnectAttempts) return
    if (reconnectTimer) return

    reconnectAttempts++
    const delay = Math.min(1000 * Math.pow(2, reconnectAttempts - 1), 30000)
    reconnectTimer = setTimeout(() => {
      reconnectTimer = null
      doConnect()
    }, delay)
  }

  // 注册私信回调
  const registerOnPrivateMessage = (cb: (data: PrivateUserVo[]) => void) => {
    onPrivateMessage = cb
  }
  const unregisterOnPrivateMessage = () => {
    onPrivateMessage = null
  }

  // 只绑定一次事件监听
  if (!listening) {
    listening = true

    watch(
      () => authStore.isLoggedIn,
      (loggedIn) => {
        if (loggedIn) {
          reconnectAttempts = 0
          doConnect()
        } else {
          doDisconnect()
        }
      },
      { immediate: true }
    )

    document.addEventListener('visibilitychange', () => {
      if (document.visibilityState === 'visible' && authStore.isLoggedIn && !ws) {
        reconnectAttempts = 0
        doConnect()
      }
    })
  }

  return {
    connected,
    unreadMap,
    totalUnread,
    privateUserList,
    getUnread,
    onPrivateMessage: registerOnPrivateMessage,
    offPrivateMessage: unregisterOnPrivateMessage
  }
}
