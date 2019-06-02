package com.hc.framework.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.File;

/**
 * description：
 * <p/>
 * Created by 曾辉 on 2016/10/24.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * 给电话号码自动添加空格
     */
    public static void phoneAddSpace(final EditText phoneEt) {
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    phoneEt.setText(sb.toString());
                    phoneEt.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 判断是否是合格的手机号码
     */
    public static boolean judgePhoneQual(String number) {
        return number
                .matches("^(\\+86-|\\+86|86-|86){0,1}1[3|4|5|7|8]{1}\\d{9}$");
    }

    //解码
    public static String unescape(String s) {
        if (s.contains("%")) {
            StringBuffer sbuf = new StringBuffer();
            int l = s.length();
            int ch = -1;
            int b, sumb = 0;
            for (int i = 0, more = -1; i < l; i++) {
            /* Get next byte b from URL segment s */
                switch (ch = s.charAt(i)) {
                    case '%':
                        ch = s.charAt(++i);
                        int hb = (Character.isDigit((char) ch) ? ch - '0'
                                : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                        ch = s.charAt(++i);
                        int lb = (Character.isDigit((char) ch) ? ch - '0'
                                : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
                        b = (hb << 4) | lb;
                        break;
                    case '+':
                        b = ' ';
                        break;
                    default:
                        b = ch;
                }
            /* Decode byte b as UTF-8, sumb collects incomplete chars */
                if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
                    sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
                    if (--more == 0)
                        sbuf.append((char) sumb); // Add char to sbuf
                } else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
                    sbuf.append((char) b); // Store in sbuf
                } else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
                    sumb = b & 0x1f;
                    more = 1; // Expect 1 more byte
                } else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
                    sumb = b & 0x0f;
                    more = 2; // Expect 2 more bytes
                } else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
                    sumb = b & 0x07;
                    more = 3; // Expect 3 more bytes
                } else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
                    sumb = b & 0x03;
                    more = 4; // Expect 4 more bytes
                } else /*if ((b & 0xfe) == 0xfc)*/ { // 1111110x (yields 1 bit)
                    sumb = b & 0x01;
                    more = 5; // Expect 5 more bytes
                }
            /* We don't test if the UTF-8 encoding is well-formed */
            }
            s = sbuf.toString();
        }
        return s;
    }


    /**
     * 删除文件
     */
    public static void delete(File paramFile) {
        int i = 0;
        if (paramFile.isFile()) {
            paramFile.delete();
            return;
        }
        if (!paramFile.isDirectory())
            return;
        File[] arrayOfFile = paramFile.listFiles();
        if (arrayOfFile == null) {
            paramFile.delete();
            return;
        }
        int j = arrayOfFile.length;
        while (i < j) {
            delete(arrayOfFile[i]);
            i++;
        }
    }
}
