/**
 * 生成 README 用功能脑图 SVG（前台 / 后台共用引擎）
 *
 * 用法：node docs/tools/gen-mindmap-svg.cjs [front|admin]
 *       front（默认）→ img/前台功能.svg   admin → img/后台功能.svg
 * 数据：mindmap-data.cjs（分支、配色、左右切分、落款均在数据文件维护）
 *
 * 设计：经典双侧脑图——根居中，一级分支左右摊开（前 split 个在右），
 * 三次贝塞尔曲线连线（XMind 风格），站点暖纸底 + 栖息地绿。
 * 纯 <text> + <path>（无 foreignObject），GitHub / Gitee README <img> 均可渲染。
 */
const fs = require('fs');
const path = require('path');
const DATA = require('./mindmap-data.cjs');

const which = process.argv[2] || 'front';
if (!DATA[which]) {
  console.error(`未知图类型 '${which}'，可选：${Object.keys(DATA).join(' / ')}`);
  process.exit(1);
}
const CONF = DATA[which];
const RIGHT = CONF.children.slice(0, CONF.split);
const LEFT = CONF.children.slice(CONF.split);

/* ---------------- 版式参数 ---------------- */
const FONT = "'PingFang SC','Microsoft YaHei','Noto Sans SC',sans-serif";
const FS = { 1: 15.5, 2: 13.5, 3: 12.5 };        // 各级字号
const LH = { 1: 34, 2: 27, 3: 24 };              // 各级行高（节点高）
const V_GAP = { 1: 30, 2: 14 };                  // 同层兄弟间距（按父层分档）
const H_GAP = 72;                                 // 层间水平间距
const ROOT_GAP = 96;                              // 根与一级分支间距
const PAD_X = 13;                                 // 根节点内边距
const BG = '#e8f0f8'; // 站点品牌浅底 --color-primary-light（qixidi-web-nuxt3/assets/css/main.css）
const INK = { 2: '#2E3A48', 3: '#5B6878' };
const ROOT_FILL = '#3E7C59';

/* 文字宽度估算：中文全角 ≈ 1.0em，ASCII 半角 ≈ 0.56em；×1.05 保守余量（跨平台字体差异） */
function textW(s, size) {
  let w = 0;
  for (const ch of s) w += /[\x00-\xff]/.test(ch) ? size * 0.56 : size * 1.0;
  return w * 1.05;
}

/* ---------------- 布局 ---------------- */
/* 后序量测：节点宽高 + 子树高 sh（子块总高，父垂直居中于子块） */
function measure(node, depth) {
  node.depth = depth;
  node.w = textW(node.label, FS[depth] || 17);
  node.h = LH[depth] || 46;
  if (!node.children) { node.sh = node.h; return node; }
  const gap = V_GAP[depth] || V_GAP[2];
  let sum = 0;
  node.children.forEach((c, i) => { measure(c, depth + 1); sum += c.sh + (i > 0 ? gap : 0); });
  node.sh = Math.max(node.h, sum);
  return node;
}

/* 父节点 cy 已定，子块垂直居中排布 */
function assignY(node, cy) {
  node.cy = cy;
  if (!node.children) return;
  const gap = V_GAP[node.depth] || V_GAP[2];
  const sum = node.children.reduce((a, c, i) => a + c.sh + (i > 0 ? gap : 0), 0);
  let y = cy - sum / 2;
  node.children.forEach((c) => { assignY(c, y + c.sh / 2); y += c.sh + gap; });
}

/* 一侧布局：按层最大宽定列缘（右侧=左缘向右推进 / 左侧=右缘向左推进），
   挂 parent、定 x、收集节点与连线；edge 为该列基准缘 */
function layoutSide(branches, firstEdge, side) {
  const maxW = [0, 0, 0, 0];
  const scan = (n, d) => { maxW[d] = Math.max(maxW[d], n.w); (n.children || []).forEach((c) => scan(c, d + 1)); };
  branches.forEach((b) => scan(b, 1));

  const nodes = [];
  const links = [];
  const walk = (n, parent, edge) => {
    n.parent = parent;
    n.x = side === 1 ? edge : edge - n.w;
    nodes.push(n);
    links.push({ from: parent, to: n }); // from = null 表示该节点是一级分支，连线接到根节点边缘
    const next = side === 1 ? edge + maxW[n.depth] + H_GAP : edge - maxW[n.depth] - H_GAP;
    (n.children || []).forEach((c) => walk(c, n, next));
  };
  branches.forEach((b) => walk(b, null, firstEdge));
  return { nodes, links, span: maxW[1] + H_GAP + maxW[2] + H_GAP + maxW[3] };
}

/* ---------------- SVG 输出 ---------------- */
function esc(s) { return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); }

