package entity;

public class RideoItem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	int id;
	String p_id;
	String m_id;
	String p_url;
	String description;
	String source_link;
	String local_add;
	String source_type;
	String isExternal;
	String isInteresting;
	int group_num;
	String title;
	String tags;
	double width;
	double height;
	String issue;
	String date;
	String keyword;


	
	public void setId( int id)
	{
		this.id=id;
	}
	public int getId()
	{
		return this.id;
	}
	public void setPId(String pid)
	{
		this.p_id=pid;	
	}
	public String getPID()
	{
		return this.p_id;
	}
	public void setMId(String mid)
	{
		this.m_id=mid;
	}
	public String getMID()
	{
		return this.m_id;
	}
	public void setPUrl(String p_url)
	{
		this.p_url=p_url;
	}
	public String getPUrl()
	{
		return this.p_url;
	}
	public void setDes(String des)
	{
		this.description=des;
		
	}
	public String getDes()
	{
		return this.description;
	}
	public void setSourceLink(String sourceLink)
	{
		this.source_link=sourceLink;
	}
	public String getSourceLink()
	{
		return this.source_link;
	}
	public void setLocalAdd(String localAdd)
	{
		this.local_add=localAdd;
		
	}
	public String getLocalAdd()
	{
		return this.local_add;
	}
	
	public void setSourceType(String sourceType)
	{
		this.source_type=sourceType;
	}
	public String getSourceType()
	{
		return this.source_type;
	}
	public void setIsExternal(String isExternal)
	{
		this.isExternal=isExternal;
	}
	public String getIsExternal()
	{
		return this.isExternal;
	}
	public void setIsInteresting(String isInteresting)
	{
		this.isInteresting=isInteresting;
	}
	public String getIsInteresting()
	{
		return this.isInteresting;
	}
	
	public void setGroupNum(int group_num)
	{
		this.group_num=group_num;
	}
	public int getGroupNum()
	{
		return this.group_num;
	}
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getTitle()
	{
		return this.title;
	}
	
	
	public void setTags(String tags)
	{
		this.tags=tags;
		
	}
	
	public String getTags()
	{
		return this.tags;
	}
	public void setWidths(double width)
	{
		this.width=width;
	}
	public double getWidths()
	{
		return this.width;
	}
	public void setHeights(double height)
	{
		this.height=height;
	}
	public double getHeights()
	{
		return this.height;
	}
	public void setIssues(String issues)
	{
		this.issue=issues;
	}
	public String getIssues()
	{
		return this.issue;
	}
	
	public void setDate(String date)
	{
		this.date=date;
	}
	public String getDate()
	{
		return this.date;
	}
	
	public void setKeyword(String keyWord)
	{
		this.keyword=keyWord;
	}
	public String getKeyword()
	{
		return this.keyword;
	}
	
	
	


	
	
}
