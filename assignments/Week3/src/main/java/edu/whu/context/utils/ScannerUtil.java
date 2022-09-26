package edu.whu.context.utils;


import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.*;

/**
 * @author myAdministrator
 */

public class ScannerUtil {
    public static Set<Class<?>> getAllClass(String packagename){
        ImmutableSet<ClassPath.ClassInfo> sep = null;
        try {
            sep = ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClassesRecursive(packagename);
        } catch (IOException e) {
            throw new RuntimeException("加载异常");
        }
        Set<Class<?>> classes = new HashSet<>();
        sep.stream().iterator().forEachRemaining(t->{
        try {
             classes.add(Class.forName(String.valueOf(t)));
        } catch (Exception e) {
            throw new RuntimeException("类加载异常");
        }
        });
        return classes;
    }
}