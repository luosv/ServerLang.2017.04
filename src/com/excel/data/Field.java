package com.excel.data;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Administrator
 */
public class Field {

    private String keyTag;

    private String name;

    private String className;

    private String javaClassName;

    private String dbClassName;

    private int cell;

    private String explain;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getKeyTag() {
        return keyTag;
    }

    public void setKeyTag(String keyTag) {
        this.keyTag = keyTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public String getDbClassName() {
        return dbClassName;
    }

    public void setDbClassName(String dbClassName) {
        this.dbClassName = dbClassName;
    }

}
