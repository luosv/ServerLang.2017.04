package com.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelLists {

    private static final Log log = LogFactory.getLog(ExcelLists.class);

    public static List<String> getFileList() {
        List<String> excelPaths = OptionConfigure.getInstance().getExcelPaths();
        List<String> excelList = new ArrayList<>();
        for (String excelPath : excelPaths) {
            File dir = new File(excelPath);
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    if (child.startsWith("~")) {
                        // Excel打开时的临时文件
                        continue;
                    }
                    // 文件名
                    Pattern pattern = Pattern.compile("(.+).xls(.+)");
                    Matcher matcher = pattern.matcher(child);
                    if (matcher.find()) {
                        excelList.add(excelPath + "\\" + matcher.group(0));
                    }
                }
            } else {
                log.error("错误！children == null");
            }
        }
        return excelList;
    }

}
