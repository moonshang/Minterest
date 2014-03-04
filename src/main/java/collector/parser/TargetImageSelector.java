package collector.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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

import entity.GoogleHtmlObject;
import entity.WebImageObject;
import util.ImageCollectorUtils;
import util.Parameters;

public class TargetImageSelector {
	
	static final Logger LOG = Logger.getLogger(TargetImageSelector.class.getName());
	static final Logger WARN = Logger.getLogger(TargetImageSelector.class.getName());
	
	private static final int IMG_WIDTH = 720;
	
	public GoogleHtmlObject googleObject = null;
	public WebImageObject webImageObject = null;
	
	public TargetImageSelector(GoogleHtmlObject gobj, ArrayList<WebImageObject> imgObjs) {
		
		String queryUrlStr = gobj.smallImageUrl;
		
		WebImageObject targeObj = getTargetImage(queryUrlStr, imgObjs);
		if(targeObj==null){
			LOG.warn("No image found by image matching program...Maybe caused by small image size, or ioexception");
			return;
		}
		
		this.webImageObject = targeObj;
		this.googleObject = gobj;
		if(this.webImageObject==null){
			LOG.warn("webImageObject is null");
		}
		else{
			LOG.info("not null\t"+this.webImageObject.url);
		}
		
		File rootDir = new File(googleObject.saveRootDir);
		if(!rootDir.exists()||!rootDir.isDirectory()){
			rootDir.mkdir();
			this.webImageObject.addr = rootDir+"/0."+ImageCollectorUtils.getURLExtension(webImageObject.url);
		}
		else{
			File[] images = rootDir.listFiles();
			int index = images.length;
			this.webImageObject.addr = rootDir+"/"+String.valueOf(index)+"."+ImageCollectorUtils.getURLExtension(webImageObject.url);
			LOG.info(this.webImageObject.addr);
		}
		
	}
	
	private static WebImageObject getTargetImage(String queryurlstr, ArrayList<WebImageObject> targetImageObjs){
		
		Map<WebImageObject,Integer> map = new HashMap<WebImageObject,Integer>();
		Map<WebImageObject,Integer> mapSorted = new TreeMap<WebImageObject,Integer>();
		
		DoGSIFTEngine engine = new DoGSIFTEngine();	
		
		MBFImage queryImg;
		LocalFeatureList<Keypoint> queryKeypoints = null;
		try {
			InputStream in = ImageCollectorUtils.getInputStreamFromURL(queryurlstr);
			BufferedImage bImg = ImageIO.read(in);
			
			if (bImg.getWidth() > IMG_WIDTH)
				bImg = resizeImage(bImg, IMG_WIDTH);
			
			queryImg = ImageUtilities.createMBFImage(bImg, false);
			queryKeypoints = engine.findFeatures(queryImg.flatten());
			in.close();
		} catch (IOException e) {
			WARN.warn(Thread.currentThread().getName()+"\t"+e.getMessage());
			return null;
		}finally{
			
		}
			
		LocalFeatureList<Keypoint> targetKeypoints = null;
		for(WebImageObject targetImageObj:targetImageObjs){
			MBFImage targetImg;
			try {
				InputStream in = ImageCollectorUtils.getInputStreamFromURL(targetImageObj.url);
				if(in==null)continue;
				BufferedImage bImg = ImageIO.read(in);
				if(bImg==null)continue;
				int width = bImg.getWidth();
				int height = bImg.getHeight();
				if(width<Parameters.WIDTH || height<Parameters.HEIGHT){
					continue;
				}
				targetImageObj.width = width;
				targetImageObj.height = height;
				
				if (bImg.getWidth() > IMG_WIDTH)
					bImg = resizeImage(bImg, IMG_WIDTH);
				
				targetImg = ImageUtilities.createMBFImage(bImg, false);
				targetKeypoints = engine.findFeatures(targetImg.flatten());	
				in.close();
			} catch (IOException e) {
				WARN.warn(Thread.currentThread().getName()+"\t"+e.getMessage());
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
//			LOG.info(Thread.currentThread().getName()+"\tNumber of matches for image " + targetImageObj.url + " is " + numMatches);
			map.put(targetImageObj, numMatches);
		}
		
		mapSorted = sortByValues(map);
		Iterator<Map.Entry<WebImageObject, Integer>> it = mapSorted.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<WebImageObject, Integer> entry = it.next();
//			LOG.info(entry.getKey().url+"\t"+entry.getValue());
		}
		if(mapSorted.size()>0){
			WebImageObject returnObj = mapSorted.entrySet().iterator().next().getKey();
			LOG.info("Found image:"+returnObj.url);
			return returnObj;
		}
		else{
			return null;
		}
	
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
		
		ResampleOp resampleOp = new ResampleOp(destWidth,(destWidth * image.getHeight()) / image.getWidth() );
		resampleOp.setFilter(ResampleFilters.getLanczos3Filter()); 
		image = resampleOp.filter(image, null);
		
		return image;
		
	}
	
	
	
	
}
