/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.util;

import java.awt.*;

public class Constrains extends GridBagConstraints {

    public Constrains(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }

    public Constrains(int gridx, int gridy, int gridwidth, int gridheight) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }

    public Constrains setAnchor(int anchor) {

        this.anchor = anchor;
        return this;
    }

    public Constrains setFill(int fill) {
        this.fill = fill;
        return this;
    }

    public Constrains setWeight(double weightx, double weighty) {
        this.weightx = weightx;
        this.weighty = weighty;

        return this;
    }

    public Constrains setInserts(int distance) {
        this.insets = new Insets(distance, distance, distance, distance);
        return this;
    }

    public Constrains setInserts(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public Constrains setIpad(int ipadx, int ipady) {
        this.ipadx = ipadx;
        this.ipady = ipady;
        return this;
    }
}