package query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KeyWordQueryFactory {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String queryFile="./Lex";
		KeyWordQueryFactory qf=new KeyWordQueryFactory(queryFile);
		
		for(String s:qf.getQuery())
		{
			System.out.println(s);
		}

	}
private  ArrayList<String> query=null;
	public  KeyWordQueryFactory()
	{
		if(query==null)
			query= new ArrayList<String>();
	}
	
	public  KeyWordQueryFactory(String queryFolder)
	{
		if(query==null)
		{
			query=new ArrayList<String>();
			BufferedReader readerMovie = null;
			try {
				readerMovie = new BufferedReader(new FileReader(queryFolder));
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
