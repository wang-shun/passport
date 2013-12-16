package com.sogou.upd.passport.oauth2.common.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sogou.upd.passport.oauth2.common.exception.HTMLTextParseException;

import java.util.Map;

public class HTMLTextUtils {

    public static Map<String, Object> parseHTMLText(String text) throws HTMLTextParseException {

        Map<String, Object> params = Maps.newHashMap();
        try {
            if (!Strings.isNullOrEmpty(text)) {
                String[] paramsArray = text.split("&");
                for (String value : paramsArray) {
                    String[] result = value.split("=");
                    params.put(result[0], result[1]);
                }
            }
        } catch (Exception e) {
            throw new HTMLTextParseException(e);
        }
        return params;
    }
}
