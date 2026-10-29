/**
 * 生成 README 用技术架构图 SVG（qixidi 模块化单体 · 三端接入）
 *
 * 用法：node docs/tools/gen-arch-svg.cjs   → img/技术架构图.svg
 * 构图：接入层（Nuxt3 门户 / Vue2 旧后台 / AI Agent·MCP 莓红强调）
 *       → 应用层 qixidi-service（Spring Boot 4 模块化单体，8 模块四行）
 *       → 数据层（MySQL / Redis / OSS）
 * 与 gen-mindmap-svg.cjs 同一视觉语言：暖纸底、栖息地绿、AI/MCP 一律莓红。
 * 纯 <text> + <rect> + <line>（无 foreignObject），GitHub / Gitee README <img> 均可渲染。
 */
const fs = require('fs');
const path = require('path');

/* ---------------- 版式 ---------------- */
const W = 1240;
const FONT = "'PingFang SC','Microsoft YaHei','Noto Sans SC',sans-serif";
const BG = '#FBFAF5';
const GREEN = '#3E7C59';   // 栖息地绿：主体与常规链路
const BERRY = '#B23A78';   // 莓红：AI / MCP 专属（与功能脑图同义）
const TEAL = '#2F7E86';    // 青：WebSocket 实时链路
const INK = '#3A372F';
const INK2 = '#6B6558';
const LINE_GRAY = '#9C9585';
const BOX_FILL = '#FFFFFF';
const BOX_STROKE = '#CFC8B8';
const INNER_FILL = '#FCFBF7';

function textW(s, size) {
  let w = 0;
  for (const ch of s) w += /[\x00-\xff]/.test(ch) ? size * 0.56 : size * 1.0;
  return w * 1.05;
}
function esc(s) { return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); }

const S = []; // SVG 片段

/* 框：主名 + 副注两行居中 */
function box(x, y, w, h, { title, sub, stroke = BOX_STROKE, fill = BOX_FILL, titleFill = INK }) {
  S.push(`<rect x="${x}" y="${y}" width="${w}" height="${h}" rx="9" fill="${fill}" stroke="${stroke}" stroke-width="1.3"/>`);
  if (sub) {
    S.push(`<text x="${(x + w / 2).toFixed(1)}" y="${y + h / 2 - 6}" font-size="14" font-weight="700" fill="${titleFill}" text-anchor="middle">${esc(title)}</text>`);
    S.push(`<text x="${(x + w / 2).toFixed(1)}" y="${y + h / 2 + 15}" font-size="11.5" fill="${INK2}" text-anchor="middle">${esc(sub)}</text>`);
  } else {
    S.push(`<text x="${(x + w / 2).toFixed(1)}" y="${y + h / 2 + 5}" font-size="14" font-weight="700" fill="${titleFill}" text-anchor="middle">${esc(title)}</text>`);
  }
}

/* 层名行：色点 + 层名 + 说明 */
function layerTitle(x, y, color, name, desc) {
  S.push(`<circle cx="${x + 5}" cy="${y - 4}" r="4.5" fill="${color}"/>`);
  S.push(`<text x="${x + 18}" y="${y}" font-size="13.5" font-weight="700" fill="${INK}">${esc(name)}</text>`);
  if (desc) {
    S.push(`<text x="${x + 18 + textW(name, 13.5) + 14}" y="${y}" font-size="11.5" fill="${INK2}">${esc(desc)}</text>`);
  }
}

/* 垂直连线（箭头 + 右侧标注） */
let markerSeq = 0;
function vline(x, y1, y2, { color = LINE_GRAY, label, dash, labelFill } = {}) {
  const id = `arr${markerSeq++}`;
  S.push(`<marker id="${id}" viewBox="0 0 10 10" refX="8.5" refY="5" markerWidth="6.5" markerHeight="6.5" orient="auto-start-reverse"><path d="M0,0 L10,5 L0,10 z" fill="${color}"/></marker>`);
  const attrs = dash ? ` stroke-dasharray="${dash}"` : '';
  S.push(`<line x1="${x}" y1="${y1}" x2="${x}" y2="${y2}" stroke="${color}" stroke-width="1.6"${attrs} marker-end="url(#${id})"/>`);
  if (label) {
    S.push(`<text x="${x + 9}" y="${(y1 + y2) / 2 + 4}" font-size="11" fill="${labelFill || color}">${esc(label)}</text>`);
  }
}

