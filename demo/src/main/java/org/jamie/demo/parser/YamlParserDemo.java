package org.jamie.demo.parser;

import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EchoJamie
 * @version 1.0.0
 * @description Yaml 转 Map -> Properties
 * @date 2023/4/21 00:47
 */
public class YamlParserDemo {

    public static final Yaml parser = new Yaml();

    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("/Users/jamie/namespace/spring-demo/demo/src/main/resources/application.yml")) {
        //try (FileInputStream fileInputStream = new FileInputStream("/Users/jamie/namespace/work/defanyun-apaas-private-run/xdap-integration/src/main/resources/application-integration-local.yml")) {
            Map map = parser.loadAs(fileInputStream, Map.class);
            Map test = yamlToProps(map);
            test.forEach((k, v) -> {
                System.out.println(k + "=" + v);
            });
        } catch (FileNotFoundException fnfe) {

        } catch (IOException ioe) {

        }
    }


    public static Map<String, String> yamlToProps(Map yaml) {
        return yamlMapToProps(yaml, null);
    }
    private static Map<String, String> yamlMapToProps(Map yaml, String parentKey) {
        Map<String, String> result = new LinkedHashMap<>();
        yaml.forEach((k,v) -> {
            if (v instanceof Map) {
                yamlMapToProps((Map) v, (String) k).forEach((subK, subV) -> {
                    result.put(getKey(subK, parentKey), subV);
                });
            } else {
                // 最后一层
                if (v instanceof List) {
                    List subV = (List)v;
                    for (int i = 0; i < subV.size(); i++) {
                        if (subV.get(i) instanceof Map) {
                            result.putAll(yamlMapToProps((Map) subV.get(i), getKey((String) k, parentKey) + "[" + i + "]"));
                        } else {
                            // 逗号模式
                            result.put(getKey((String) k, parentKey), String.join(",", (List)v));
                            // 方括号模式
                            //result.put(getKey((String) k, parentKey) + "[" + i + "]", subV.get(i).toString());
                        }
                    }
                } else {
                    String value = v != null ? v.toString() : "";
                    result.put(getKey((String) k, parentKey), value.toString());
                }
            }
        });
        return result;
    }

    private static String getKey(String key, String parentKey) {
        return StringUtils.isEmpty(parentKey) ? key.toString() : parentKey + "." + key.toString();
    }
}
