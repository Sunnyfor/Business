package com.zuoyu.business.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * <pre>
 * Function：EditText输入小写自动转为大写
 *
 * Created by JoannChen on 2017/4/28 15:58
 * QQ:411083907
 * E-mail:Q8622268@foxmail.com
 * Copyright Notice：版权所有@ChenYongZuo
 * </pre>
 */
public class CaseSwitchUtil extends ReplacementTransformationMethod {

    //    调用
//    EditText.setTransformationMethod(new CaseSwitchUtil());
    private char[] lowerCase = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private char[] upperCase = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    @Override
    protected char[] getOriginal() {
        return lowerCase;
    }

    @Override
    protected char[] getReplacement() {
        return upperCase;
    }

}
