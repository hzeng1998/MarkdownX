package com.hzeng.util;

import java.awt.Component;
import java.awt.Container;

public class FindComponent {

    /**
     * 通过组件名,从父级组件沿着递归找到此名字的组件
     *
     * @param c    父级组件
     * @param name 设置的组件名称
     * @return 查找对象
     */
    public static Component searchComponentByName(Container c, String name) {//父级组件,设置的组件名称

        Component result = null;
        Component[] components = c.getComponents();
        if (null != components && components.length > 0) {
            for (Component component : components) {
                String name2 = component.getName();
                if (name2 != null && name2.equals(name)) {
                    result = component;
                    return result;
                } else if (null == result) {//递归调用所有下级组件列表
                    if (component instanceof Container)
                        result = searchComponentByName((Container) component, name);
                }
            }
        }
        return result;

    }

    /**
     * 不使用instanceof 判断是否是同一种类型,
     * 判断目标类型和源类型的字节码对象(Class)是否是同一种类型
     * 通过class类的toString 方法,判断打印出来的类型是否相等,或者直接判断
     *
     * @param c 父级组件
     * @param t 字节码对象
     * @return 查找对象
     */
    private static <T extends Component> Component searchComponentByClass(Container c, Class<T> t) {//泛型方法

        Component result = null;
        Component[] components = c.getComponents();
        if (null != components && components.length > 0) {
            for (Component component : components) {
                //if(component instanceof t){//发现泛型无法作为instanceof右边的对象,无法在运行时将泛型替换为对应的对象
                if (component.getClass().equals(t)) {
                    //if(component.getClass().toString().equals(t.toString()))			和上一行代码效果相同
                    result = component;
                    return result;
                } else if (null == result) {//递归调用所有下级组件列表
                    if (component instanceof Container)
                        result = searchComponentByClass((Container) component, t);
                }
            }
        }
        return result;
    }
}
