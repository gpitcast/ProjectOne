package com.youyi.ywl.util;

/**
 * Created by Administrator on 2018/9/30.
 *  密码匹配检查工具
 */

public class PswCheckedUtil {

    /**
     *  规则  至少包含大小写字母及数字中的两种
     * @param str
     * @return
     */
    public static boolean isLetterAndDigit(String str){
        boolean isDigit = false;//定义一个boolean值,用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值,用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))){//用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }else if (Character.isLetter(str.charAt(i))){//用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }

        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit&& isLetter && str.matches(regex);
        return isRight;
    }

}

