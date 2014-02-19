package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;



/**
 * a converter from Chinese character to pinyin
 * code from web
 * @author ys439
 *
 */
public class FileName2Pinyin {
	String pinyinpath;
	String originalpath;
	
	public static String convertHanzi2PinyinStr(String hanzistr){
		char[] hanzi = hanzistr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(char c:hanzi){
			if(isHanZi(c)){
				String pinyin41char = PinyinHelper.toHanyuPinyinStringArray(c)[0];
				sb.append(pinyin41char.substring(0, pinyin41char.length()-1));//capitalize
			}
			else if(Character.isLetterOrDigit(c)){
				sb.append(c);
			}
			else{
//				sb.append("_");
			}
		
		}
		String ret = sb.toString().replaceAll(":", "");
		return ret;
	}
	
	/**
	 * verify if a ch is a Chinese char
	 * @param ch
	 * @return
	 */
	public static boolean isHanZi(char ch) {
		return (ch >= 0x4E00 && ch <= 0x9FA5);

	}
	
	/**
	 * 判断字符串是否是乱码
	 *
	 * @param strName 字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String strName) {
	    Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
	    Matcher m = p.matcher(strName);
	    String after = m.replaceAll("");
	    String temp = after.replaceAll("\\p{P}", "");
	    char[] ch = temp.trim().toCharArray();
	   // float chLength = ch.length;
	    float count = 0;
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (!Character.isLetterOrDigit(c)) {
	            if (!isHanZi(c)) {
	                count = count + 1;
	            }
	        }
	    }
	    if (count>1) {
	        return true;
	    } else {
	        return false;
	    }
	 
	}
	
	public static void main(String[] args){
		String orig = "小时代 女装 天猫 - Google Search";
//		String orig = "what";
//		char c = '小';
//		String[] res = PinyinHelper.toHanyuPinyinStringArray(c);
		System.out.println(convertHanzi2PinyinStr(orig));
		
	}

}
