package com.sogou.upd.passport.common.validation.constraints;

import com.google.common.base.Strings;

import com.sogou.upd.passport.common.filter.HTMLFilter;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检查回调的url是否在sogou.com或sohu.com域下
 * User: shipengzhi
 * Date: 13-6-17
 * Time: 下午6:05
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuValidator implements ConstraintValidator<Ru, String> {

    @Override
    public void initialize(Ru constraintAnnotation) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Strings.isNullOrEmpty(value)) {
            return true;
        }
        HTMLFilter filter = new HTMLFilter();
        String afterFilter = filter.filter(value);
        if (!value.equals(afterFilter)) {
            String temp = afterFilter.replaceAll("\\&amp\\;", "&");
            if (value.equals(temp)) {
                return true;
            }
            return false;
        }
        try {
            // typany.com 为输入法海外版域名
            Pattern p = Pattern.compile("(^(https?:\\/\\/)?[\\w\\-.]+\\.(sogou\\.com|sohu\\.com|qq\\.com|soso\\.com|go2map\\.com|pinyin\\.cn|teemo\\.cn|typany\\.com|joygame8\\.com|59youwan.com)"
                                        + "($|\\?|\\/|\\\\|:[\\d]))|(^(https?:\\/\\/)?+(2288wxy\\.com\\.cn|8888you\\.cn|pr\\.vaf\\.cn)($|\\?|\\/|\\\\|:[\\d]))", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(value);
            return matcher.find();
        } catch (Exception e) {
            return false;
        }

    }

}
