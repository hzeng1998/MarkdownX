/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hzeng.render;

import lombok.Data;
import org.w3c.dom.Document;

import java.awt.*;

@Data
public class RenderHTMLOptions {

    private Integer imgWidth;

    private Integer imgHeight;

    private boolean autoWidth;

    private boolean autoHeight;

    private String outType;

    private Document document;

    private Font font;

    private String fontColor;

}
