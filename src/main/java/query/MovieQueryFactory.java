package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import database.MysqlDatabase;
import entity.MovieItem;

public class MovieQueryFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//String source="./movieList";
		MovieQueryFactory qf=new MovieQueryFactory();


	}
private  HashMap<String, String> query=null;
	public  MovieQueryFactory()
	{
		if(query==null)
			query= new HashMap<String, String>();
		try
		{
			MysqlDatabase md=new MysqlDatabase();
			ArrayList<MovieItem> miList=md.batchsearchMovieDataFromMysql();
			for(MovieItem mi:miList)
				{
				query.put(mi.get_movie_name().substring(0,mi.get_movie_name().indexOf("(")),mi.get_movie_id());
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	
	public HashMap<String,String> getQuery()
	{
		return query;
	}
}
