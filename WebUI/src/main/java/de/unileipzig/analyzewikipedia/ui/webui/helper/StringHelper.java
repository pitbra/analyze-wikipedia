/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.helper;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author Pit.Braunsdorf
 */
public class StringHelper {

    public static String unescapeString(String text) {
        return StringEscapeUtils.unescapeJava(text);
    }

    public static String prettyPrintString(String text) {
        String result = unescapeString(text);
        result = result.replace('_', ' ');

        return result;
    }

    public static String ellipsiereString(String text, int length) {
        String result = ellipsisByWord(text, length);
        if (result == null) {
            result = ellipsisByCharacter(text, length);
        }

        return result;
    }

    public static String ellipsisByCharacter(String text, int length) {
        //Muss nicht ellispiert werden
        if (text.length() <= length) {
            return text;
        }

        char[] characters = text.toCharArray();
        String result = "";

        for (int i = 0; i < length; ++i) {
            result += characters[i];
        }
        
        result = result.trim();
        if (!result.equals(text)) {
            result += "...";
        }

        return result;
    }

    public static String ellipsisByWord(String text, int length) {
        String[] arr = text.split(" ");
        String result = "";

        if (arr.length < 1) {
            return null;
        } else if (arr[0].length() > length) {
            return null;
        } else {
            int i = 0;
            while ((i < arr.length) && (result.length() + arr[i].length() < length)) {
                result += arr[i] + ' ';
                ++i;
            }
        }

        result = result.trim();
        if (!result.equals(text)) {
            result += "...";
        }

        return result;
    }
}
