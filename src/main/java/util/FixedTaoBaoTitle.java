package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixedTaoBaoTitle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String fixTaoBaoTitle(String title){
		Pattern prefix = Pattern.compile("举报 [0-9]+\\s家店在售此款");
		Matcher matcher = prefix.matcher(title);
		title = matcher.replaceAll("");
		 
		Pattern ptitle = Pattern.compile("(?!=举报 [0-9]+\\s).+(￥\\d+\\.\\d{2})");
		matcher = ptitle.matcher(title);
		
		
		while(matcher.find()){
			title = matcher.group(0);
		}
		title = title.replaceAll("举报", "");
		if(title!=null)title = title.trim();
		if(title.startsWith("@"))title = title.substring(1);
		return title;
	}

}
