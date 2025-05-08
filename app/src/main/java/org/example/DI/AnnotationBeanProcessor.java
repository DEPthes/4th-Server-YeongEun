package org.example.DI;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * @PostConstruct 어노테이션이 붙은 메서드를 찾아서 호출해주는 후처리기
 */
public class AnnotationBeanProcessor {

    public static void process(Object bean) {
        Class<?> clazz = bean.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.setAccessible(true);
                    method.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException("@PostConstruct 메서드 호출 실패: " + method.getName(), e);
                }
            }
        }
    }
    public static void processInitMethod(Object bean, String initMethodName) {
        if (initMethodName == null || initMethodName.isEmpty()) return;
        try {
            Method method = bean.getClass().getDeclaredMethod(initMethodName); // 여기서 init()이 되고,
            method.setAccessible(true);
            method.invoke(bean); // 여기서 init() 로그 생성
        } catch (Exception e) {
            throw new RuntimeException("init-method 호출 실패: " + initMethodName, e);
        }
    }
}
