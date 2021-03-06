/* eslint-disable */
import { Heading } from "tiptap-extensions";

export class TocHeading extends Heading {
  get schema() {
    return {
      attrs: {
        level: {
          default: 1
        },
        id: {
          default: ""
        }
      },
      content: "inline*",
      group: "block",
      defining: true,
      draggable: false,
      parseDOM: this.options.levels.map(level => {
        return {
          tag: "h".concat(level),
          getAttrs: dom => ({
            level: level
          })
        };
      }),
      toDOM: node => {
        let id = node.attrs.id || getUuid();
        let dom = [
          "h".concat(node.attrs.level),
          {
            id: id
          },
          0
        ];
        node.attrs.id = id;
        tocData.push({ id: id, data: node.content.content[0].text, level: node.attrs.level });
        return dom;
      }
    };
  }
}
export let tocData = [];

export function resetTocData() {
  tocData = [];
}
function getUuid() {
  let s = [];
  let hexDigits = "0123456789abcdef";
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = ""; // -

  return s.join("").substr(0, 6);
}
