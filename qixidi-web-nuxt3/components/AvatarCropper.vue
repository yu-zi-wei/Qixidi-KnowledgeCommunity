<template>
  <n-modal v-model:show="visible" preset="dialog" title="裁剪头像" :show-icon="false" :mask-closable="false" style="width: 560px; max-width: 92vw;">
    <div class="cropper-wrapper">
      <canvas
        ref="canvasRef"
        class="cropper-canvas"
        @mousedown="onPointerDown"
        @mousemove="onPointerMove"
        @mouseup="onPointerUp"
        @mouseleave="onPointerUp"
        @touchstart.prevent="onTouchStart"
        @touchmove.prevent="onTouchMove"
        @touchend="onPointerUp"
        @wheel.prevent="onWheel"
      />
    </div>
    <p class="cropper-tip">拖拽移动图片，滚轮缩放</p>
    <template #action>
      <n-button @click="visible = false">取消</n-button>
      <n-button type="primary" @click="handleConfirm">确认</n-button>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
const props = defineProps<{
  imageSrc: string
  show: boolean
}>()

const emit = defineEmits<{
  'update:show': [value: boolean]
  confirm: [blob: Blob]
}>()

const visible = computed({
  get: () => props.show,
  set: (val) => emit('update:show', val)
})

const canvasRef = ref<HTMLCanvasElement | null>(null)
const CANVAS_SIZE = 480
const CANVAS_HEIGHT = 380
const OUTPUT_SIZE = 256
const RADIUS = 110

let img: HTMLImageElement | null = null
let baseScale = 1
let zoom = 1
let imgX = 0
let imgY = 0
let dragging = false
let dragStartX = 0
let dragStartY = 0
let imgStartX = 0
let imgStartY = 0

// 圆心始终在画布中心
const cx = CANVAS_SIZE / 2
const cy = CANVAS_HEIGHT / 2

watch(() => props.show, async (show) => {
  if (!show || !props.imageSrc) return
  await nextTick()
  loadImage(props.imageSrc)
})

const loadImage = (src: string) => {
  const image = new Image()
  image.crossOrigin = 'anonymous'
  image.onload = () => {
    img = image
    const canvas = canvasRef.value
    if (!canvas) return

    canvas.width = CANVAS_SIZE
    canvas.height = CANVAS_HEIGHT

    // 初始缩放：图片短边刚好填满圆形区域
    const fitSize = RADIUS * 2
    baseScale = fitSize / Math.min(image.width, image.height)
    zoom = 1

    // 图片居中
    const drawW = image.width * baseScale * zoom
    const drawH = image.height * baseScale * zoom
    imgX = (CANVAS_SIZE - drawW) / 2
    imgY = (CANVAS_HEIGHT - drawH) / 2

    draw()
  }
  image.src = src
}

const draw = () => {
  const canvas = canvasRef.value
  if (!canvas || !img) return
  const ctx = canvas.getContext('2d')!
  const w = canvas.width
  const h = canvas.height

  ctx.clearRect(0, 0, w, h)

  // 1. 画图片
  const drawW = img.width * baseScale * zoom
  const drawH = img.height * baseScale * zoom
  ctx.drawImage(img, imgX, imgY, drawW, drawH)

  // 2. 半透明遮罩（圆形挖空）
  ctx.save()
  ctx.fillStyle = 'rgba(0, 0, 0, 0.55)'
  ctx.beginPath()
  ctx.rect(0, 0, w, h)
  ctx.arc(cx, cy, RADIUS, 0, Math.PI * 2, true)
  ctx.fill()
  ctx.restore()

  // 3. 圆形边框
  ctx.beginPath()
  ctx.arc(cx, cy, RADIUS, 0, Math.PI * 2)
  ctx.strokeStyle = 'rgba(255, 255, 255, 0.8)'
  ctx.lineWidth = 2
  ctx.stroke()
}

