package collector.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.log4j.PropertyConfigurator;

import collector.parser.ImageStorageManager;
import collector.parser.RideoItemConstructor;
import collector.parser.GoogleHtmlParser;
import collector.parser.TargetImageSelector;
import collector.parser.WebPageParser;
import entity.GoogleHtmlObject;
import entity.WebImageObject;

public class SingleThreadCollector {
	
//	public static void start(String itemPath) throws FileNotFoundException{
//		File dir = new File(itemPath);
//		File[] htmls = dir.listFiles();
//		for(File html:htmls){
//			if(html.isDirectory())continue;
//			else if(!html.getName().endsWith("htm")&&(!html.getName().endsWith("html")))continue;
//			else{
//				String htmlPath = html.getAbsolutePath();
//				GoogleImageCollector collector = new GoogleImageCollector();
//				collector.pipeLine(htmlPath);
//			}
//			
//		}
//	}
	
	public static void start(String itemPath,String movie_id,String keyword) throws FileNotFoundException{
		PropertyConfigurator.configure("log4j.properties");
		File dir = new File(itemPath);
		File[] htmls = dir.listFiles();
		for(File html:htmls){
			if(html.isDirectory())continue;
			else if(!html.getName().endsWith("htm")&&(!html.getName().endsWith("html")))continue;
			else{
				String htmlPath = html.getAbsolutePath();
				ArrayList<GoogleHtmlObject> googleHtmlObjects = GoogleHtmlParser.getGoogleHtmlItems(htmlPath);
				for(GoogleHtmlObject googleHtmlObj:googleHtmlObjects){
					String webUrl = googleHtmlObj.webUrl;
					
					ArrayList<WebImageObject> webImageObjects = WebPageParser.getImageObjectfromPage(webUrl);
					TargetImageSelector targetImage = new TargetImageSelector(googleHtmlObj, webImageObjects);
					RideoItemConstructor rideoItemConstructor = new RideoItemConstructor(targetImage, movie_id, keyword);
					ImageStorageManager storageManager = new ImageStorageManager(rideoItemConstructor.getRideoItem());
					storageManager.save2Local();
					
				}
			}
			
		}
	}
	
	public static void main(String args[]){
		String path = "e:/crawlerdata/quanzhixian/";
		try{
//			String path = args[0];
			SingleThreadCollector.start(path,"movie_id","keywordtest");
		}catch(FileNotFoundException e){
			e.printStackTrace();
			
		}
	}
	

}
