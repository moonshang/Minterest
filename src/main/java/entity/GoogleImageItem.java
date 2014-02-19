package entity;

public class GoogleImageItem {

	/**
	 * @param args
	 */

	String source;
	String link;
	String short_text;
	String website_content;
	String image_add;
	String image_url;
	String[] related_image_adds;
	String[] related_image_urls;
	
	public void set_source(String source)
	{
		this.source=source;
	}
	public void set_link(String link)
	{
		this.link=link;
	}
	
	public void set_short_text(String short_text)
	{
		this.short_text=short_text;
	}
	public void set_website_content(String website_content)
	{
		this.website_content=website_content;
	}
	public void set_image_add(String image_add)
	{
		this.image_add=image_add;
	}
	public void set_image_url(String image_url)
	{
		this.image_url=image_url;
	}
	public void set_related_image_adds(String[] related_image_adds)
	{
		this.related_image_adds=related_image_adds;
	}
	
	public void set_related_image_urls(String[] related_image_urls)
	{
		this.related_image_urls=related_image_urls;
	}
	
	
	
	public String get_source()
	{
		return this.source;
	}
	
	public String get_link()
	{
		return this.link;
	}
	public String get_short_text()
	{
		return this.short_text;
	}
	public String get_website_content()
	{
		return this.website_content;
	}
	public String get_image_add()
	{
		return this.image_add;
	}
	public String get_image_url()
	{
		return this.image_url;
	}
	public String[] get_related_image_adds()
	{
		return this.related_image_adds;
	}
	
	public String[] get_related_image_urls()
	{
		return this.related_image_urls;
	}
	
	
	
}
