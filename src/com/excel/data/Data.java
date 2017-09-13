package com.excel.data;

import java.util.ArrayList;

/**
 * 数据 bean
 */
public class Data {
    //文件名
    private String filename;
    //sheet名
    private String sheetName;
    //主键名称
    private Field key;
    //主键组合列
    private ArrayList<Field> keys = new ArrayList<>();
    //字段信息表
    private ArrayList<Field> fields = new ArrayList<>();
    //主键组合列
    private ArrayList<Index> indexKeys = new ArrayList<>();
    //根据第一行数据确定索引组合
    private ArrayList<Index> index = new ArrayList<>();

    private String[][] data;

    private String tableComment;

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename.trim();
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName.trim();
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public ArrayList<Field> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<Field> keys) {
        this.keys = keys;
    }

    public Field getKey() {
        return key;
    }

    public void setKey(Field key) {
        this.key = key;
    }

    public ArrayList<Index> getIndexKeys() {
        return index;
    }

    public void setIndexKeys(ArrayList<Index> indexKeys) {
        this.indexKeys = indexKeys;
    }

}
