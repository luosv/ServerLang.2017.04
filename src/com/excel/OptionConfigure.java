package com.excel;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionConfigure {

    private static final Logger log = Logger.getLogger(OptionConfigure.class);

    private static final String DEFAULT_CONFIG_PATH = "config/config.xml";
    private static final String FEN_HAO = ";";

    private List<String> excelPaths = new ArrayList<>();
    private List<String> tableCache = new ArrayList<>();
    private List<String> fieldList = new ArrayList<>();
    private List<String> onlyKey = new ArrayList<>();
    private List<String> projectPaths = new ArrayList<>();
    private List<String> langFiles = new ArrayList<>();

    public List<String> getExcelPaths() {
        return excelPaths;
    }

    public void setExcelPaths(List<String> excelPaths) {
        this.excelPaths = excelPaths;
    }

    public List<String> getTableCache() {
        return tableCache;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getOnlyKey() {
        return onlyKey;
    }

    public void setOnlyKey(List<String> onlyKey) {
        this.onlyKey = onlyKey;
    }

    public List<String> getProjectPaths() {
        return projectPaths;
    }

    public void setProjectPaths(List<String> projectPaths) {
        this.projectPaths = projectPaths;
    }

    public List<String> getLangFiles() {
        return langFiles;
    }

    public void setLangFiles(List<String> langFiles) {
        this.langFiles = langFiles;
    }

    public void setTableCache(List<String> tableCache) {
        this.tableCache = tableCache;
    }

    public boolean hasConfig() {
        File xmlFile = new File(DEFAULT_CONFIG_PATH);
        return xmlFile.exists();
    }

    private OptionConfigure() {
        initXML();
    }

    /*
     * DOM4j
     */
    private void initXML() {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File(DEFAULT_CONFIG_PATH));
            Element root = doc.getRootElement();
            readNode(root);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void readNode(org.dom4j.Element root) {
        if (root == null) {
            return;
        }
        // 获取属性
        List<Attribute> attrs = root.attributes();
        if (attrs != null && attrs.size() > 0) {
            for (Attribute attr : attrs) {
                if ("excelPath".equals(attr.getName())) {
                    excelPaths.addAll(Arrays.asList(attr.getValue().split(FEN_HAO)));
                } else if ("table".equals(attr.getName())) {
                    tableCache.add(attr.getValue().toLowerCase());
                } else if ("column".equals(attr.getName())) {
                    fieldList.add(attr.getValue());
                } else if ("key".equals(attr.getName())) {
                    onlyKey.add(attr.getValue());
                } else if ("path".equals(attr.getName())) {
                    projectPaths.addAll(Arrays.asList(attr.getValue().split(FEN_HAO)));
                } else if ("langFile".equals(attr.getName())) {
                    langFiles.addAll(Arrays.asList(attr.getValue().split(FEN_HAO)));
                } else {
                    log.error("不支持有配置选项！名：" + attr.getName());
                }
            }
        }
        // 获取他的节点
        List<Element> childNodes = root.elements();
        for (Element chaidNode : childNodes) {
            readNode(chaidNode);
        }
    }

    /**
     * 用枚举来实现单例
     */
    public static OptionConfigure getInstance() {
        return Singleton.INSTANCE.getExcelManager();
    }

    private enum Singleton {
        INSTANCE;
        OptionConfigure optionReader;

        Singleton() {
            optionReader = new OptionConfigure();
        }

        OptionConfigure getExcelManager() {
            return optionReader;
        }
    }

}
