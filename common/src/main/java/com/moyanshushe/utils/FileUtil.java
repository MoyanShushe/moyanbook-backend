package com.moyanshushe.utils;

import com.moyanshushe.constant.FileConstant;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public class FileUtil {

    public static boolean checkFileNameFormat(String name) {
        return name != null && name.endsWith(FileConstant.WEBP_SUFFIX);
    }

    private FileUtil() {}
}
