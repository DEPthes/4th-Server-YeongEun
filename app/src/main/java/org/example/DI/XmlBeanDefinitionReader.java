package org.example.DI;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * XML 파일을 읽어 BeanDefinition 리스트를 만들어주는 클래스
 */
public class XmlBeanDefinitionReader {

    public List<BeanDefinition> read(InputStream inputStream) {
        List<BeanDefinition> definitions = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            Element root = doc.getDocumentElement();
            NodeList beans = root.getElementsByTagName("bean");

            for (int i = 0; i < beans.getLength(); i++) {
                Element bean = (Element) beans.item(i);
                String id = bean.getAttribute("id");
                String className = bean.getAttribute("class");
                String initMethod = bean.getAttribute("init-method");

                BeanDefinition def = new BeanDefinition(id, className);
                if (!initMethod.isEmpty()) {
                    def.setInitMethod(initMethod);
                }

                NodeList properties = bean.getElementsByTagName("property");
                for (int j = 0; j < properties.getLength(); j++) {
                    Element prop = (Element) properties.item(j);
                    String name = prop.getAttribute("name");
                    String ref = prop.getAttribute("ref");
                    def.addProperty(name, ref);
                }
                definitions.add(def);
            }
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 실패: " + e.getMessage(), e);
        }
        return definitions;
    }
}