/* ---------------- 坐标 ---------------- */
const PAD = 44;
const INNER_W = W - PAD * 2;      // 1152
const GAP = 22;
const COL3 = (INNER_W - GAP * 2) / 3;  // 三等分列宽

const svgH = 728;

/* 接入层 */
const yAccessTitle = 56;
const yAccess = 72;
const hAccess = 62;
layerTitle(PAD, yAccessTitle, GREEN, '接入层', '浏览器 / AI Agent 三端入口');
const access = [
  { title: '门户 + 用户后台', sub: 'qixidi-web-nuxt3 · Nuxt 3 · SSR', berry: false },
  { title: '旧版管理后台', sub: 'qixidi-web-admin · Vue 2 · 维护模式', berry: false },
  { title: 'AI Agent', sub: 'Claude Code 等 · 以用户身份创作', berry: true },
];
const accessCx = [];
access.forEach((it, i) => {
  const x = PAD + i * (COL3 + GAP);
  accessCx.push(x + COL3 / 2);
  box(x, yAccess, COL3, hAccess, {
    title: it.title, sub: it.sub,
    stroke: it.berry ? BERRY : BOX_STROKE,
    fill: it.berry ? '#FDF4F8' : BOX_FILL,
    titleFill: it.berry ? BERRY : INK,
  });
});

/* 连接区 1：接入层 → 应用层 */
const yLink1Top = yAccess + hAccess;          // 134
const yLink1Bot = yLink1Top + 72;             // 206
vline(accessCx[0] - 128, yLink1Top, yLink1Bot, { color: TEAL, dash: '5,4', label: 'WS 实时推送' });
vline(accessCx[0], yLink1Top, yLink1Bot, { color: GREEN, label: 'HTTPS · nginx 反代 /api' });
vline(accessCx[1], yLink1Top, yLink1Bot, { color: GREEN, label: 'HTTPS · /api' });
vline(accessCx[2], yLink1Top, yLink1Bot, { color: BERRY, label: 'MCP 协议 · API Key', labelFill: BERRY });

/* 应用层：qixidi-service 模块化单体大框 */
const yAppTitle = 232;
const yApp = 246;
layerTitle(PAD, yAppTitle, GREEN, '应用层 · qixidi-service', 'Spring Boot 4.0 · Java 17 · 模块化单体（可拆可合 · 单机即起）');
const appH = 296;
S.push(`<rect x="${PAD}" y="${yApp}" width="${INNER_W}" height="${appH}" rx="12" fill="#F7F6F0" stroke="${GREEN}" stroke-width="1.8"/>`);

const inX = PAD + 18;                 // 大框内边距
const inW = INNER_W - 36;
let ry = yApp + 18;

/* 行 1：framework 通栏 */
const FRAME_H = 40;
S.push(`<rect x="${inX}" y="${ry}" width="${inW}" height="${FRAME_H}" rx="8" fill="${INNER_FILL}" stroke="${BOX_STROKE}" stroke-width="1.2"/>`);
S.push(`<text x="${(inX + inW / 2).toFixed(1)}" y="${ry + FRAME_H / 2 + 5}" font-size="13.5" font-weight="700" fill="${INK}" text-anchor="middle">framework 核心框架 —— 统一返回 · 防刷限流 · 全局日志 · Sa-Token 会话</text>`);
ry += FRAME_H + 13;

/* 行 2：能力三模块 */
const MOD_H = 58;
const mods = [
  { name: 'qixidi-auth', sub: '认证鉴权 · Sa-Token · 三端登录', berry: false },
  { name: 'qixidi-ai', sub: 'Spring AI · 对话 · 摘要生成', berry: false },
  { name: 'qixidi-mcp', sub: 'MCP 端点 · API Key 会话桥', berry: true },
];
mods.forEach((m, i) => {
  const x = inX + i * ((inW - GAP * 2) / 3 + GAP);
  box(x, ry, (inW - GAP * 2) / 3, MOD_H, {
    title: m.name, sub: m.sub,
    stroke: m.berry ? BERRY : BOX_STROKE,
    fill: m.berry ? '#FDF4F8' : INNER_FILL,
    titleFill: m.berry ? BERRY : INK,
  });
});
ry += MOD_H + 13;

