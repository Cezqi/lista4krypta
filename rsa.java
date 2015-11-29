

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;



public class rsa {
	
  public static final String ALGORITHM = "RSA";
  

  public static final String PRIVATE_KEY_FILE = "C:/keys/private.txt";

  public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
  public static int size=100;

  
  public static BigInteger getCoprime(BigInteger m) {
      int length = m.bitLength()-1;
      Liczby lp= new Liczby();
      BigInteger e = lp.generuj(length);
      while (! (m.gcd(e)).equals(BigInteger.ONE) ) {
      	 e = lp.generuj(length);
      }
      return e;
   }

  public static void generateKey(int kk, int dd) {
    try {  BigInteger n,e,d;
      size=(kk*dd)/2;

      File privateKeyFile = new File(PRIVATE_KEY_FILE);
      File publicKeyFile = new File(PUBLIC_KEY_FILE);

      // Create files to store public and private key
      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();
      Liczby lp= new Liczby();
      BigInteger p,q;
      String numStr;
     // do{
      p= lp.generuj(size);
       q= lp.generuj(size);
       
      n = p.multiply(q);
   
      numStr = n.toString();
      //}while (numStr.length()!=(kk)  );
      
      BigInteger m = (p.subtract(BigInteger.ONE)).multiply(
         q.subtract(BigInteger.ONE));
      e = getCoprime(m);
       d = e.modInverse(m);
       
      writeFile( "public.key",  e+ ";" +n);
      writeFile( "private.key",  d+ ";" +n);
 			 

/*System.out.println("p: "+p);
      System.out.println("q: "+q);
      System.out.println("Euklides: "+m);
      System.out.println("Modulus: "+n);
      System.out.println("Key size: "+n.bitLength());
      System.out.println("Public key: "+e);
      System.out.println("Private key: "+d); */
  
    
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  
  public byte[] copy(byte[] a, byte[] b) {
	    byte[] result = new byte[a.length + b.length]; 
	    System.arraycopy(a, 0, result, 0, a.length); 
	    System.arraycopy(b, 0, result, a.length, b.length); 
	    return result;
	} 


  public static boolean areKeysPresent() {

    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
    return false;
  }

	private static String bytesToString(byte[] encrypted) {
		String test = "";
		for (byte b : encrypted) {
			test += Byte.toString(b);
		}
		return test;
	}
	//Encrypt message
	public byte[] encrypt(byte[] message, BigInteger e, BigInteger n) {
		return (new BigInteger(message)).modPow(e, n).toByteArray();
	}
	// Decrypt message
	public byte[] decrypt(byte[] message, BigInteger d, BigInteger n) {
		return (new BigInteger(message)).modPow(d, n).toByteArray();
	}
	public int size(String tekst, BigInteger n)
	{  
		 String sizeTest=n.toString();
		 String  first; 
		 byte[] by;
		    
		    int size=sizeTest.length();
		   if(size>tekst.length()) size=tekst.length();
		    while(true)
		    {// if(true) return 2;
		  first = tekst.substring(0, size);
		by=first.getBytes();
		BigInteger nn = new BigInteger(by);
			if(nn.compareTo(n)==-1)
			{//System.out.println("Size " + tekst);
			return size;
			}
			size=size-1;
			if(size==0){//System.out.println("Size2 " + tekst); 
			return size;} 
		  }
		
	}
	public  Boolean polish(String t)

    {

        return ( t.equals("¹") ||  t.equals("¥")  || t.equals("ê")  ||   t.equals("Ê") ||   t.equals("œ") ||

            t.equals("Œ") ||  t.equals("¿") ||  t.equals("¯") || t.equals("æ") || t.equals("Æ") ||  t.equals("Ÿ") || t.equals("") ||  t.equals("ñ") ||

            t.equals("Ñ") ||t.equals("ó") || t.equals("Ó") || t.equals("³") || t.equals("£") );

    }  
	
	public byte[][] encryptTotal(String  tekst, BigInteger ee, BigInteger nn)
	{    BigInteger e;
		e=ee;
		BigInteger n;
     	n=nn;
		String sizeTest=n.toString();
	    
	    byte[]pom;
	    int size=size(tekst,n);
		String s=tekst;
		String first,second;
		int sizetab=(s.length());
		byte[][] result=new byte[sizetab][];
		int i=0;
		int size2;
		while(s.length()>0)
		{String min=s.substring(0, 1);
		if(polish(min))  { size2=size(s.substring(1,s.length()), n); }
		else size2=size(s, n);
			if(s.length()>size2) 
				{
				first = s.substring(0, size2);
				 min=first.substring(0, 1);
				if(polish(min))  { first="?"+s.substring(1,size2);
				  result[i]= encrypt(first.getBytes(),e,n);
				 System.out.println(" ddd2");
				 second = s.substring(size2, s.length());
					s=second;
					i=i+1;
				}
				else{
				//System.out.println("Encrypted String in Bytes: " + bytesToString(result[0]) + " " + first);
			//	result[i] = new byte[size2];
				//System.out.print("Size: " + first);
				//System.out.println("...");
				result[i]= encrypt(first.getBytes(),e,n);
			// result[i]// gives "How ar"
				second = s.substring(size2, s.length());
				s=second;
				i=i+1;
				}
				
			}
			else 
			{
				//result[i] = new byte[size2];
			result[i]= encrypt(s.getBytes(),e,n);

			return result;
			
			}
		
		}
		
		
		return result;
		
		
	}
	
	public byte[] decryptTotal(byte[][]  tekst, BigInteger dd, BigInteger nn)
	{  BigInteger d;
	    d=dd;
	    BigInteger n;
	     n=nn;
	    byte[] result;
	    byte[] pom;
	    int size=tekst.length;
	    if(!(tekst[0]==null)) {} 
	    {
	    	result=decrypt(tekst[0],d,n);
	   
	    	for(int i=1; i<size;i++)
	    		{ if(!(tekst[i]==null))
	    			{pom=decrypt(tekst[i],d,n);
	    				result=copy(result,pom);
	    			}
	    		}
	    	return result;
	    }
		
		
		
		
		//return null;
		
		
	}
	
	
	static String readFile(String path, Charset encoding) 
			 
			{
			  byte[] encoded;
			try {
				encoded = Files.readAllBytes(Paths.get(path));
				 return new String(encoded, encoding);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error");
   		     System.exit(0);
				return " ";
			}
			 
			}
	
	static void writeFile(String path,  byte[] decoded) 
			 
			{
		try {
			Files.write(Paths.get(path), decoded);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
			}
	
	static void writeFile(String path,  String decoded) 
			  
			{
		try {
			Files.write(Paths.get(path), decoded.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
			}

 
public static void main(String[] args) {

    try {

      if (args[0].equals("generate")) {
    	  int k,d;
    	  if (!args[1].isEmpty() && !args[2].isEmpty()) 
    	  {  try{
    		  		k=Integer.parseInt(args[2]);
    		  		d=Integer.parseInt(args[2]);
    		  		generateKey(k,d);
    	  		}
    	  	catch(Exception e)
    	  		{
    		     System.out.println("Error1");
    		     e.printStackTrace();
    		     System.exit(0);
    	  		}
    	  System.exit(0);
    	  }
      }
      String readFile, saveFile;
      readFile="tekst.txt";
      saveFile="tekst2.txt";
    
        rsa rsa = new rsa();
        String data = null;
		 int ilosc;
		
		 if (args[0].equals("encrypt")) {
	    	  int k,d;
	    	  if (!args[2].isEmpty() && !args[1].isEmpty()) 
	    	  {  try{
	    		  
	    		  data = readFile(args[1], Charset.defaultCharset());
	    		  String data2 = readFile("public.key", Charset.defaultCharset());
 		  		 	String[] key;
 		  		 	key=data2.split(";");
 		  		 	BigInteger ee,nn;
 		  		 	ee= new BigInteger(key[0]);
 		  		 	nn= new BigInteger(key[1]);
	    			
	    			String teststring ;
	    		    Liczby lp=new Liczby();
	    			teststring=data;
	    			//System.out.println("Encrypting String: " + teststring);
	    			//System.out.println("String in Bytes: " + bytesToString(teststring.getBytes()));
	    			// encrypt
	    			byte[][] encrypted = rsa.encryptTotal(teststring,ee,nn);
	    			
	    			//System.out.println("Encrypted String in Bytes: " + bytesToString(encrypted[0]));
	    			FileOutputStream fileOutputStream = new FileOutputStream( args[2]);

	    			ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
	    			
	    			oos.writeObject(encrypted);
	    		  		
	    		  		
	    	  		}
	    	  	catch(Exception e)
	    	  		{
	    	  		System.out.println("Error");
	    	  		e.printStackTrace();
	    		     System.exit(0);
	    	  		}

	    	  }
	    	  System.exit(0);
	      }
		 
		 
		 if (args[0].equals("decrypt")) {
	    	  int k,d;
	    	  if (!args[1].isEmpty() && !args[2].isEmpty() ) 
	    	  {  try{  // System.out.println("Decrypted String in Bytes: " + args[1]+ " " + args[2]);
	    		  		
	    		  		 data = readFile("private.key", Charset.defaultCharset());
	    		  		 String[] key;
	    		  		 key=data.split(";");
	    		  		 BigInteger dd,nn;
	    		  		 dd= new BigInteger(key[0]);
	    		  		 nn= new BigInteger(key[1]);
	    		  		
	    		  		FileInputStream fileInputStream = new FileInputStream(args[1]);
	    				ObjectInputStream ois = new ObjectInputStream(fileInputStream);
	    				byte[][] encrypted2 = (byte[][])ois.readObject();
	    				byte[] decrypted2 = rsa.decryptTotal(encrypted2,dd,nn);
	    				//System.out.println("Decrypted String in Bytes: " + key[1]+ bytesToString(decrypted2));
	    			//	System.out.println("Decrypted String: " + new String(decrypted2, Charset.defaultCharset()));
	    				
	    				writeFile(args[2], decrypted2);
	    		  		
	    	  		}
	    	  	catch(Exception e)
	    	  		{
	    		     System.out.println("Error7");
	    		     e.printStackTrace();
	    		     System.exit(0);
	    	  		}
	    	  System.exit(0);
	    	  }
	      }	
		
	
		//byte[] decrypted = rsa.decryptTotal(encrypted);
		//System.out.println("Decrypted String in Bytes: " +  bytesToString(decrypted));
		//System.out.println("Decrypted String: " + new String(decrypted));
		
		
		//FileInputStream fileInputStream = new FileInputStream( "ENCRYPT.DATA");
		//ObjectInputStream ois = new ObjectInputStream(fileInputStream);
		//byte[][] encrypted2 = (byte[][])ois.readObject();
		//byte[] decrypted2 = rsa.decryptTotal(encrypted2,);
		//System.out.println("Decrypted String in Bytes: " +  bytesToString(decrypted2));
		//System.out.println("Decrypted String: " + new String(decrypted2, Charset.defaultCharset()));
		//writeFile(saveFile, decrypted2);
	      
		
     
    		} catch (Exception e) {
    				e.printStackTrace();
    			}
    
    System.out.println(" Koniec. ");
    System.exit(0);
  }
  
}