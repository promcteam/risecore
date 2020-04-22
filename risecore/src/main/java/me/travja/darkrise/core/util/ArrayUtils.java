package me.travja.darkrise.core.util;

import java.util.ArrayList;

public class ArrayUtils {

    public static <T> ArrayList<T> toArray(T... objs) {
        ArrayList<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

}
