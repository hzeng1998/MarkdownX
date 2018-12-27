/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hzeng.util;

import java.io.*;

import java.nio.charset.StandardCharsets;

public class FileReadUtil {

    public static String readAll(File currentFile) throws IOException {

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(currentFile));

        byte[] bytes = new byte[4096];

        int len;
        StringBuilder stringBuilder = new StringBuilder();
        while ((len = bufferedInputStream.read(bytes, 0, bytes.length)) != -1) {
            stringBuilder.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        }

        bufferedInputStream.close();

        return stringBuilder.toString();
    }
}