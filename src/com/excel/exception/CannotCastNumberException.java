package com.excel.exception;

import com.excel.data.Field;

/**
 * 无法转化为数值异常
 *
 * @author ChenLong
 */
public class CannotCastNumberException extends IllegalityExcelFormatException {

    public CannotCastNumberException(String filePath, String sheetName, Field field) {
        super(filePath, sheetName, field);
    }

}
