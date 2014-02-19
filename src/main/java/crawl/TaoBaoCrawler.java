package crawl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

import util.URLEncoding;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/*
 * Tao Bao Crawler used to crawl all objects from Taobao
 * Lifan Guo
 * 2014/2/17
 * 
 */
public class TaoBaoCrawler {

	private static final int WEB_CLIENT_TIMEOUT = 1000*30;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String url="http://s.taobao.com/search?initiative_id=staobaoz_20140218&js=1&q=";
		String q=URLEncoding.encode("来自星星的你同款");
		url=url+q+"&stats_click=search_radio_all%3A1";
		WebClient webClient = new WebClient();
		webClient.setJavaScriptEnabled(false);
    	webClient.setCssEnabled(false);
    	webClient.setAjaxController(new AjaxController());
    	webClient.setTimeout(WEB_CLIENT_TIMEOUT);
    	
    	try {
    		
    		System.out.println(url);
			HtmlPage	htmlPage = webClient.getPage(url);
			
			BufferedWriter writer=new BufferedWriter(new FileWriter("D:\\TCLFiles\\WeiboNews\\JScrawler\\example.html"));
			writer.write(htmlPage.asXml());
			writer.flush();
			writer.close();
		//	System.out.println(htmlPage.asXml());
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void crawl()
	{
		
		
	}

}
