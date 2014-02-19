package main;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import crawl.GoogleCrawler;





public class MessageListenerDeamon extends Thread
{
    Log log = LogFactory.getLog(MessageListenerDeamon.class);

    java.util.Properties prop=new 	java.util.Properties();
    public MessageListenerDeamon()
    {
        setDaemon(true);
    }
    
    @Override
    public void run()
    {
        // 获取消息请求检查时间间隔，单位：秒
        //int queueMessageCheckDuration = 24*60*60*1000;
    
   //     try {
        	
			//prop.load(new FileInputStream("D:\\EclipseWorkspace\\ContentDiary\\src\\main\\resources\\Config.properties"));
			
			//System.out.println("loading movies");
//        String duration=prop.get("Duration").toString();
        
        	try {
				GoogleCrawler.crawler();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
//            while (true)
//            {
//
//            
//                Thread.sleep(queueMessageCheckDuration * 2);
//            }
//        }
//        catch (InterruptedException e)
//        {
//            log.error("Exception: " + e.toString());
//        } 
		
    }
}