// --- 鼠标事件 ---
const getCanvasPos = (e: MouseEvent) => {
  const canvas = canvasRef.value!
  const rect = canvas.getBoundingClientRect()
  return {
    x: (e.clientX - rect.left) * (canvas.width / rect.width),
    y: (e.clientY - rect.top) * (canvas.height / rect.height)
  }
}

const onPointerDown = (e: MouseEvent) => {
  dragging = true
  const pos = getCanvasPos(e)
  dragStartX = pos.x
  dragStartY = pos.y
  imgStartX = imgX
  imgStartY = imgY
}

const onPointerMove = (e: MouseEvent) => {
  if (!dragging) return
  const pos = getCanvasPos(e)
  imgX = imgStartX + (pos.x - dragStartX)
  imgY = imgStartY + (pos.y - dragStartY)
  draw()
}

const onPointerUp = () => {
  dragging = false
}

// --- 触摸事件 ---
const onTouchStart = (e: TouchEvent) => {
  const touch = e.touches[0]
  const canvas = canvasRef.value!
  const rect = canvas.getBoundingClientRect()
  dragStartX = (touch.clientX - rect.left) * (canvas.width / rect.width)
  dragStartY = (touch.clientY - rect.top) * (canvas.height / rect.height)
  imgStartX = imgX
  imgStartY = imgY
  dragging = true
}

const onTouchMove = (e: TouchEvent) => {
  if (!dragging) return
  const touch = e.touches[0]
  const canvas = canvasRef.value!
  const rect = canvas.getBoundingClientRect()
  const x = (touch.clientX - rect.left) * (canvas.width / rect.width)
  const y = (touch.clientY - rect.top) * (canvas.height / rect.height)
  imgX = imgStartX + (x - dragStartX)
  imgY = imgStartY + (y - dragStartY)
  draw()
}

// --- 滚轮缩放 ---
const onWheel = (e: WheelEvent) => {
  const delta = e.deltaY > 0 ? -0.05 : 0.05
  const newZoom = Math.max(0.5, Math.min(5, zoom + delta))

  // 以鼠标位置为锚点缩放
  const pos = getCanvasPos(e)
  const ratio = newZoom / zoom
  imgX = pos.x - (pos.x - imgX) * ratio
  imgY = pos.y - (pos.y - imgY) * ratio
  zoom = newZoom

  draw()
}

// --- 裁剪 ---
const handleConfirm = () => {
  if (!img) return

  const totalScale = baseScale * zoom

  // 圆形区域在原始图片上的坐标
  const srcX = (cx - RADIUS - imgX) / totalScale
  const srcY = (cy - RADIUS - imgY) / totalScale
  const srcSize = (RADIUS * 2) / totalScale

  const cropCanvas = document.createElement('canvas')
  cropCanvas.width = OUTPUT_SIZE
  cropCanvas.height = OUTPUT_SIZE
  const ctx = cropCanvas.getContext('2d')!

  ctx.beginPath()
  ctx.arc(OUTPUT_SIZE / 2, OUTPUT_SIZE / 2, OUTPUT_SIZE / 2, 0, Math.PI * 2)
  ctx.clip()
  ctx.drawImage(img, srcX, srcY, srcSize, srcSize, 0, 0, OUTPUT_SIZE, OUTPUT_SIZE)

  cropCanvas.toBlob((blob) => {
    if (blob) {
      emit('confirm', blob)
      visible.value = false
    }
  }, 'image/png')
}
</script>

<style scoped>
.cropper-wrapper {
  display: flex;
  justify-content: center;
  padding: 4px 0 0;
}

.cropper-canvas {
  cursor: grab;
  border-radius: var(--radius-md);
  max-width: 100%;
  height: auto;
  background: var(--color-surface-dim);
}

.cropper-canvas:active {
  cursor: grabbing;
}

.cropper-tip {
  text-align: center;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin: 8px 0 0;
}
</style>
