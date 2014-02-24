package crawl;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import java.util.concurrent.Future;






import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import util.FileName2Pinyin;
import util.URLEncoding;







public class Grawler 
{  
	final static Log log = LogFactory.getLog(Grawler.class);
	Vector<String> taskTrackQueue=null;
	double threashold;
	int number_limit;
	LinkedList<String> keywordList=new LinkedList<String>();
	Vector<String> taskQueue=null;
	Vector<String> movieNameList=new Vector<String>();
	public static Object signal = new Object();
	static Future<String> future=null;
	
	
	
	public Grawler()
	{
		 java.util.Properties prop=new 	java.util.Properties();
		 try {
			prop.load(new FileInputStream("./config"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		threashold= new Double(prop.get("threshold").toString());
		number_limit=new Integer(prop.get("number").toString());
		
		
		try
		{
		BufferedReader readerLex=new BufferedReader(new FileReader("./Lex"));
		
		
		String keyword="";
		
		while((keyword=readerLex.readLine())!=null)
		{
			String s=keyword;
			keywordList.add(s);
		}
		
		for(int i=0;i<keywordList.size();i++)
		{
			System.out.println(keywordList.get(i));
		}
		
		readerLex.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			
		BufferedReader readerMovie=new BufferedReader(new FileReader("./movieList"));
		String movieName="";
	    while((movieName=readerMovie.readLine())!=null)
		{
			String s=movieName;
			movieNameList.add(s);
		}
					
		readerMovie.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		taskQueue=new Vector<String>();
//		taskTrackQueue=new Vector<String>();
	}
	
	
	
	
	
	
	
	public ObjectMapper getJsonMapper() 
	{
		ObjectMapper mapper = new ObjectMapper(); 
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(Feature.INDENT_OUTPUT, true);
		return mapper;
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public synchronized ArrayList<String> crawler_google_image_htmlFormat(String date,String movie_name) throws IOException 
	{
		ArrayList<String> ci_list=new ArrayList<String>();
		
		 String pinyin=FileName2Pinyin.convertHanzi2PinyinStr(movie_name);
		 
		 
		 java.util.Properties prop=new 	java.util.Properties();
		 try {
			prop.load(new FileInputStream("./config"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//File movieFolder=new File("/mnt/nfs/nas179/rideo/"+pinyin);
		File movieFolder=new File("/mnt/nfs/nas179/rideo_Minterest/"+pinyin);
		 
		//File movieFolder=new File(prop.getProperty("movieFolder")+pinyin);
	    // File movieFolder=new File("/rideo/"+pinyin);
		  if(!movieFolder.exists())
		  {
			  System.out.println(movieFolder.getAbsolutePath());
			  movieFolder.mkdirs();
		  }
		  
		

			String[] user_agents = {
					       "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",//ok
					     //  "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20130406 Firefox/23.0", //not work
			             //  "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0", //not work
			               "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533 (KHTML, like Gecko) Element Browser 5.0",//ok
			               "IBM WebExplorer /v0.94', 'Galaxy/1.0 [en] (Mac OS X 10.5.6; U; en)"//ok
			             //  "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",//not ok
			             //  "Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14",//not ok
			             //  "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25",//not work
			             //  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1468.0 Safari/537.36",//not work
			              // "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0; TheWorld)",//not work
			               //"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.66 Safari/537.36"
			};
			
			HashMap<String,Boolean> keywordToDoMap=new HashMap<String,Boolean>();
			for(int i=0;i<keywordList.size();i++)
			{
				String keywordPY=FileName2Pinyin.convertHanzi2PinyinStr(keywordList.get(i));
				keywordToDoMap.put(keywordPY, false);
			}
		
			
			for(int j=0;j<keywordList.size();j++)
			{
				   String keyword_pinyin=FileName2Pinyin.convertHanzi2PinyinStr(keywordList.get(j));
			       String keyword_folder=movieFolder.getAbsolutePath()+"/"+keyword_pinyin;
			       
			       if(!new File(keyword_folder).exists())
			    	   new File(keyword_folder).mkdirs();
			       
			       File configFile=new File(keyword_folder+"/"+"config");
					  if(!configFile.exists())
						  configFile.createNewFile();
	      for(int i=0;i<number_limit;i++)
	      {
	    	 

		   URL url=new URL("http://www.google.com/search?hl=en&site=imghp&tbm=isch&source=hp&q=" +URLEncoding.encode(movie_name)+"+"+URLEncoding.encode(keywordList.get(j).toString())+
			    			"&start="+20*i);
	   
	    	HttpURLConnection cont = (HttpURLConnection) url.openConnection();
	    	cont.setConnectTimeout(10000);
	    	
	    	
	    	int random=(int)(Math.random()*2) ;
	        cont.setRequestProperty("User-Agent",user_agents[random] );            
	        cont.connect();
	        BufferedReader  reader = new BufferedReader(new InputStreamReader(cont.getInputStream()));
	        String outputFileName=movieFolder.getAbsolutePath()+"/"+keyword_pinyin+"/"+date+"_"+i+".html";
	        
	        FileWriter fw = new FileWriter(outputFileName);
			BufferedWriter bw = new BufferedWriter(fw);
	       
	        String s;        
	        while((s=reader.readLine())!=null) 
	        {
	             bw.write(new String(s.getBytes(),"UTF-8"));
	        }        
	        
	        bw.flush();
	        bw.close();
	        
	        
	        BufferedReader configReader=new BufferedReader(new FileReader(configFile));
	        String lastFileName=null;
	        String line=null;
	        while((line=configReader.readLine())!=null)
	        {
	        	
	        	lastFileName=line;
	        }
	        
	        configReader.close();
	        
	        
	        boolean needsUpdate=true;
	        if(lastFileName==null)
	        {
	        	
	        }
	        else
	        {
	        	
		        needsUpdate=compare(outputFileName,lastFileName);
	        }
	        
	        if(needsUpdate)
	        {
	        	
	        	
	        	
	            try
		   {

		        PageAnalyzer pgAnalyzer=new PageAnalyzer();
		        String[] query={movie_name,keywordList.get(j).toString()};
		        double score= pgAnalyzer.analyzer(query,outputFileName);
		       if(score<threashold)
		       {
		    	   new File(outputFileName).delete();
		    	   break;
		       }
		       else
		       {
		    	   if(i==0)
		        	{
		        		BufferedWriter configWriter=new BufferedWriter(new FileWriter(configFile));
		        		configWriter.write(outputFileName);
		        		configWriter.flush();
		        		configWriter.close();
		        	}
		    	   
		    	   
		        	
		       }
		      }
		    
		      catch(Exception e)
		      {
		    	  e.printStackTrace();
		    	  System.out.println("blocked by google, wait for 30 min");
		    	  try {
					Thread.sleep(1000*60*3*10);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }
		     }

	        else
	        {
	        	
	        	new File(outputFileName).delete();
	        	break;
	        }
	        	
	       
	        	
	      }
	      
	        
	        
	   
	      int r=(int) (Math.random()*2);
	      try
	      {

	     // For here we have all htmls. 
//	      final String folderName=movieFolder.getAbsolutePath()+"/"+keyword_pinyin;
//	      new File(folderName).getAbsolutePath();
//	      
//	      ExecutorService threadPool = Executors.newCachedThreadPool();
//	  	
//			CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
//			 cs.submit(new Callable<Integer>() 
//					{  
//	                public Integer call() throws Exception 
//	                {  
//	                   ImageCollector.start(folderName, 10) ;
//	                   return 1;
//	                }  
//	            });  
//	    	  final String folderName=movieFolder.getAbsolutePath()+"/"+keyword_pinyin;
//	    	  final String keyword_final=keyword_pinyin;
//	    	  if(j==0) //first one we need to do this......
//	    	  {
//	    	  Callable<String> task=new Callable<String> ()
//	          {
//	    		  public String call()
//	    		  {
//	    			  
//	    			  ImageCollector.start(folderName, 10) ;
//	    			  return keyword_final;
//	    			
//	    		  }
//	    	  };
//	    	   ExecutorService executor = Executors.newCachedThreadPool();
//		       Future<String> future = executor.submit(task);
//		      
//	    	  if( future.isDone())
//	    	 {
//	    		  keywordToDoMap.put(key, value)
//	    		  get(future.get())
//	    	 }
//	    	  }
//	    	  else
//	    	  {
//	    		  
//	    	  }
	    	  
	    	
	    	
	    	 
	    	  System.out.println("waiting for "+2*r+" min");
	          Thread.sleep(1000*60*2*r);
	      }
	      catch(Exception e)
	      {
	    	  e.printStackTrace();
	    	  
	      }
	    
	      DeleteMoiveTask(movie_name);
	      ci_list.add(movieFolder.getAbsolutePath()+"/"+keyword_pinyin);
	  }
			
			return ci_list;

	}
	
	private synchronized void DeleteMoiveTask(String movie_name)
	{
		movieNameList.remove(movie_name);
		
	}
	

	
	
	private boolean compare(String outputFileName, String lastFile) throws IOException 
	
	{
		return true;
		
	}


	
	
	
	
	
	
	
	
	
	
	
//	public synchronized String getNextMovieName()
//	{
//		if(movieNameList.isEmpty())return null;
//		else{
//			String movieFile = movieNameList.get(0);
//			return movieFile;
//		}
//	} 
	
	
	
	
//	private synchronized void begin()
//	{
//		
//			new Thread(new Runnable() 
//			{
////				@Override
//				public void run() 
//				{
//					// TODO Auto-generated method stub
//					while(true)
//					{
//						String fileurl = getNextMovieName();
//						if(fileurl!=null)
//						{
//							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//							Date date = new Date();                               
//							System.out.println(sf.format(date));   
//							try {
//								crawler_google_image_htmlFormat(sf.format(date),fileurl);
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							continue;
//						}
//						else{
//							synchronized (signal) 
//							{
//								try{
//									signal.wait();
//								}catch(InterruptedException e)
//								{
//									e.printStackTrace();
//									log.warn(Thread.currentThread().getName()+"\t"+e.getMessage());
//								}
//							}
//						}
//					}
//					
//				}
//			}).start();
//		}
//	
	
	
	
//	public synchronized static void crawl() throws InterruptedException
//	{
//		PropertyConfigurator.configure("log4j.properties");
//		
//
//		RSSCrawler crawler = new RSSCrawler();
//		long start= System.currentTimeMillis(); 
//		crawler.begin();
//		
//		
//  
//		
////		  Callable<String> task=new Callable<String> ()
////		          {
////		    		  public String call() throws InterruptedException
////		    		  {
////		    			  System.out.println("wait for taks inside the queue");
////		    			  Thread.sleep(1000*60*10);
////						return null;
////		    			
////		    		  }
////		    	  };
////		    	   ExecutorService executor = Executors.newCachedThreadPool();
////			       future= executor.submit(task);
////			    	crawler.taskTrackQueue.add(crawler.taskQueue.get(0));
////					System.out.println(crawler.taskQueue.get(0)+" finished");
////					System.out.println(crawler.taskQueue.get(0)+" removed");
////					crawler.taskQueue.remove(crawler.taskQueue.get(0));
//		
//		
//		
//		
//		while(true)
//		{
//			if(crawler.taskQueue.isEmpty())
//			{
//				
//				if( crawler.movieNameList.isEmpty())
//				{
//				long end= System.currentTimeMillis(); 
//				log.info("总共耗时"+(end-start)/1000+"秒");
//                System.out.println("总共耗时"+(end-start)/1000+"秒");  
//                System.exit(1);
//				}
//			}
//			else if(!crawler.taskQueue.isEmpty()&&!crawler.taskTrackQueue.contains(crawler.taskQueue.get(0)))
//			{
//				System.out.println("Doing image crawling for"+crawler.taskQueue.get(0));
//
//				final String taskToDo=crawler.taskQueue.get(0);
//				
//				if(future==null)
//				{
//					
//					  Callable<String> task=new Callable<String> ()
//			          {
//			    		  public String call() throws InterruptedException
//			    		  {
//			    			  System.out.println("wait for taks inside the queue");
//			    			  Thread.sleep(1000*60*10);
//							return null;
//			    			
//			    		  }
//			    	  };
//			    	   ExecutorService executor = Executors.newCachedThreadPool();
//				       future= executor.submit(task);
//				       crawler.taskTrackQueue.add(crawler.taskQueue.get(0));
//						System.out.println(crawler.taskQueue.get(0)+" finished");
//						System.out.println(crawler.taskQueue.get(0)+" removed");
//						crawler.taskQueue.remove(crawler.taskQueue.get(0));
//					
//					
//				}
//				else
//				{
//					
//				}
//				
//					System.out.println(future);
//					System.out.println("is done "+future.isDone());
//					System.out.println("is cancelled"+future.isCancelled());
//				
////						  System.out.println("restart new thread");
////						  task=new Callable<String> ()
////						          {
////						    		  public String call() throws InterruptedException
////						    		  {
//////						    			  System.out.println("this is a new thread");
////						    			 
////						    			  ImageCollector ic=new ImageCollector(taskToDo);
////						  				  ic.start(3);
////										return taskToDo;
////						    			
////						    		  }
////						    	  };
////						    	   executor = Executors.newCachedThreadPool();
////							       future= executor.submit(task);
////							    	crawler.taskTrackQueue.add(crawler.taskQueue.get(0));
////									System.out.println(crawler.taskQueue.get(0)+" finished");
////									System.out.println(crawler.taskQueue.get(0)+" removed");
////									crawler.taskQueue.remove(crawler.taskQueue.get(0));
//				  
//			}
//				
//			}
//		}
//	
//	
//	





//	public void crawl()
//	{
//		
//		LinkedList<String> taskQueue=new LinkedList<String>();
//		
//		ArrayList<String> movieList=new ArrayList<String>();
//		try
//		{
//			
//		BufferedReader readerMovie=new BufferedReader(new FileReader("./movieList"));
//		String movieName="";
//	    while((movieName=readerMovie.readLine())!=null)
//		{
//			String s=movieName;
//			movieList.add(s);
//		}
//					
//		readerMovie.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		
//		
//		
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();                               
//		System.out.println(sf.format(date));    
//		
//		for(int i=0;i<movieList.size();i++)
//		{
//		try
//		{
//			Boolean movieGoogleSearch=crawler_google_image_htmlFormat(sf.format(date),movieList.get(i));
//			if(movieGoogleSearch)
//			{
//				taskQueue.push(movieList.get(i));
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		}
//	}
	
	
	
	public static void main(String[] args) 
	{
		
		try
		{
//			String[] kws=new String[]{keyword_title,keyword_actor_1,keyword_actor_2,keyword_actor_3};
//			new RSSCrawler().crawler_baidu(kws);
       

		 //  Douban d=new Douban("杩滄柟鍦ㄥ摢閲�);
		//   new RSSCrawler().crawler_baidu_JsonFormat("杩滄柟鍦ㄥ摢閲�, d.movie.actors);

//		  RSSCrawler.crawl();
			


		   
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
