/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hzeng.render;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class MarkdownEntity {

    public static String TAG_WIDTH = "<style type=\"text/css\"> %s { width:85%%} </style>";

    // css 样式
    private String css;

    // 最外网的div标签， 可以用来设置样式，宽高，字体等
    private Map<String, String> divStyle = new ConcurrentHashMap<>();

    // 转换后的html文档
    private String html;

    public MarkdownEntity() {
    }

    public MarkdownEntity(String html) {
        this.html = html;
    }

    @Override
    public String toString() {
        return  "\n<style>" + css + "\n</style>"  + "\n<body " + parseDiv() + ">\n" + html + "\n</body>";
    }


    private String parseDiv() {

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : divStyle.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
        }
        return stringBuilder.toString();
    }


    public void addDivStyle(String attrKey, String value) {
        if (divStyle.containsKey(attrKey)) {
            divStyle.put(attrKey, divStyle.get(attrKey) + " " + value);
        } else {
            divStyle.put(attrKey, value);
        }
    }


    public void addWidthCss(String tag) {
        String wcss = String.format(TAG_WIDTH, tag);
        css += wcss;
    }
}
