package collector.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import query.BlockingSiteFactory;
import util.ImageCollectorUtils;
import util.Parameters;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import database.MysqlDatabase;
import entity.WebImageObject;


public class WebPageParser {
	
	static final Logger LOG = Logger.getLogger(WebPageParser.class.getName());
	static final Logger WARN = Logger.getLogger(WebPageParser.class.getName());
	
	public  static ArrayList<WebImageObject> getImageObjectfromPage(String webUrl){
		return parse(webUrl);
	}
	/**
	 * 
	 * @param webUrl
	 * @return null url invalid
	 * 		   arraylist ImageObject
	 * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *         {@link #setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     * @throws IOException if an IO problem occurs
     * @throws MalformedURLException if no URL can be created from the provided string
	 */
	private static ArrayList<WebImageObject> parse(String webUrl) 
//			throws Exception
	{
		ArrayList<WebImageObject> links = new ArrayList<WebImageObject>();
		
		WebClient webClient = setWebClient();
		
		String fixedURL = ImageCollectorUtils.urlDecode(webUrl);
		if(!(fixedURL==null || fixedURL.equals(""))){
			webUrl = fixedURL;
			LOG.info(Thread.currentThread().getName()+" is parsing "+ webUrl+"[after decode]");
		}
		else{
			WARN.warn(Thread.currentThread().getName()+"\t Can not decode URL:"+webUrl);
			return null;
		}
		HtmlPage htmlPage = null;
		try {
			htmlPage = webClient.getPage(webUrl);
		} catch (FailingHttpStatusCodeException e) {
			WARN.warn(Thread.currentThread().getName()+e.getMessage()+"\tthe server returns a failing status code URL="+webUrl);
			return null;
		} catch (MalformedURLException e) {
			WARN.warn(Thread.currentThread().getName()+e.getMessage()+"\tif no URL can be created from the provided string URL="+webUrl);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			WARN.warn(Thread.currentThread().getName()+"\tIOException:url="+webUrl+"\t"+e.getMessage());
			return null;
			
		}
		
		
		Document doc = Jsoup.parse(htmlPage.asXml());
		LOG.info("Finish load jsoup document object...");
		
		String context = doc.body().text();
		String title = doc.title();
		
		Elements imgs = doc.select("img,IMG,Img");
		Elements hrefs = doc.select("a[href]");
		Elements files = doc.select("img[file]"); 
		
		
		for(Element link:imgs){
			String src = link.attr("src");
			if(!isVaidExtension(src))continue;
			try{
				String alt = link.attr("alt");
				String text = context;
				WebImageObject thislink = new WebImageObject(src, alt, text,title);
				links.add(thislink);
			}catch(NullPointerException e){
				WARN.warn(Thread.currentThread().getName()+"\t"+e.getMessage()+"NullPointerException for parsing src"+src);
				continue;
			}
			
		}
		
		for(Element link:hrefs){
			String src = link.attr("abs:href");
			if(!isVaidExtension(src))continue;
			try{
				String alt = link.text();
				String text = context;
				WebImageObject thislink = new WebImageObject(src, alt, text,title);
				links.add(thislink);
			}catch(NullPointerException e){
				WARN.warn(Thread.currentThread().getName()+"\t"+e.getMessage()+"NullPointerException for parsing src"+src);
				continue;
			}
		}
		for(Element link:files){
			String src = link.attr("abs:file");
			if(!isVaidExtension(src))continue;
			try{
				String alt = link.text();
				String text = context;
				WebImageObject thislink = new WebImageObject(src, alt, text,title);
				links.add(thislink);
			}catch(NullPointerException e){
				WARN.warn(Thread.currentThread().getName()+"\t"+e.getMessage()+"NullPointerException for parsing src"+src);
				continue;
			}
		}
		
		//trick to put context back together
//		WebImageObject lastobj = new WebImageObject("", "", context,title);
//		links.add(lastobj);
		
		ArrayList<WebImageObject> results = removeBlockingSite(links);
		return results;
	}
	
	/**
	 * 
	 * @param webClient
	 */
	private static WebClient setWebClient(){
		WebClient webClient = new WebClient();
		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.setJavaScriptEnabled(false);
		webClient.setCssEnabled(false);
		webClient.setAjaxController(new AjaxController());
		webClient.setTimeout(Parameters.WEB_CLIENT_TIMEOUT);
		return webClient;
	}
	
	private static boolean isVaidExtension(String url){
		if(url == null || url.equals(""))return false;
		String extension = ImageCollectorUtils.getURLExtension(url);
		if(extension==null)return false;
		if((extension.equals("jpg"))||(extension.equals("png"))||(extension.equals("jpeg"))||(extension.equals("gif"))||(extension.equals("JPG"))){
			return true;
		}
		else{
			return false;
		}
	}
	
	private static ArrayList<WebImageObject> removeBlockingSite(ArrayList<WebImageObject> webImgObjs){
		BlockingSiteFactory bsf = new BlockingSiteFactory();
		HashSet<String> bSites = bsf.getSites();
		
		ArrayList<WebImageObject> result = new ArrayList<>();
		for(WebImageObject webImgObj:webImgObjs){
			String urlstr = webImgObj.url;
			URL url;
			try {
				url = new URL(urlstr);
				String host = url.getHost();
				if(bSites.contains(host)){
					LOG.info("Site is on the blocking list:"+host);
					continue;
				}
				else{
					result.add(webImgObj);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				continue;
			}
			
		}
		return result;
	}

}
