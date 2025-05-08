package org.example.DI;

import java.util.HashMap;
import java.util.Map;

/**
 * XML에서 <bean> 태그 하나를 파싱해서 저장하는 클래스
 */
public class BeanDefinition {
    private String id;           // bean의 ID
    private String className;   // bean의 클래스명 (전체 경로)
    private String initMethod;  // 초기화 메서드명 (선택)

    // 의존성 주입 정보: <property name="..." ref="..." />
    private Map<String, String> propertyMap = new HashMap<>();

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void addProperty(String name, String ref) {
        this.propertyMap.put(name, ref);
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
}