/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.file;

import lombok.Data;

import java.io.Serializable;

@Data
public class Operation implements Serializable {

    public Operation(Method method, String value) {
        this.method = method;
        this.value = value;
    }

    private Method method;
    private String value;
}
