/*
 *  Copyright (c) 2018. All Rights Reserved.
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