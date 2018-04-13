package com.feature.projectone.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/4/8.
 * 手机号码验证工具类
 */

public class PhoneNumberCheckedUtil {

    /**
     * 检验一个手机号是否为真实的手机号码
     *
     * @param number
     * @return
     */
    public static boolean checkNumber(String number) {
        Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
