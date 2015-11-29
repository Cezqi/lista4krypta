

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class rsa {
	

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
      writeFile( "key.key",  p+ ";" +q);
 			 

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

        return ( t.equals("π") ||  t.equals("•")  || t.equals("Í")  ||   t.equals(" ") ||   t.equals("ú") ||

            t.equals("å") ||  t.equals("ø") ||  t.equals("Ø") || t.equals("Ê") || t.equals("∆") ||  t.equals("ü") || t.equals("è") ||  t.equals("Ò") ||

            t.equals("—") ||t.equals("Û") || t.equals("”") || t.equals("≥") || t.equals("£") );

    }  
	
	public static BigInteger crt(BigInteger what, BigInteger p, BigInteger q, BigInteger m){
		   BigInteger dep, deq, qInverse, m1, m2, h;

		   dep = what.remainder(p.subtract(BigInteger.ONE));
		   deq = what.remainder(q.subtract(BigInteger.ONE));
		   qInverse =  p.modInverse(q);

		      BigInteger cDp = m.modPow(dep, p);
		      BigInteger cDq = m.modPow(deq, q);
		      BigInteger u = ((cDq.subtract(cDp)).multiply(qInverse)).remainder(q);
		      if (u.compareTo(BigInteger.ZERO) < 0) u = u.add(q);
		      return cDp.add(u.multiply(p));

		  
		}

		//Encrypt using d
		public static BigInteger encrypt2(BigInteger e, BigInteger p, BigInteger q,BigInteger m){
		    return crt(e,p,q,m);
		}

		//decrypt using e
		public static BigInteger decrypt2(BigInteger d, BigInteger p, BigInteger q,BigInteger m){
		    return crt(d,p,q,m);
		}
		
		
		public class TaskAsCallable implements Callable<byte[]>{
			 
		    
			private BigInteger d;
			private BigInteger pp;
			private BigInteger qq;
			
			private byte[] tekst;
		     
		   
		     
		    public TaskAsCallable(BigInteger d, BigInteger pp, BigInteger qq, byte[] tekst) {
				this.d=d;
				this.pp=pp;
				this.qq=qq;
				this.tekst=tekst;
			}

			public byte[] call() throws Exception {
		         
		        /* this is where your business logic goes */
		        return crt(d,pp,qq, new BigInteger(tekst)).toByteArray();
		    }
		}	
	
	
	public byte[][] encryptTotal(String  tekst, BigInteger ee, BigInteger nn, BigInteger pp, BigInteger qq, Boolean ctrif)
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
				  if(ctrif.equals(false)) result[i]= encrypt(first.getBytes(),e,n);
				  else
				  result[i]= encrypt2(e,pp,qq, new BigInteger(first.getBytes())).toByteArray();
				 //System.out.println(" ddd2");
				 second = s.substring(size2, s.length());
					s=second;
					i=i+1;
				}
				else{
				//System.out.println("Encrypted String in Bytes: " + bytesToString(result[0]) + " " + first);
			//	result[i] = new byte[size2];
				//System.out.print("Size: " + first);
				//System.out.println("...");
					 if(ctrif.equals(false))result[i]= encrypt(first.getBytes(),e,n);
				else  result[i]= encrypt2(e,pp,qq, new BigInteger(first.getBytes())).toByteArray();
			// result[i]// gives "How ar"
				second = s.substring(size2, s.length());
				s=second;
				i=i+1;
				}
				
			}
			else 
			{
				//result[i] = new byte[size2];
				 if(ctrif.equals(false)) result[i]= encrypt(s.getBytes(),e,n);
				 else result[i]= encrypt2(e,pp,qq, new BigInteger(s.getBytes())).toByteArray();

			return result;
			
			}
		
		}
		
		
		return result;
		
		
	}
	
	public byte[] decryptTotal(byte[][]  tekst, BigInteger dd, BigInteger nn, BigInteger pp, BigInteger qq,Boolean ctrif)
	{  BigInteger d;
	    d=dd;
	    BigInteger n;
	     n=nn;
	    byte[] result;
	    byte[] pom;
	    int size=tekst.length;
	    if(!(tekst[0]==null)) {} 
	    {
	    	 if(ctrif.equals(false))result=decrypt(tekst[0],d,n);
	    	 else result= decrypt2(d,pp,qq, new BigInteger(tekst[0])).toByteArray();
	    	 
	    	
	    	for(int i=1; i<size;i++)
	    		{ if(!(tekst[i]==null))
	    			{  if(ctrif.equals(false))pom=decrypt(tekst[i],d,n);
	    			 else{ 
	    				 
	    				 
	    				  pom= decrypt2(d,pp,qq, new BigInteger(tekst[i])).toByteArray();       
	    	     
	    			 
	    			 }
	    			 result=copy(result,pom);
	    			  
	    			}
	    		}
	    	return result;
	    }
		
		
		
		
		//return null;
		
		
	}
	
	public byte[][] encryptTotal2(String  tekst, BigInteger ee, BigInteger nn, BigInteger pp, BigInteger qq, Boolean ctrif, int lll)
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
		int how=0;
		 ExecutorService executorService = Executors.newFixedThreadPool(lll);
		 
         List<Callable<byte[]>> lst = new ArrayList<Callable<byte[]>>();
		while(s.length()>0)
		{String min=s.substring(0, 1);
		
		
		if(polish(min))  { size2=size(s.substring(1,s.length()), n); }
		else size2=size(s, n);
			if(s.length()>size2) 
				{first = s.substring(0, size2);
				 min=first.substring(0, 1);
				if(polish(min))  { first="?"+s.substring(1,size2);	}else{}
				second = s.substring(size2, s.length());
				s=second;
			
				
				if(how<lll )
				{    
					lst.add(new TaskAsCallable(e,pp,qq, first.getBytes()));
					how=how+1;	
					//System.out.println(how+ " " + i);
					
				}
			else
				{ List<Future<byte[]>> tasks;
					try {
							tasks = executorService.invokeAll(lst);
								//System.out.println( " ;" +tasks.size() +" Responses recieved.\n");
		      
								for(Future<byte[]> task : tasks)
									{result[i]=task.get();
									i=i+1;
									}
								lst = new ArrayList<Callable<byte[]>>();
								how=0;
								
								lst.add(new TaskAsCallable(e,pp,qq, first.getBytes()));
								how=how+1;	
								
							} catch (InterruptedException | ExecutionException e1) {
								// TODO Auto-generated catch block
									e1.printStackTrace();
							}
			
				}
				
				
			}
			else 
			{   List<Future<byte[]>> tasks;
			try {
				tasks = executorService.invokeAll(lst);
			//	System.out.println(" ;" +tasks.size() +" Responses recieved.\n");
			      
				for(Future<byte[]> task : tasks)
					{result[i]=task.get();
					i=i+1;
					}
				result[i]= encrypt2(e,pp,qq, new BigInteger(s.getBytes())).toByteArray();

				return result;
				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
				}
				
			
			}
		
		}
		
		
		return result;
		
		
	}
	
	
	public byte[] decryptTotal2(byte[][]  tekst, BigInteger dd, BigInteger nn, BigInteger pp, BigInteger qq,Boolean ctrif, int lll)
	{  BigInteger d;
	    d=dd;
	    BigInteger n;
	     n=nn;
	    byte[] result;
	    byte[] pom;
	    int size=tekst.length;
	    int how=0;
	    if(!(tekst[0]==null)) {} 
	    {   result= decrypt2(d,pp,qq, new BigInteger(tekst[0])).toByteArray();
	    	 
	    	 ExecutorService executorService = Executors.newFixedThreadPool(lll);
	         
	         List<Callable<byte[]>> lst = new ArrayList<Callable<byte[]>>();
	       
	    	for(int i=1; i<size;i++)
	    		{ if(!(tekst[i]==null))
	    			{  
	    				if(how<lll )
	    					{
	    						lst.add(new TaskAsCallable(d,pp,qq, tekst[i]));
	    						how=how+1;	
	    						
	    					}
	    				else
	    					{ List<Future<byte[]>> tasks;
	    						try {
	    								tasks = executorService.invokeAll(lst);
	    									//System.out.println(tasks.size() +" Responses recieved.\n");
	    		          
	    									for(Future<byte[]> task : tasks)
	    										{
	    											pom=task.get();
	    											result=copy(result,pom);
	    											}
	    									lst = new ArrayList<Callable<byte[]>>();
	    									how=0;
	    									
	    									lst.add(new TaskAsCallable(d,pp,qq, tekst[i]));
	    		    						how=how+1;	
	    									
	    								} catch (InterruptedException | ExecutionException e) {
	    									// TODO Auto-generated catch block
	    										e.printStackTrace();
	    								}
	    				
	    					}
	    			 
	    	     
	    			  
	    			}
	    		}
	    	
	    	List<Future<byte[]>> tasks;
			try {       tasks = executorService.invokeAll(lst);
						//System.out.println(tasks.size() +" Responses recieved.\n");
      
						for(Future<byte[]> task : tasks)
							{
								pom=task.get();
								result=copy(result,pom);
								}
					
					} catch (InterruptedException | ExecutionException e) {
						
							e.printStackTrace();
					}
	    	  executorService.shutdown();
	    	return result;
	      }
	    
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
    	Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min=rightNow.get(Calendar.MINUTE);
        System.out.println(" Poczπtek " + hour + " " +min);

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
    	  rightNow = Calendar.getInstance();
    	    hour = rightNow.get(Calendar.HOUR_OF_DAY);
    	    min=rightNow.get(Calendar.MINUTE);
    	    System.out.println(" Koniec. " + hour + " " +min);
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
	    	  if (!args[2].isEmpty() && !args[1].isEmpty()  && !args[3].isEmpty()  && !args[4].isEmpty()) 
	    	  {  try{ k=8;
	    		  try{
	    		  		k=Integer.parseInt(args[4]);
	    		  	
	    	  		}
	    	  	catch(Exception e)
	    	  		{
	    		     System.out.println("Error8");
	    		     e.printStackTrace();
	    		     
	    	  		}
	    		  
	    		  data = readFile(args[1], Charset.defaultCharset());
	    		  String data2 = readFile("public.key", Charset.defaultCharset());
 		  		 	String[] key;
 		  		 	key=data2.split(";");
 		  		 	BigInteger ee,nn;
 		  		 	ee= new BigInteger(key[0]);
 		  		 	nn= new BigInteger(key[1]);
 		  		 	String data3 = readFile("key.key", Charset.defaultCharset());
 		  		 	String[] key2;
 		  		 	key2=data3.split(";");
 		  		 	//System.out.println(key2[0]);
 		  		 	BigInteger pp,qq;
 		  		 	pp= new BigInteger(key2[0]);
 		  		 	qq= new BigInteger(key2[1]);
	    			
	    			String teststring ;
	    		    Liczby lp=new Liczby();
	    			teststring=data;
	    			//System.out.println("Encrypting String: " + teststring);
	    			//System.out.println("String in Bytes: " + bytesToString(teststring.getBytes()));
	    			// encrypt
	    			byte[][] encrypted;
	    			if(args[3].equals(true)) encrypted= rsa.encryptTotal2(teststring,ee,nn,pp,qq, args[3].equals(true),k);
	    			else encrypted= rsa.encryptTotal(teststring,ee,nn,pp,qq, args[3].equals(true));
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
	    	  rightNow = Calendar.getInstance();
	    	    hour = rightNow.get(Calendar.HOUR_OF_DAY);
	    	     min=rightNow.get(Calendar.MINUTE);
	    	    System.out.println(" Koniec. " + hour + " " +min);
	    	  System.exit(0);
	      }
		 
		 
		 if (args[0].equals("decrypt")) {
	    	  int k,d;
	    	  if (!args[1].isEmpty() && !args[2].isEmpty()  && !args[3].isEmpty()  && !args[4].isEmpty()) 
	    	  {  try{  // System.out.println("Decrypted String in Bytes: " + args[1]+ " " + args[2]);
	    		  k=8;
	    		  try{
	    		  		k=Integer.parseInt(args[4]);
	    		  	
	    	  		}
	    	  	catch(Exception e)
	    	  		{
	    		     System.out.println("Error8");
	    		     e.printStackTrace();
	    		     
	    	  		}
	    		  		 data = readFile("private.key", Charset.defaultCharset());
	    		  		 String[] key;
	    		  		 key=data.split(";");
	    		  		 BigInteger dd,nn;
	    		  		 dd= new BigInteger(key[0]);
	    		  		 nn= new BigInteger(key[1]);
	    		  		String data3 = readFile("key.key", Charset.defaultCharset());
	 		  		 	String[] key2;
	 		  		 	key2=data3.split(";");
	 		  		 	BigInteger pp,qq;
	 		  		 	pp= new BigInteger(key2[0]);
	 		  		 	qq= new BigInteger(key2[1]);
	    		  		
	    		  		FileInputStream fileInputStream = new FileInputStream(args[1]);
	    				ObjectInputStream ois = new ObjectInputStream(fileInputStream);
	    				byte[][] encrypted2 = (byte[][])ois.readObject();
	    				byte[] decrypted2;
	    				if(args[3].equals(true))
	    					 decrypted2 = rsa.decryptTotal2(encrypted2,dd,nn,pp,qq,args[3].equals(true),k);
	    				else  decrypted2 = rsa.decryptTotal(encrypted2,dd,nn,pp,qq,args[3].equals(true));
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
	    	 rightNow = Calendar.getInstance();
	    	    hour = rightNow.get(Calendar.HOUR_OF_DAY);
	    	     min=rightNow.get(Calendar.MINUTE);
	    	    System.out.println(" Koniec. " + hour + " " +min);
	    	   
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
    Calendar rightNow = Calendar.getInstance();
    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
    int min=rightNow.get(Calendar.MINUTE);
    System.out.println(" Koniec. " + hour + " " +min);
    System.exit(0);
  }
  
}