package algorithm;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import entity.GoogleImageItem;



public class Ranker {
	int TOPK = 2000;
	
		public LinkedList<GoogleImageItem> rank(ArrayList<GoogleImageItem> unsorted) 
//				throws Exception
		{
			if(unsorted==null)return null;
			
			ArrayList<Pair> pairList = new ArrayList<Pair>();
			for(int i = 0;i<unsorted.size();i++){
				InputStream in = null;
				Image img = null;
				try {
					in = new FileInputStream(new File(unsorted.get(i).get_image_add()));
					img = ImageIO.read(in);
				} catch (FileNotFoundException e) {
					continue;
				}catch(IOException e){
					continue;
				}
				ImageIcon imgIcon = new ImageIcon(img);
				int height = imgIcon.getIconHeight();
				int width = imgIcon.getIconWidth();
				Pair pair = new Pair(i, height, width);
				pairList.add(pair);
			}
			
			Collections.sort(pairList, new PairComparator());
			LinkedList<GoogleImageItem> sortedList = new LinkedList<GoogleImageItem>();
			
			//return the top k result
			if(unsorted.size()<this.TOPK)this.TOPK=unsorted.size();
			for(int i = 0;i<this.TOPK;i++){
				int id = pairList.get(i).id;
				sortedList.add(unsorted.get(id));
			}
			
			return sortedList;
		}
		
//		public void rankTopItems(int number_Top) throws SQLException
//		{
//			 String sql="SELECT distinct movie_name FROM Rideo.Published_ImRep_Wanying";
//			 System.out.println(sql);
//			 PreparedStatement stmt = conn.clientPrepareStatement(sql);
//			 ResultSet rs = stmt .executeQuery(sql);
//			 ArrayList<String> movieNameList=new ArrayList<String>();
//			 while(rs.next())
//			  { 
//				 movieNameList.add(rs.getString("movie_name"));
//			  }
//			 
//			 ArrayList<GoogleImageItem> itemList=new ArrayList<GoogleImageItem>();
//			 for(int i=0;i<movieNameList.size();i++)
//			 {
//				 String sqlUrl="SELECT DISTINCT(url),movie_name,movie_id,source,alt,local_add,`from`,title,`group` from Rideo.Published_ImRep_Wanying where movie_name='" +
//			 movieNameList.get(i)+"' group by url";
//				 System.out.println(sqlUrl);
//				 PreparedStatement stmtURL = conn.clientPrepareStatement(sql);
//				 ResultSet rsURL = stmtURL .executeQuery(sqlUrl);
//				 while(rsURL.next())
//				 {
//					 System.out.println(rsURL.getString("url"));
//					 System.out.println(rsURL.getString("movie_name"));
//					 System.out.println(rsURL.getString("movie_id"));
//					 System.out.println(rsURL.getString("source"));
//					 System.out.println(rsURL.getString("alt"));
//					 System.out.println(rsURL.getString("local_add"));
//					 System.out.println(rsURL.getString("from"));
//					 System.out.println(rsURL.getString("title"));
//					 System.out.println(rsURL.getInt("group"));
//					 GoogleImageItem gii=new GoogleImageItem();
//					 gii.set_image_add(rsURL.getString("local_add"));
//					
//					 break;
//				 }
//				 break;
//			 }
//		}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0;i<10;i++){
			list.add(i);
		}

	}

}

