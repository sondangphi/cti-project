
package az.encoding.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlCoding {
    public static String encode(String uni) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<uni.length(); i++) {
            char c = uni.charAt(i);
            
            if (c <= 127) {
                sb.append(c);
            } else {
                sb.append("&#").append((int)c).append(";"); 
            }
        }
        
        return sb.toString();
    }
    
    public static String decode(String html) {
        Pattern pttr = Pattern.compile("&#(?<uni>\\d+);");
        Matcher match = pttr.matcher(html);
        while (match.find()) {
            int uni = Integer.parseInt(match.group("uni"));
            html = html.replace("&#"+uni+";", new String(new char[] { ((char)uni) }));
            match = pttr.matcher(html);
        }
        
        return html;
    }
}
