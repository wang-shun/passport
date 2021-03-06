package com.sogou.upd.passport.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Title: 私有方法调用工具类
 * Description:利用java反射调用类的的私有方法
 * User: shipengzhi
 * Date: 14-7-26
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class ReflectUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 利用递归找一个类的指定方法，如果找不到，去父亲里面找直到最上层Object对象为止。
     *
     * @param clazz      目标类
     * @param methodName 方法名
     * @param classes    方法参数类型数组
     * @return 方法对象
     * @throws Exception
     */
    public static Method getMethod(Class clazz, String methodName,
                                   final Class[] classes) throws Exception {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }

    /**
     * @param obj        调整方法的对象
     * @param methodName 方法名
     * @param classes    参数类型数组
     * @param objects    参数数组
     * @return 方法的返回值
     */
    public static Object invoke(final Object obj, final String methodName,
                                final Class[] classes, final Object[] objects) {
        try {
            Method method = getMethod(obj.getClass(), methodName, classes);
            method.setAccessible(true);// 调用private方法的关键一句话
            return method.invoke(obj, objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(final Object obj, final String methodName,
                                final Class[] classes) {
        return invoke(obj, methodName, classes, new Object[]{});
    }

    /**
     * 无参数方法
     *
     * @param obj        调整方法的对象
     * @param methodName 方法名
     * @return 方法的返回值
     */
    public static Object invoke(final Object obj, final String methodName) {
        return invoke(obj, methodName, new Class[]{}, new Object[]{});
    }

    /**
     * 初始化带参数构造器
     * @param clazz
     * @param paramsTypes
     * @param paramValues
     * @return
     */
    public static Object instantiateClassWithParameters(Class clazz, Class[] paramsTypes,
                                                        Object[] paramValues) throws Exception {
        try {
            if (paramsTypes != null && paramValues != null) {
                if (!(paramsTypes.length == paramValues.length)) {
                    throw new IllegalArgumentException(
                            "Number of types and values must be equal");
                }

                if (paramsTypes.length == 0 && paramValues.length == 0) {
                    return clazz
                            .newInstance();
                }
                Constructor clazzConstructor = clazz.getConstructor(paramsTypes);
                return clazzConstructor.newInstance(paramValues);
            }
            return clazz.newInstance();
        } catch (NoSuchMethodException e) {
            log.error("Instantiate Class With Parameters NoSuchMethodException! Class:" + clazz.getName(), e);
            throw new Exception();
        } catch (InstantiationException e) {
            log.error("Instantiate Class With Parameters InstantiationException! Class:" + clazz.getName(), e);
            throw new Exception();
        } catch (IllegalAccessException e) {
            log.error("Instantiate Class With Parameters IllegalAccessException! Class:" + clazz.getName(), e);
            throw new Exception();
        } catch (InvocationTargetException e) {
            log.error("Instantiate Class With Parameters InvocationTargetException! Class:" + clazz.getName(), e);
            throw new Exception();
        }
    }

    /**
     * 测试反射调用
     *
     * @param args
     */
    public static void main(String[] args) {
        invoke(new B(), "printlnA", new Class[]{String.class},
                new Object[]{"test"});
        invoke(new B(), "printlnB");
    }
}

class A {
    private void printlnA(String s) {
        System.out.println(s);
    }
}

class B extends A {
    private void printlnB() {
        System.out.println("b");
    }
}