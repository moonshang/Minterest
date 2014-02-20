package util;

import java.io.UnsupportedEncodingException; 
import java.net.URLEncoder; 

public class URLEncoding { 
    public static void main(String[] args){ 
        
    } 
    
    public static String encode (String keyword)
    {
        try { 
            String codes=URLEncoder.encode(keyword, "UTF-8"); 
            //System.out.println(codes); 
         
            return codes;
        } catch (UnsupportedEncodingException e) 
        { 
       
            e.printStackTrace(); 
         	return null;
        } 
    	
    }
}