/* 行 3：qixidi-business 通栏（两行） */
const BIZ_H = 58;
S.push(`<rect x="${inX}" y="${ry}" width="${inW}" height="${BIZ_H}" rx="8" fill="${INNER_FILL}" stroke="${BOX_STROKE}" stroke-width="1.2"/>`);
S.push(`<text x="${(inX + inW / 2).toFixed(1)}" y="${ry + 24}" font-size="13.5" font-weight="700" fill="${INK}" text-anchor="middle">qixidi-business 核心业务</text>`);
S.push(`<text x="${(inX + inW / 2).toFixed(1)}" y="${ry + 44}" font-size="11.5" fill="${INK2}" text-anchor="middle">文章 · 专栏 · 专题 · 合集 · 随笔 · 时光小记 · 相册 · 评论 · 私信 · 关注 · 搜索 · 消息 —— MyBatis-Plus</text>`);
ry += BIZ_H + 13;

/* 行 4：支撑三模块 */
const base = [
  { name: 'qixidi-system', sub: '系统管理 · RBAC · 菜单 · 日志' },
  { name: 'qixidi-common', sub: '公共 · 工具 · 常量 · 异常' },
  { name: 'qixidi-startup', sub: '启动装配 · 单产物打包' },
];
base.forEach((m, i) => {
  const x = inX + i * ((inW - GAP * 2) / 3 + GAP);
  box(x, ry, (inW - GAP * 2) / 3, MOD_H, { title: m.name, sub: m.sub, fill: INNER_FILL });
});

/* 连接区 2：应用层 → 数据层（x 对准数据层三框中心） */
const yLink2Top = yApp + appH;              // 542
const yDataTitle = yLink2Top + 46;          // 588
const yData = yDataTitle + 16;              // 604
const yLink2Bot = yData;
const hData = 62;
const dataCx = [];
for (let i = 0; i < 3; i++) dataCx.push(PAD + i * (COL3 + GAP) + COL3 / 2);
vline(dataCx[0], yLink2Top, yLink2Bot, { label: 'MyBatis-Plus' });
vline(dataCx[1], yLink2Top, yLink2Bot, { label: 'Redisson' });
vline(dataCx[2], yLink2Top, yLink2Bot, { label: 'OSS SDK' });

/* 数据层（desc 不写长文：层标题带与第一条竖线 x=COL3 中心相距仅 ~230px，长描述会横穿连线） */
layerTitle(PAD, yDataTitle, '#B0713A', '数据层', '');
const dataBoxes = [
  { title: 'MySQL 8.0', sub: '业务数据 · 全量持久化' },
  { title: 'Redis 6', sub: '会话 · 缓存 · 分布式锁' },
  { title: '阿里云 OSS / MinIO', sub: '图片等对象存储 · 可切换' },
];
dataBoxes.forEach((d, i) => {
  box(PAD + i * (COL3 + GAP), yData, COL3, hData, d);
});

/* 落款 */
S.push(`<text x="${W - 24}" y="${svgH - 20}" font-size="11" fill="#B3AC9D" text-anchor="end">qixidi · 模块化单体 · 单机可部署（MySQL + Redis + 一枚 JAR） · 2026-10</text>`);

/* ---------------- 输出 ---------------- */
const svg = [
  `<svg xmlns="http://www.w3.org/2000/svg" font-family="${FONT}" viewBox="0 0 ${W} ${svgH}">`,
  `<rect width="100%" height="100%" fill="${BG}"/>`,
  ...S,
  '</svg>',
].join('\n');

const outPath = path.resolve(__dirname, '../../img/技术架构图.svg');
fs.writeFileSync(outPath, svg, 'utf8');
console.log('written:', outPath, `(${(fs.statSync(outPath).size / 1024).toFixed(1)} KB) viewBox ${W}x${svgH}`);
