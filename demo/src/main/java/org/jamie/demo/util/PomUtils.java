package org.jamie.demo.util;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jamie
 * @version 1.0.0
 * @description 获取Pom中的属性
 * @date 2023/7/12 16:03
 */
public class PomUtils {

    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    public static final String VERSION = "version";

    /**
     * <p>
     * Description：获取 pom.xml 中的 properties
     * </p>
     *
     * @param propertyKey properties 中的 key
     * @return properties 中的 value
     */
    public static String getPomProperties(String propertyKey) {
        try (FileReader reader = new FileReader("pom.xml") ) {
            Model model = new MavenXpp3Reader().read(reader);
            return model.getProperties().getProperty(propertyKey);
        } catch (IOException | XmlPullParserException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getPomBaseAttr() {
        Map<String, String> map = new HashMap<>();
        try (FileReader reader = new FileReader("pom.xml") ) {
            Model model = new MavenXpp3Reader().read(reader);
            String groupId = model.getGroupId();
            String artifactId = model.getArtifactId();
            String version = model.getVersion();
            map.put(GROUP_ID, groupId);
            map.put(ARTIFACT_ID, artifactId);
            map.put(VERSION, version);
        } catch (IOException | XmlPullParserException ioe) {
            ioe.printStackTrace();
        }
        return map;
    }
}
