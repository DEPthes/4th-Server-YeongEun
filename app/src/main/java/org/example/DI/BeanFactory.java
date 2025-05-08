package org.example.DI;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;

/**
 * BeanDefinition을 바탕으로 객체 생성, 의존성 주입, 초기화 수행
 */
public class BeanFactory {

    private final Map<String, Object> beanMap = new HashMap<>();
    private final Map<String, BeanDefinition> definitionMap = new HashMap<>();

    public void loadBeans(String xmlPath) {
        try {
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(xmlPath);
            if (inputStream == null) {
                throw new FileNotFoundException("classpath:" + xmlPath + " not found");
            }

            List<BeanDefinition> definitions = reader.read(inputStream);

            // 1단계: BeanDefinition 등록
            for (BeanDefinition def : definitions) {
                definitionMap.put(def.getId(), def);
            }
            // 2단계: 객체 생성 (생성자 호출)
            for (BeanDefinition def : definitions) {
                createBean(def);
            }
            // 3단계: 의존성 주입 (setter 또는 field)
            for (BeanDefinition def : definitions) {
                injectDependencies(def);
            }
            // 4단계: 초기화 메서드 호출
            for (Object bean : beanMap.values()) {
                callInitMethods(bean);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("XML 파일을 찾을 수 없습니다: " + xmlPath, e);
        } catch (Exception e) {
            throw new RuntimeException("Bean 로딩 실패: " + xmlPath, e);
        }
    }

    private void createBean(BeanDefinition def) {
        try {
            Class<?> clazz = Class.forName(def.getClassName());
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(def.getId(), instance);
        } catch (Exception e) {
            throw new RuntimeException("Bean 생성 실패: " + def.getId(), e);
        }
    }

    private void injectDependencies(BeanDefinition def) {
        Object bean = beanMap.get(def.getId());
        Class<?> clazz = bean.getClass();

        for (Map.Entry<String, String> entry : def.getPropertyMap().entrySet()) {
            String fieldName = entry.getKey();
            String refId = entry.getValue();
            Object refBean = beanMap.get(refId);
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(bean, refBean);
            } catch (Exception e) {
                throw new RuntimeException("의존성 주입 실패: " + fieldName + " in " + def.getId(), e);
            }
        }
    }

    private void callInitMethods(Object bean) {
        AnnotationBeanProcessor.process(bean); // @PostConstruct 호출

        for (Map.Entry<String, BeanDefinition> entry : definitionMap.entrySet()) {
            if (beanMap.get(entry.getKey()) == bean) {
                String initMethod = entry.getValue().getInitMethod();
                AnnotationBeanProcessor.processInitMethod(bean, initMethod); // init-method 호출
            }
        }
    }

    public Object getBean(String id) {
        return beanMap.get(id);
    }
}