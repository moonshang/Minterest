package database;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;




import com.mysql.jdbc.Connection;

import entity.GoogleImageItem;
import entity.TaoBaoItem;


public class MysqlDatabase {

	
	static String driver = "com.mysql.jdbc.Driver";

	public static Connection conn =null;
	
	public MysqlDatabase()
	{
		try
		{
			Class.forName(driver);
	
		
		if(conn==null)
		{
			
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.55:3306/Rideo?useUnicode=true&characterEncoding=gb2312","root","111111");
		
		//	conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/ContentDiary?useUnicode=true&characterEncoding=UTF-8","root","111111");
			
			
		//	conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/ContentDiary?useUnicode=true&characterEncoding=UTF-8","root","glf1030");

			System.out.println("sucess connect mysql");

		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public  boolean insertDataCollectionintoMysql(ArrayList<TaoBaoItem> taoBaoCollection)
	{

		try {
			
			
	    for(int i=0;i<taoBaoCollection.size();i++)
	    {
	    
	    	TaoBaoItem single_new=taoBaoCollection.get(i);
		PreparedStatement stat=conn.clientPrepareStatement("INSERT INTO Published_ImRep VALUES(?,?,?,?,?,?,?,?,?,?,?)"); 
		    
			/* id
			 * movie_name
			 * key_word
			 * title
			 * date
			 * content
			 * image_url
			 * gossip_url
			 *image_add
			 *movie_id
			 */
			stat.setInt(1, 0);
			stat.setString(2, single_new.get_Movie_Name()); 
			stat.setString(3, single_new.get_movie_id()); 
			stat.setString(4, single_new.get_url()); 
		    stat.setString(5,  single_new.get_soruce());
			stat.setString(6, single_new.get_alt()); 
			stat.setString(7, single_new.get_localAdd()); 
			stat.setString(8, single_new.get_from()); 
			stat.setString(9, single_new.get_title());
			stat.setInt(10, single_new.get_interesting());
			stat.setInt(11, single_new.get_group());

			int index=stat.executeUpdate();
			System.out.println("insert data "+index);
	    }
	    

		
		
		
			
        return true;
			
		} 
		
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}  
		
		
		
		
		
	
	}

	
	public HashMap<String,String[]> batchsearchMovieDataFromMysql()
	{
		 HashMap<String, String[]> movieData=new HashMap<String,String[]>();
		
		 try
		 {
			 String sql="select * from ContentDiary.movie";
			 PreparedStatement stmt = conn.clientPrepareStatement(sql);
		
			  ResultSet rs = stmt .executeQuery(sql);
			  
			  while(rs.next())
			  { 
				  
			   String  movie_name=rs.getString("movie_name");
			   String  movie_id=rs.getString("movie_id");
			   String  movie_actors=rs.getString("actor_list");
			   String  movie_director=rs.getString("director");
			   
			   String[] list={movie_id,movie_director,movie_actors};
			   movieData.put(movie_name,list);
	           System.out.println(rs.getString("movie_name"));
	           System.out.println(rs.getString("movie_id"));
	              
	        
	              
			  }
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 
		
		return movieData;
	}

	
	public String getMId(String movie_name)
	{
		String movie_id=null;
		try
		{
			 String sql="select * from ContentDiary.movie where movie_name like'"+movie_name+"%'";
			 System.out.println(sql);
			 PreparedStatement stmt = conn.clientPrepareStatement(sql);
			 ResultSet rs = stmt .executeQuery(sql);
			 while(rs.next())
			  { 
				  movie_id=rs.getString("movie_id");

			  }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return movie_id;
	}
	

	
	public void rankTopItems(int number_Top) throws SQLException
	{
		 String sql="SELECT distinct movie_name FROM Rideo.Published_ImRep_Wanying";
		 System.out.println(sql);
		 PreparedStatement stmt = conn.clientPrepareStatement(sql);
		 ResultSet rs = stmt .executeQuery(sql);
		 ArrayList<String> movieNameList=new ArrayList<String>();
		 while(rs.next())
		  { 
			 movieNameList.add(rs.getString("movie_name"));
		  }
		 
		 ArrayList<GoogleImageItem> itemList=new ArrayList<GoogleImageItem>();
		 for(int i=0;i<movieNameList.size();i++)
		 {
			 String sqlUrl="SELECT DISTINCT(url),movie_name,movie_id,source,alt,local_add,`from`,title,`group` from Rideo.Published_ImRep_Wanying where movie_name='" +
		 movieNameList.get(i)+"' group by url";
			 System.out.println(sqlUrl);
			 PreparedStatement stmtURL = conn.clientPrepareStatement(sql);
			 ResultSet rsURL = stmtURL .executeQuery(sqlUrl);
			 while(rsURL.next())
			 {
				 System.out.println(rsURL.getString("url"));
				 System.out.println(rsURL.getString("movie_name"));
				 System.out.println(rsURL.getString("movie_id"));
				 System.out.println(rsURL.getString("source"));
				 System.out.println(rsURL.getString("alt"));
				 System.out.println(rsURL.getString("local_add"));
				 System.out.println(rsURL.getString("from"));
				 System.out.println(rsURL.getString("title"));
				 System.out.println(rsURL.getInt("group"));
				 GoogleImageItem gii=new GoogleImageItem();
				 gii.set_image_add(rsURL.getString("local_add"));
				
				 break;
			 }
			 break;
		 }
	}
	
	
	
	public void close()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException
	{
		/*
		 * success. 
		 * test by lg
		 */
		MysqlDatabase mdb=new MysqlDatabase();
		mdb.rankTopItems(10);
	}
}
