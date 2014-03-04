package collector.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entity.GoogleHtmlObject;
import util.ImageCollectorUtils;

public class GoogleHtmlParser {
	
	static final Logger LOG = Logger.getLogger(GoogleHtmlParser.class.getName());
	static final Logger WARN = Logger.getLogger(GoogleHtmlParser.class.getName());
	
	public static ArrayList<GoogleHtmlObject> getGoogleHtmlItems(String googleHtmlFile){
		return parse(googleHtmlFile);
	}
	
	
	/**
	 * 
	 * @param googleHtmlFile .htm file of Google Image result
	 * @return url for each google result
	 * @throws IOException occurs when error happens to use jsoup parse the google htm file.
	 */
	private static ArrayList<GoogleHtmlObject> parse(String googleHtmlFile)
//			throws IOException
			{
		ArrayList<GoogleHtmlObject> result = new ArrayList<GoogleHtmlObject>();
		String rootFolderPath = ImageCollectorUtils.buildImageRootDir(googleHtmlFile);
		
		LOG.info(Thread.currentThread().getName()+" Start parsing..."+googleHtmlFile);
		
		Document googleImageDoc = null;

		File googleFile = new File(googleHtmlFile);
		try {
			googleImageDoc = Jsoup.parse(googleFile,"UTF-8","");
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			
		}
		
		Element body = googleImageDoc.body();
		Element images = body.getElementsByClass("images_table").get(0);
		Elements rows = images.getElementsByTag("tr");
		
		for (Element row : rows) {
			Elements items = row.getElementsByTag("td");
			for (Element item : items) {// each image in google

				String link;
				String imgsrc;
				String cite;
				String title;
				String height;
				String width;

				if(item.select("a")==null || !item.select("a").first().hasAttr("href")){
					WARN.warn(Thread.currentThread().getName()+"["+item.toString().subSequence(0, 100)+"...]Do not contains <a href> attr...");
					continue;
				}
				else{
					link = item.select("a").first().attr("href");
					if(!link.contains("&") || !link.contains("http")){
						WARN.warn(Thread.currentThread().getName()+"Invalid url:"+link);
						continue;
					}
					else{
						link = link.substring(link.indexOf("http"));
						link = link.substring(0, link.indexOf("&"));
					}
				}

				imgsrc = item.select("img").first().attr("src");

				cite = item.select("cite").first().attr("title");
				
				height = item.select("img").first().attr("height");
				width = item.select("img").first().attr("width");

				title = item.toString();
				title = ImageCollectorUtils.refineGoogleImageTitle(title);
				
				GoogleHtmlObject gItem = new GoogleHtmlObject(imgsrc, link, title, cite,height,width);
				gItem.saveRootDir = rootFolderPath;
				result.add(gItem);
			}
		}
		
		return result;
	}
	
	private static ArrayList<GoogleHtmlObject> removeFromBlockingCite(ArrayList<GoogleHtmlObject> googleObjs){
		ArrayList<GoogleHtmlObject> result = new ArrayList<GoogleHtmlObject>();
		
		return result;
	}
	
}
