package query;

import java.util.ArrayList;

import database.MysqlDatabase;
import entity.MovieItem;

public class TaoBaoKeyWordQueryFacotry {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	private  ArrayList<String> query=null;
	public  TaoBaoKeyWordQueryFacotry()
	{
		if(query==null)
			query= new ArrayList<String>();
		try
		{
			MysqlDatabase md=new MysqlDatabase();
			ArrayList<String> queryList=md.getTaoBaoKeyWords();
		
			for(String q:queryList)
				{
				query.add(q);
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getQuery()
	{
		return this.query;
	}
}
