package crawl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TaoBaoCrawlerSingleWebCrawlerSingleWeb {
	private static final int WEB_CLIENT_TIMEOUT = 1000*30;
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		String q="http://item.taobao.com/item.htm?id=35633049435";
		String att="data-src";
		try {
			String s=TaoBaoCrawlerSingleWebCrawlerSingleWeb.crawlSinglePage(q,att);
			System.out.println(s);
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
	/*
	 * attr could be src or data-src
	 */
	public static String crawlSinglePage(String singleLink,String attr) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException 
	{
		WebClient webClient = new WebClient();
		webClient.setJavaScriptEnabled(false);
    	webClient.setCssEnabled(false);
    	webClient.setRefreshHandler(new ThreadedRefreshHandler());
    	//webClient.setAjaxController(new AjaxController());
    	webClient.addRequestHeader("Referer", singleLink);
    	webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    //	webClient.setTimeout(WEB_CLIENT_TIMEOUT);
    	HtmlPage	htmlPage = webClient.getPage(singleLink);
    	Thread.sleep(10000);
    	Document doc = Jsoup.parse(htmlPage.asXml());
   
    //	System.out.println(doc.html());
    	//System.out.println(singleLink);
		Elements single_eles=doc.select("[id=J_ImgBooth]");
	
		
		if(single_eles.size()>0)
		{
	//	System.out.println(single_eles.get(0));
		//System.out.println(single_eles.get(0).html());
	//	System.out.println(single_eles.get(0).attr(attr));
		return single_eles.get(0).attr(attr);
		}
		else
			return null;
	
	}

}