/* 三次贝塞尔：水平方向控制点，XMind 风格圆滑曲线 */
function curve(x1, y1, x2, y2) {
  const dx = (x2 - x1) / 2;
  return `M ${x1.toFixed(1)} ${y1.toFixed(1)} C ${(x1 + dx).toFixed(1)} ${y1.toFixed(1)}, ${(x2 - dx).toFixed(1)} ${y2.toFixed(1)}, ${x2.toFixed(1)} ${y2.toFixed(1)}`;
}

function branchColor(n) {
  let cur = n;
  while (cur && cur.depth > 1) cur = cur.parent;
  return cur ? cur.color : '#3E7C59';
}

function render() {
  RIGHT.forEach((b) => measure(b, 1));
  LEFT.forEach((b) => measure(b, 1));

  const sideHeight = (branches) =>
    branches.reduce((a, b, i) => a + b.sh + (i > 0 ? V_GAP[1] : 0), 0);
  const height = Math.max(sideHeight(RIGHT), sideHeight(LEFT)) + 100;

  const rootW = textW(CONF.label, 17) + PAD_X * 2;
  // 几何：canvasW = 左列宽 + 根距 + 根 + 根距 + 右列宽 + 双侧边距
  const spanOf = (branches, side, mid) => layoutSide(branches, mid + side * (rootW / 2 + ROOT_GAP), side).span;
  const spanR = spanOf(RIGHT, 1, 0);
  const spanL = spanOf(LEFT, -1, 0);
  const MARGIN = 48;
  const canvasW = MARGIN + spanL + ROOT_GAP + rootW + ROOT_GAP + spanR + MARGIN;
  const midX = MARGIN + spanL + ROOT_GAP + rootW / 2;

  const rootCy = height / 2;
  const rootEdge = side => midX + side * (rootW / 2);

  const parts = [];
  for (const [branches, side] of [[RIGHT, 1], [LEFT, -1]]) {
    const firstEdge = midX + side * (rootW / 2 + ROOT_GAP);
    const { nodes, links } = layoutSide(branches, firstEdge, side);

    // 该侧一级分支块整体垂直居中
    let y = rootCy - sideHeight(branches) / 2;
    branches.forEach((b, i) => { assignY(b, y + b.sh / 2); y += b.sh + (i > 0 ? V_GAP[1] : 0); });

    const g = [];
    for (const { from, to } of links) {
      const x1 = from ? (side === 1 ? from.x + from.w : from.x) : rootEdge(side);
      const y1 = from ? from.cy : rootCy;
      const x2 = side === 1 ? to.x : to.x + to.w;
      const col = to.depth === 1 ? to.color : branchColor(to);
      const sw = to.depth === 1 ? 2.4 : 1.7;
      g.push(`<path d="${curve(x1, y1, x2, to.cy)}" fill="none" stroke="${col}" stroke-width="${sw}" stroke-opacity="0.85"/>`);
    }
    for (const n of nodes) {
      if (n.depth === 1) {
        g.push(`<text x="${n.x.toFixed(1)}" y="${(n.cy + 5.5).toFixed(1)}" font-size="${FS[1]}" font-weight="700" fill="${n.color}">${esc(n.label)}</text>`);
      } else {
        g.push(`<text x="${n.x.toFixed(1)}" y="${(n.cy + 4.5).toFixed(1)}" font-size="${FS[n.depth]}" fill="${INK[n.depth]}">${esc(n.label)}</text>`);
      }
    }
    parts.push(g.join('\n'));
  }

  const svg = [];
  svg.push(`<svg xmlns="http://www.w3.org/2000/svg" font-family="${FONT}" viewBox="0 0 ${canvasW.toFixed(0)} ${height.toFixed(0)}">`);
  svg.push(`<rect width="100%" height="100%" fill="${BG}"/>`);
  svg.push(parts.join('\n'));
  svg.push(`<rect x="${(midX - rootW / 2).toFixed(1)}" y="${rootCy - 23}" width="${rootW.toFixed(1)}" height="46" rx="13" fill="${ROOT_FILL}"/>`);
  svg.push(`<text x="${midX.toFixed(1)}" y="${(rootCy + 6).toFixed(1)}" font-size="17" font-weight="700" fill="#FFFFFF" text-anchor="middle">${esc(CONF.label)}</text>`);
  svg.push(`<text x="${(canvasW - 24).toFixed(0)}" y="${(height - 22).toFixed(0)}" font-size="11" fill="#93A2B5" text-anchor="end">${esc(CONF.note)}</text>`);
  svg.push('</svg>');

  const out = path.resolve(__dirname, `../../img/${CONF.key}.svg`);
  fs.writeFileSync(out, svg.join('\n'), 'utf8');
  console.log('written:', out, `(${(fs.statSync(out).size / 1024).toFixed(1)} KB)`, `viewBox ${canvasW.toFixed(0)}x${height.toFixed(0)}`);
}

render();
