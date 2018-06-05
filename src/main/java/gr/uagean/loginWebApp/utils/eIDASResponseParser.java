/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uagean.loginWebApp.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nikos
 */
public class eIDASResponseParser {

    private final static Pattern namePattern = Pattern.compile("friendlyName='(.*?)'");
    private final static Pattern valuePattern = Pattern.compile("=\\[(.*?)\\]");

    public static Map<String, String> parse(String eIDASResponse) throws IndexOutOfBoundsException {
        Map<String, String> result = new HashMap();
        String attributePart = eIDASResponse.split("attributes='")[1];
        String[] attributesStrings = attributePart.split("AttributeDefinition");

        Arrays.stream(attributesStrings).filter(string -> {
            return string.indexOf("=") > 0;
        }).filter(string -> {
            return namePattern.matcher(string).find();
        }).forEach(attrString -> {
//            String value = attrString.split("=\\[(.*?)\\]")[1].replace("[", "").replace("]", "");

            Matcher nameMatcher = namePattern.matcher(attrString);
            Matcher valueMatcher = valuePattern.matcher(attrString);

            if (valueMatcher.find() && nameMatcher.find()) {
                String name = nameMatcher.group(1);

                char c[] = name.toCharArray();
                c[0] = Character.toLowerCase(c[0]);
                name = new String(c);

                String value = valueMatcher.group(1);
                result.put(name, value);
                
                if(name.equals("personIdentifier")){
                    result.put("eid", value);
                }
            }
        });

        return result;
    }

}
