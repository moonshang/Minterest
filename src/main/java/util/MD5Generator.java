package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

                String result=MD5Generator.execute("http://gi1.md.alicdn.com/bao/uploaded/i1/13754019374130956/T1Rz7ZXaBbXXXXXXXX_!!0-item_pic.jpg_360x360q90.jpg");
System.out.println(result);
	}
	
	public static String execute(String plaintext)
	{
	
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
			
			return hashtext;
		} catch (NoSuchAlgorithmException e) 
		{
			
			e.printStackTrace();
			return null;
		}
		
	}

}
