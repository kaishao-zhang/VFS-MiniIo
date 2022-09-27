package com.zyk.xml;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 凯少
 * @create 2022-09-25 22:59
 */
public class ParseXML {
    @Test
    public void parseXml() throws ParserConfigurationException, IOException, SAXException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/applicationContext.xml");
        //创建xml解析工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document document = docBuilder.parse(new InputSource(is));
        //<beans>
        Element root = document.getDocumentElement();
        //获取<beans>中的属性
        String profileSpec = root.getAttribute("profile");
        System.out.println("profileSpec = " + profileSpec);
        String xmlns = root.getAttribute("xmlns");
        System.out.println("xmlns = " + xmlns);
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (StringUtils.isNotBlank(ele.getAttribute("id"))) {
                    String id = ele.getAttribute("id");
                    System.out.println("id = " + id);
                }
                NodeList properties = ele.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node node1 = properties.item(j);
                    if (node1 instanceof Element) {
                        Element ele1 = (Element) node1;
                        if (StringUtils.isNotBlank(ele1.getAttribute("name"))) {
                            String name = ele1.getAttribute("name");
                            System.out.println("name = " +name);
                        }
                        if (StringUtils.isNotBlank(ele1.getAttribute("value"))) {
                            String value = ele1.getAttribute("value");
                            System.out.println("value = " +value);
                        }
                    }
                }
            }
        }
    }
}
