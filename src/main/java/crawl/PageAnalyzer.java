package crawl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.lionsoul.jcseg.test.JcsegTest;

import entity.GoogleImageItem;

import util.WordSimilarity;





public class PageAnalyzer 
{

	WordSimilarity wordSimi;
	
	
	public PageAnalyzer()
	{
		 try {
			wordSimi=new WordSimilarity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String[] query=new String[]{"小时代","皮草"};
		String in="D:\\TCLFiles\\WeiboNews\\Social Magazine\\xiaoshidai\\女装49.html";
		new PageAnalyzer().analyzer(query,in);
	}
	
	
	public  double analyzer(String[] query,String htmlFile)
	{
		try
		{
			JcsegTest test=new JcsegTest();
			ArrayList<GoogleImageItem> itemList=new ArrayList<GoogleImageItem>();
			
			Document doc = Jsoup.parse(new File(htmlFile), "UTF-8");
			Elements tableElement=doc.getElementsByClass("images_table");
		
			for(int i=0;i<tableElement.size();i++)
			{
				Elements imageElements=tableElement.get(i).select("td[style]");
				
				for(int j=0;j<imageElements.size();j++)
				{
//					System.out.println(imageElements.get(j).select("cite[title]").text());
//					System.out.println(imageElements.get(j).text());
//					System.out.println(imageElements.get(j).getElementsByAttribute("href").get(0).attributes().get("href"));
//					
//					
					GoogleImageItem item=new GoogleImageItem();
					
					
					item.set_source(imageElements.get(j).select("cite[title]").text());
					item.set_short_text(imageElements.get(j).text().replace(item.get_source(), ""));
					item.set_link(imageElements.get(j).getElementsByAttribute("href").get(0).attributes().get("href").split("=")[1].split("&")[0]);
					
					
					itemList.add(item);
					
//					System.out.println(item.get_source());
//					System.out.println(item.get_link());
//					System.out.println(item.get_short_text());
				}
			
			}
			
			double score=0.0;
			for(int i=0;i<itemList.size();i++)
			{
				System.out.println(itemList.get(i).get_short_text().trim());
				String[] strC=test.segment(itemList.get(i).get_short_text()).trim().split(" ");
				//System.out.println(strC);
				
				//System.out.println(score);
				
				if(!itemList.get(i).get_short_text().contains(query[0]))
					score=score+0;
				else
				score=score+caculateSim(query,strC);
			}
			
			
	//System.out.println("score:"+score/20);
	return score/20;
		}
		
		/*
		 * we caculate the query and text relations. 
		 */
	
		
		
		
		
		
		catch(Exception e)
		{
			e.printStackTrace();
			return 0.0;
		}
		
		
		
	}
	
	public void close()
	{
		wordSimi.close();
	}
	public double caculateSim(String[] query, String[] text) throws SQLException
	{
		String movieName=query[0];
		String queryWord=query[1];
		double count=0.0;
		double num=0.0;
		
		for(int i=0;i<text.length;i++)
		{
			if(Pattern.matches("[\u4e00-\u9fa5]++[\\d]*",text[i]))//regular expression helps match Chinese
			{
				if(!text[i].trim().equals((movieName.trim())))
				{
					//System.out.println(queryWord+"\t"+text[i].trim());
					//System.out.println(this.wordSimi.getSimilarity(queryWord, text[i].trim()));
					num=num+this.wordSimi.getSimilarity(queryWord, text[i].trim());
					count++;
				}
				
			}
		}
//		System.out.println("num"+num);
//		System.out.println("count"+count);
//		System.out.println(num/count);
		if(count==0)
			return 0;
		return num/count;
	}

}
