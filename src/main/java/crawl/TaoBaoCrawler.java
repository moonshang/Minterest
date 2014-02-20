package crawl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import query.KeyWordQueryFactory;
import query.MovieQueryFactory;


import util.URLEncoding;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
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
	KeyWordQueryFactory kqf=new KeyWordQueryFactory("./TaobaoLex");
	MovieQueryFactory mqf=new MovieQueryFactory("./movieList");
	int number_pages=5;
	int number_items=40;
	String src="src";
	String data_src="data-src";
	public static void main(String[] args) 
	{
	
//		ArrayList<String> queryList=new TaoBaoCrawler().getQueryList();
//		for(String e:queryList)
//			System.out.println(e);
    	
		new TaoBaoCrawler().crawl();

	}
	
	public void crawl()
	{

		ArrayList<String> queryList=getQueryList();
		
		
		for(String q:queryList)
		{
			try {
				ArrayList<String> list=crawlWithQuery(q);
				System.out.println(list.size());
				for(String e:list)
					System.out.println(e);
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
			
	}
	
	
	public ArrayList<String> getQueryList() 
	{

		String query_prefix="http://s.taobao.com/search?js=1&q=";
		String query_postfix="&s=";
		ArrayList<String> queryList=new ArrayList<String>();
		for(String q:kqf.getQuery())
		{
			for(int i=0;i<number_pages;i++)
			{
				for(String m:mqf.getQuery())
				{
				String query=query_prefix+URLEncoding.encode(m+q)+query_postfix+i*number_items;
				queryList.add(query);
				}
			}
		}
		return queryList;
	}

	public ArrayList<String> crawlWithQuery(String query) throws FailingHttpStatusCodeException, MalformedURLException, IOException
	{
		ArrayList<String> imageURL=new ArrayList<String>();
		WebClient webClient = new WebClient();
		webClient.setJavaScriptEnabled(false);
    	webClient.setCssEnabled(false);
    	webClient.setRefreshHandler(new ThreadedRefreshHandler());
    	webClient.setAjaxController(new AjaxController());
    	webClient.setTimeout(WEB_CLIENT_TIMEOUT);
    	HtmlPage htmlPage = webClient.getPage(query);
    	Document doc = Jsoup.parse(htmlPage.asXml());
    	//System.out.println(doc);
    	Elements e=doc.select("[class=item-box]");
    	for(Element ele:e)
    	{  
    		System.out.println(ele.text());
    		Elements eles=ele.select("[trace=auction]");
    		if(eles.size()>0)
    		{
    	    System.out.println(eles.get(0).attr("href"));
    	   String image_url = null;
		try {
			image_url = retrievalImageFromSingleLink(eles.get(0).attr("href"));
			System.out.println(image_url);
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		    if(image_url!=null)
    		imageURL.add(image_url);
    	
    		}
    		try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	return imageURL;
    	
	}

	private ArrayList<String> retrievalUsefulLinks()
	{
		
		return null;
	}
	
	private String retrievalImageFromSingleLink(String singleLink) throws NoSuchMethodException, ScriptException, InterruptedException
	{
		try {
			String s=TaoBaoCrawlerSingleWebCrawlerSingleWeb.crawlSinglePage(singleLink,src);
			if(s!=null)
			{
				if(s.length()>2)
				{
					//System.out.println(src);
					return s;
				}
				else
				{
					//System.out.println(data_src);
					s=TaoBaoCrawlerSingleWebCrawlerSingleWeb.crawlSinglePage(singleLink,data_src);
					return s;
				}
			
			}

		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
		
	}
	
	private Boolean imageLocalizer(ArrayList<String> imageList)
	{
		return false;
	}
	
	
}
