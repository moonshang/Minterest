package entity;

public class TaoBaoItem {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	String movie_name;
	String movie_id;
	String url;
	String source;
	String alt;
	String local_add;
	String from;
	String title;
	int interesting=-1;
	int gourp=0;
	
	
	public String get_Movie_Name()
	{
		return this.movie_name;
	}
	
	public String get_movie_id()
	{
		return this.movie_id;
	}
	
	public String get_url()
	{
		return this.url;
	}
	
	public String get_soruce()
	{
		return this.source;
	}

	public String get_alt()
	{
		return this.alt;
	}
	public String get_localAdd()
	{
		return this.local_add;
	}
	public String get_from()
	{
		return this.from;
	}
	public String get_title()
	{
		return this.title;
	}
	public int get_interesting()
	{
		return this.interesting;
	}
	public int get_group()
	{
		return this.gourp;
	}
	
	public void set_Movie_Name(String movie_name)
	{
		this.movie_name=movie_name;
	}
	
	public void set_movie_id(String movie_id)
	{
		this.movie_id=movie_id;
	}
	
	public void set_url(String url)
	{
		this.url=url;
	}
	
	public void set_soruce(String source)
	{
		this.source=source;
	}

	public void set_alt(String alt)
	{
		this.alt=alt;
	}
	public void set_localAdd(String localAd)
	{
		this.local_add=localAd;
	}
	public void set_from(String from)
	{
		this.from=from;
	}
	public void set_title(String title)
	{
		this.title=title;
	}
	
}
