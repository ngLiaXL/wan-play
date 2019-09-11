package com.ldroid.kwei.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.annotations.NonNull;

public class MethodUtils {

    private MethodUtils() {

    }

    public static void runAnnotatedMethods(int requestCode, @NonNull Object object, Object[] parameter) {
        Class clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MethodAnnotation.class)) {
                MethodAnnotation ann = method.getAnnotation(MethodAnnotation.class);
                if (ann.value() == requestCode) {
                    try {
                        // Make method accessible if private
                        if (!method.isAccessible()) {
                            method.setAccessible(true);
                        }
                        if (method.getParameterTypes().length > 0) {
                            method.invoke(object, parameter);
                        } else {
                            method.invoke(object);
                        }

                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
            }
        }
    }
}
