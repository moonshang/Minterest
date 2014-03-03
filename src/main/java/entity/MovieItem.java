package entity;

public class MovieItem {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
	
	String movie_name;
	String movie_id;
	String[] actor_list;
	String director;
	
	
	public void set_movie_name(String movie_name)
	{
		this.movie_name=movie_name;
	}
	public String get_movie_name()
	{
		return this.movie_name;
	}
	public void set_movie_id(String movie_id)
	{
		this.movie_id=movie_id;
	}
	public String get_movie_id()
	{
		return this.movie_id;
	}
	public void set_actor_list(String[] actorList)
	{
		this.actor_list=actorList;
	}
	public String[] get_actor_list()
	{
		return this.actor_list;
	}
	public void set_director(String director)
	{
		this.director=director;
	}
	public String get_director()
	{
		return this.director;
	}

}
