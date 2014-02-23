/**
 * 
 */
package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author Wanying
 *
 */
public class WordSimilarity {

	/**
	 * @param args
	 */
	
	static Connection conn;
	public double similarity;
	
	public WordSimilarity() throws Exception
	{
		connDB();
		
	}
	
	public void close()
	{
		try {
			WordSimilarity.conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connDB(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			 java.util.Properties prop=new 	java.util.Properties();
			 try {
				prop.load(new FileInputStream("./config"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			String mysqlConfig=prop.getProperty("mysql");
			String userName=prop.getProperty("root");
			String passWord=prop.getProperty("password");
			//System.out.println(mysqlConfig+userName+passWord);
			
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.55:3306/HowNet?useUnicode=true&characterEncoding=UTF-8","root","111111");
	    // conn = (Connection) DriverManager.getConnection(mysqlConfig,userName,passWord);

			//System.out.println("Successfully Connect to the Database!");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public String getGlossary(String word) throws SQLException{
		String glossary="";
		Statement stmt = conn.createStatement();
		
		String sql = "select glossary from dictionary where word = '"+word+"'";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()){
			glossary = rs.getString("glossary").trim();
		}
		return glossary;
	}
	
	public HashSet<String> getWordList(String glossary){
		HashSet<String> set = new HashSet<String>();
		String list[];
		list = glossary.split(",");
		for(int l=0;l<list.length;l++){
			list[l]=list[l].replaceAll("\\pS|\\pN|\\pP", "");
			set.add(list[l].trim());
		}
		return set;
	}
	
	public double getSimilarity(String word1, String word2) throws SQLException{
		double sim=0.0;
		if(word1.trim().equals(word2.trim()))
			return 1.0;
		
		String glossary1 = getGlossary(word1).trim();
		String glossary2 = getGlossary(word2).trim();
		//System.out.println(glossary1+":"+glossary2);
		if(glossary1.isEmpty()||glossary2.isEmpty()) return 0.0;
		
		HashSet<String> set1 = getWordList(glossary1);
		HashSet<String> set2 = getWordList(glossary2);
		
		int length1 = set1.size();
		int length2 = set2.size();
		int co_length=0;
		
		Iterator<String> iter = set1.iterator();
		while(iter.hasNext()){
			Object obj = iter.next();
			String word = obj.toString().trim();
			if(set2.contains(word)){
				co_length++;
			}
		}
		
		sim=(double)co_length/((double)length1+(double)length2-co_length);
		
		return sim;
	}
	
	public static void main(String[] args) throws Exception 
	{
//		String word1 ="内衣";
//		String word2 ="睡衣";
//		
//		WordSimilarity wordSimi=new WordSimilarity();
	
	//System.out.println(	wordSimi.getSimilarity(word1,word2));
		

	}

}
