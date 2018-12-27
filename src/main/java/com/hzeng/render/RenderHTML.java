/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hzeng.render;

import com.hzeng.util.FileReadUtil;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RenderHTML {

    public static String MARKDOWN_CSS;

    static {
        try {
            MARKDOWN_CSS = FileReadUtil.readAll(new File("src/main/resources/css/markdown.css"));
            MARKDOWN_CSS = "<style type = \"text/css\">\n" + MARKDOWN_CSS + "\n</style>\n";
        } catch (IOException e) {
            e.printStackTrace();
            MARKDOWN_CSS = "";
        }
    }

    public static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        // enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));


        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content);
        return renderer.render(document);
    }

    public static MarkdownEntity convertToHTML(String markdown) {
        String html = parse(markdown);
        MarkdownEntity markdownEntity = new MarkdownEntity();
        markdownEntity.setCss(MARKDOWN_CSS);
        markdownEntity.setHtml(html);
        markdownEntity.addDivStyle("class", "markdown-body");
        return markdownEntity;
    }
}
