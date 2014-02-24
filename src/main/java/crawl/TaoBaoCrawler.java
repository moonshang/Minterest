package crawl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import query.KeyWordQueryFactory;
import query.MovieQueryFactory;


import util.FileCreator;
import util.FileName2Pinyin;
import util.URLEncoding;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import database.MysqlDatabase;

import entity.TaoBaoItem;

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
	int number_pages=8;
	int number_items=40;
	String src="src";
	String data_src="data-src";
	MysqlDatabase msd=new MysqlDatabase();
	String root="/mnt/nfs/nas179/rideo/";
	String taobao="taobao";
	String winpath="D:/taobao/";
	public static void main(String[] args) 
	{
	
//		ArrayList<String> queryList=new TaoBaoCrawler().getQueryList();
//		for(String e:queryList)
//			System.out.println(e);
    	
		new TaoBaoCrawler().crawl();

	}
	
	
	public void crawl()
	{
		String query_prefix="http://s.taobao.com/search?js=1&q=";
		String query_postfix="&s=";
		
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();                               
		System.out.println(sf.format(date));  
		
		for(String m:mqf.getQuery())
		{
			String movie_id=msd.getMId(m);
			System.out.println(m);
			System.out.println(movie_id);
			String movie_name=m;
			
			for(String q:kqf.getQuery())
			{
				for(int i=0;i<number_pages;i++)
				{
					String query=query_prefix+URLEncoding.encode(m+q)+query_postfix+i*number_items;

					System.out.println(query);
					ArrayList<TaoBaoItem> tbiL=crawl(query,movie_name,movie_id);
					
                    
					String outpath=root+FileName2Pinyin.convertHanzi2PinyinStr(m)+"/"+taobao+"/"+FileName2Pinyin.convertHanzi2PinyinStr
							(q)+"/"+sf.format(date)+"/";
					
					String wpath=winpath+FileName2Pinyin.convertHanzi2PinyinStr(m)+"/"+taobao+"/"+FileName2Pinyin.convertHanzi2PinyinStr
							(q)+"/"+sf.format(date)+"/";
					FileCreator.createFile(wpath);
					imageLocalizer(tbiL,wpath,outpath);
					
					for(TaoBaoItem item:tbiL)
					{
						System.out.println(item.get_alt());
						System.out.println(item.get_url());
						System.out.println(item.get_localAdd());
						
					}
					msd.insertDataCollectionintoMysql(tbiL);
					
				}
			}
			
		}
		
		
		
		

	}
	
	public ArrayList<TaoBaoItem> crawl(String query, String movie_Name, String movie_id)
	{

				try {
					ArrayList<TaoBaoItem> list=crawlWithQuery(query,movie_Name,movie_id);
					return list;
				} catch (FailingHttpStatusCodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			
	}
	
	
	

	public ArrayList<TaoBaoItem> crawlWithQuery(String query, String movie_name,String movie_id) 
	{
		ArrayList<TaoBaoItem> taoBaoList=new ArrayList<TaoBaoItem>();
		WebClient webClient = new WebClient();
		webClient.setJavaScriptEnabled(false);
    	webClient.setCssEnabled(false);
    	webClient.setRefreshHandler(new ThreadedRefreshHandler());
    	webClient.setAjaxController(new AjaxController());
    	webClient.setTimeout(WEB_CLIENT_TIMEOUT);
    	HtmlPage htmlPage=null;
		try {
			htmlPage = webClient.getPage(query);
		} catch (FailingHttpStatusCodeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    	Document doc = Jsoup.parse(htmlPage.asXml());
    	//System.out.println(doc);
    	Elements e=doc.select("[class=item-box]");
    	for(Element ele:e)
    	{  
    		//System.out.println(ele.text());
    		Elements eles=ele.select("[trace=auction]");
    		if(eles.size()>0)
    		{
    	   // System.out.println(eles.get(0).attr("href"));
    	   String image_url = null;
		try {
			image_url = retrievalImageFromSingleLink(eles.get(0).attr("href"));
			//System.out.println(image_url);
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
		    	
		    {
    	
		   TaoBaoItem item= new TaoBaoItem();
		   item.set_url(image_url);
		   item.set_title(ele.text());
		   item.set_soruce(eles.get(0).attr("href"));
		   item.set_Movie_Name(movie_name);
		   item.set_movie_id(movie_id);
		   item.set_from(eles.get(0).attr("href"));
		   item.set_alt(ele.text());
		
		   taoBaoList.add(item);
		   System.out.println(item.get_url()+"\t"+item.get_alt()+"\t"+item.get_soruce());
    		}
    		try {
				Thread.sleep(1000*2);
			} catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	}
    	return taoBaoList;
    	
	}

	private ArrayList<String> retrievalUsefulLinks()
	{
		
		return null;
	}
	
	private String retrievalImageFromSingleLink(String singleLink) throws NoSuchMethodException, ScriptException, InterruptedException
	{
		try {
			String s=TaoBaoCrawlerSingleWeb.crawlSinglePage(singleLink,src);
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
					s=TaoBaoCrawlerSingleWeb.crawlSinglePage(singleLink,data_src);
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
	
	private void imageLocalizer(ArrayList<TaoBaoItem> imageList,String wpath,String outpath)
	{
		for(int i=0;i<imageList.size();i++)
		{
			TaoBaoItem item=imageList.get(i);
			try {
				String path=TaoBaoCrawlerSingleWeb.storeSingleImage(item.get_url(),wpath, outpath, i);
				item.set_localAdd(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
			
		
	}
	
	
}
