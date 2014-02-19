package image;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.transforms.AffineTransformModel;
import org.openimaj.math.model.fit.RANSAC;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

/**
 * Rank the image from target folder according to query image
 * @author Yimin Yang
 *
 */
	public class RankImage {
		
		static Logger log = Logger.getLogger(RankImage.class);
		
		//Size for resize.
		private static final int IMG_WIDTH = 720;

		public static TreeMap<String,Integer> rankImage(String query, String targetFolder) {
			
			Map<String,Integer> map = new HashMap<String,Integer>();
			Map<String,Integer> mapSorted = new TreeMap<String,Integer>();
			
			DoGSIFTEngine engine = new DoGSIFTEngine();	
			
			MBFImage queryImg;
			LocalFeatureList<Keypoint> queryKeypoints = null;
			try {
				BufferedImage bImg = ImageIO.read(new File(query));
				
				if (bImg.getWidth() > IMG_WIDTH)
					bImg = resizeImage(bImg, IMG_WIDTH);
				
				queryImg = ImageUtilities.createMBFImage(bImg, false);
				queryKeypoints = engine.findFeatures(queryImg.flatten());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.warn(Thread.currentThread().getName()+"\t"+e.getMessage());
				return null;
			}
				
			File[] targetList = (new File(targetFolder)).listFiles();
			LocalFeatureList<Keypoint> targetKeypoints = null;
			log.debug("#image for image ranking: "+targetList.length);
			for (File file: targetList){
				MBFImage targetImg;
				try {
//					targetImg = ImageUtilities.readMBF(file);
					if(file.getName().equals("0.jpg"))continue;
					BufferedImage bImg = ImageIO.read(file);
					if(bImg==null)continue;
					if (bImg.getWidth() > IMG_WIDTH)
						bImg = resizeImage(bImg, IMG_WIDTH);
					
					targetImg = ImageUtilities.createMBFImage(bImg, false);
					targetKeypoints = engine.findFeatures(targetImg.flatten());	
				} catch (IOException e) {
					log.warn(Thread.currentThread().getName()+"\t"+e.getMessage());
					continue;
				}

				AffineTransformModel fittingModel = new AffineTransformModel(5);
				RANSAC<Point2d, Point2d> ransac = 
					new RANSAC<Point2d, Point2d>(fittingModel, 1500, new RANSAC.PercentageInliersStoppingCondition(0.5), true);

				LocalFeatureMatcher<Keypoint> matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
				  new FastBasicKeypointMatcher<Keypoint>(8), ransac);

				matcher.setModelFeatures(queryKeypoints);
				matcher.findMatches(targetKeypoints);
				
				int numMatches = matcher.getMatches().size();	
				log.info(Thread.currentThread().getName()+"\tNumber of matches for image " + file.getName() + " is " + numMatches);
				map.put(file.getName(), numMatches);
			}
			
			mapSorted = sortByValues(map);
			Iterator<Map.Entry<String, Integer>> it = mapSorted.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, Integer> entry = it.next();
				log.info(Thread.currentThread().getName()+"\t[Ranked result:]"+entry.getKey()+" Number of matches: " +entry.getValue());
			}
			return (TreeMap<String, Integer>) mapSorted;
		}
		
		public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
			Comparator<K> valueComparator =  new Comparator<K>() {
			    public int compare(K k1, K k2) {
			        int compare = map.get(k2).compareTo(map.get(k1));
			        if (compare == 0) return 1;
			        else return compare;
			    }
			};
			Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
			sortedByValues.putAll(map);
			return sortedByValues;
		}
		
		public static BufferedImage resizeImage(BufferedImage image, int destWidth) {
			
//			BufferedImage image = ImageIO.read(new File(filename));
			ResampleOp resampleOp = new ResampleOp(destWidth,(destWidth * image.getHeight()) / image.getWidth() );
			resampleOp.setFilter(ResampleFilters.getLanczos3Filter()); 
			image = resampleOp.filter(image, null);
			
			return image;
			
		}
		
	public static void main(String[] args) throws Exception {
		
		String queryImage = "C:/Users/ys439/workspace2/image-collector/data/xiaobo/changqun/xiaoshidaichangqun/0/0.jpg";
		String targetFolder = "C:/Users/ys439/workspace2/image-collector/data/xiaobo/changqun/xiaoshidaichangqun/0";
		
		TreeMap<String, Integer> map = RankImage.rankImage(queryImage, targetFolder);
		System.out.println(map.size());
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Integer> entry = it.next();
			System.out.println(entry.getKey()+"\t"+entry.getValue());
//			String name = it.next();
//			Integer score = map.get(name.trim());
//			System.out.println(name+"\t"+score);
		}
	}
	
}
