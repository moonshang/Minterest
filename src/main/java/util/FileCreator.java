package util;

import java.io.File;

public class FileCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path="D:\\taobao\\beijingyushangxiyatu\\taobao\\tongkuan\\2014-02-21";
	FileCreator.createFile(path);

	}
	public static void createFile(String path)
	{
		File f=new File(path);
		
		if(!f.exists())
			f.mkdirs();
		
	}

}
