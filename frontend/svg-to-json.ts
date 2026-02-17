import fs from "fs";
import path from "path";
import { XMLParser } from "fast-xml-parser";

type IconJson = Record<
  string,
  {
    width: number;
    height: number;
    path: string;
    stroke: string;
    strokeWidth: string;
    strokeLinecap: string;
    strokeLinejoin: string;
  }
>;

const parser = new XMLParser({
  ignoreAttributes: false,
  attributeNamePrefix: "@_",
  allowBooleanAttributes: true,
});

function numOrDefault(v: unknown, def: number) {
  const n = Number(v);
  return Number.isFinite(n) ? n : def;
}

function pickAttr(obj: any, key: string): string {
  const v = obj?.[`@_${key}`];
  return v == null ? "" : String(v);
}

function toIconJson(svgText: string, iconName: string): IconJson {
  const parsed = parser.parse(svgText);
  const svg = parsed.svg;
  if (!svg) throw new Error("No <svg> root found.");

  const width = numOrDefault(svg["@_width"], 24);
  const height = numOrDefault(svg["@_height"], 24);

  // path는 1개면 객체, 여러 개면 배열
  const rawPaths = svg.path
    ? Array.isArray(svg.path)
      ? svg.path
      : [svg.path]
    : [];

  if (rawPaths.length === 0) {
    throw new Error("No <path> found. (This script expects path-based icons)");
  }

  // ✅ (1) 여러 path의 d를 하나로 합치기
  // - SVG에서 경로 여러 개면 그냥 이어붙여도 대개 정상 렌더링됩니다.
  // - 구분을 위해 공백 하나를 넣어줍니다.
  const combinedD = rawPaths
    .map((p: any) => String(p["@_d"] ?? "").trim())
    .filter(Boolean)
    .join(" ");

  // ✅ (2) stroke 관련 속성은 첫 번째 path 기준으로 가져오되 없으면 기본값
  const first = rawPaths[0] ?? {};
  const stroke = pickAttr(first, "stroke"); // 없으면 ""
  const strokeWidth = pickAttr(first, "stroke-width") || pickAttr(first, "strokeWidth") || "0";
  const strokeLinecap = pickAttr(first, "stroke-linecap") || pickAttr(first, "strokeLinecap") || "";
  const strokeLinejoin = pickAttr(first, "stroke-linejoin") || pickAttr(first, "strokeLinejoin") || "";

  return {
    [iconName]: {
      width,
      height,
      path: combinedD,
      stroke,
      strokeWidth: String(strokeWidth ?? "0"),
      strokeLinecap,
      strokeLinejoin,
    },
  };
}

// -------------------- CLI --------------------
const input = process.argv[2]; // svg 파일 경로
if (!input) {
  console.error("Usage: ts-node svg-to-icon-json.ts <file.svg> [icon-name]");
  process.exit(1);
}
const iconName = process.argv[3] ?? path.basename(input, path.extname(input));

const svgText = fs.readFileSync(input, "utf8");
const out = toIconJson(svgText, iconName);

console.log(JSON.stringify(out, null, 2));
