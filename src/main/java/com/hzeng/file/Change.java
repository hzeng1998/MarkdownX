/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.file;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/30 20:22
 */

@Data
public class Change implements Serializable {

    private ArrayList<Operation> operations;

    boolean send;

    public Change(ArrayList<Operation> operations, boolean send) {
        this.operations = operations;
        this.send = send;
    }

    public Change() {
        this.operations = new ArrayList<>();
        this.send = true;
    }

    public void transformation(Change beforeChange) {

        int operationLen = operations.size();
        int beforeChangeLen = beforeChange.operations.size();

        int beforePos = 0, pos = 0;
        int count = 0; // change number
        int indexChange = 0, index = 0;

        while (indexChange < beforeChangeLen && index < operationLen) {

            Operation beforeOperation = beforeChange.operations.get(indexChange);
            Operation operation = operations.get(index);

            if (beforeOperation.getMethod() == Method.DELETE) {
                count = -(beforeOperation.getValue().length());
                beforePos += beforeOperation.getValue().length();
            }

            if (beforeOperation.getMethod() == Method.INSERT) {
                count = (beforeOperation.getValue().length());
            }

            if (beforeOperation.getMethod() == Method.RETAIN) {
                beforePos += Integer.valueOf(beforeOperation.getValue());
            }

            if (operation.getMethod() == Method.DELETE) {
                pos += operation.getValue().length();
            }

            if (operation.getMethod() == Method.RETAIN) {
                pos += Integer.valueOf(operation.getValue());
                if (pos >= beforePos) {
                    operation.setValue(String.valueOf(Integer.valueOf(operation.getValue()) + count));
                    indexChange++;
                    count = 0;
                } else {
                    index++;
                }
            }

            if (operation.getMethod() == Method.DELETE) {
                pos += operation.getValue().length();
                index++;
            }

            if (operation.getMethod() == Method.INSERT) {
                index++;
            }
        }
    }

    public void insertOperations(Change newChange) {

        int pos = 0, index = 0;

        if (operations.isEmpty()) {
            operations.addAll(newChange.operations);
            return;
        }

        for (; index <operations.size(); index++) {
            Operation operation = operations.get(index);

            if (operation.getMethod() == Method.RETAIN) {
                pos += Integer.valueOf(operation.getValue());
            }
            if (operation.getMethod() == Method.INSERT) {
                pos += operation.getValue().length();
            }

            if (pos == Integer.valueOf(newChange.operations.get(0).getValue())) {
                operations.add(index, newChange.operations.get(1));
                break;
            }
        }
    }

    public void mergeOperations() {

        for (int index = 1; index < operations.size(); index++) {

            if (operations.get(index).getMethod() == operations.get(index - 1).getMethod()) {
                operations.get(index - 1).setValue(operations.get(index).getValue() + operations.get(index - 1).getValue());
                operations.remove(index);
                index--;
            }

            if (operations.get(index).getMethod() == Method.DELETE && operations.get(index + 1).getMethod() == Method.INSERT) {
                Operation del = operations.get(index);
                Operation ins = operations.get(index + 1);
                operations.remove(del);
                operations.remove(ins);
                index--;
            }
        }

    }

/*
    public void insertOperations(Change newChange) {

        int operationLen = operations.size();
        int newChangeLen = newChange.operations.size();

        ArrayList<Operation> mergeOpeations = new ArrayList<Operation>();

        int newPos = 0, pos = 0;
        int indexChange = 0, index = 0;

        while (indexChange < newChangeLen && index < operationLen) {

            Operation newOperation = newChange.getOperations().get(indexChange);
            Operation operation = operations.get(index);

            if (newOperation.getMethod() == Method.RETAIN) {
                newPos += Integer.valueOf(newOperation.getValue());
            }

            if (operation.getMethod() == Method.RETAIN) {
                pos += Integer.valueOf(operation.getValue());
            }

            if (operation.getMethod() == Method.INSERT) {
                pos += operation.getValue().length();
            }

            if(newOperation.getMethod() == Method.INSERT || newOperation.getMethod() == Method.DELETE) {
                if (pos > newPos && operation.getMethod() == Method.RETAIN) {
                    mergeOpeations.add(new Operation(Method.RETAIN, String.valueOf(newPos)));
                    mergeOpeations.add(newOperation);

                    operations.remove(index);
                    operations.add(index, new Operation(Method.RETAIN, String.valueOf(pos - newPos)));
                  //  mergeOpeations.add(new Operation(Method.RETAIN, String.valueOf(pos - newPos)));

                    indexChange++;
                    //index++;
                } else if (pos >= newPos && operation.getMethod() == newOperation.getMethod()) {

                    StringBuilder stringBuilder = new StringBuilder(operation.getValue());
                    stringBuilder.insert(operation.getValue().length() - pos + newPos, newOperation.getValue());
                    //mergeOpeations.add(new Operation(Method.INSERT, stringBuilder.toString()));
                    operation.setValue(stringBuilder.toString());
                    //index++;
                    indexChange++;
                } else {

                    mergeOpeations.add(operation);
                    index++;
                }
            }
        }

        operations = mergeOpeations;
    }
    */
}
