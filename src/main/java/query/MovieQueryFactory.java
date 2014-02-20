package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MovieQueryFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String movieFile="./movieList";
		MovieQueryFactory qf=new MovieQueryFactory(movieFile);
		
		for(String s:qf.getQuery())
		{
			System.out.println(s);
		}

	}
private  ArrayList<String> query=null;
	public  MovieQueryFactory()
	{
		if(query==null)
			query= new ArrayList<String>();
	}
	
	public  MovieQueryFactory(String movieFolder)
	{
		if(query==null)
		{
			query=new ArrayList<String>();
			BufferedReader readerMovie = null;
			try {
				readerMovie = new BufferedReader(new FileReader(movieFolder));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String movieName="";
		    try {
				while((movieName=readerMovie.readLine())!=null)
				{
					String s=movieName;
					query.add(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			try {
				readerMovie.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getQuery()
	{
		return query;
	}
}
