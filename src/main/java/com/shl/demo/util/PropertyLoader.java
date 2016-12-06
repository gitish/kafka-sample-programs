package com.shl.demo.util;

import java.io.InputStream;
import java.util.Properties;

import com.google.common.io.Resources;

public class PropertyLoader {
    public static Properties loadProperties(String propetyFile){
        Properties properties=null;
        try (InputStream props = Resources.getResource(propetyFile)
                .openStream()) {
            properties = new Properties();
            properties.load(props);
        }catch(Exception e){
            e.printStackTrace();
        }
        return properties;
    }
}
