import java.math.BigInteger;
import java.security.SecureRandom;
import java.lang.Thread;
 
public class Liczby {
 
    
 
       
        
        public Liczby(){}
 
        private static final BigInteger ZERO = BigInteger.ZERO;
    	private static final BigInteger JEDEN = BigInteger.ONE;
    	private static final BigInteger DWA = new BigInteger("2");
    	private static final BigInteger TRZY = new BigInteger("3");
    	
    	public static boolean czyLiczbaPierwsza(BigInteger n)
    	{
    		return czyLiczbaPierwsza( n, 20);
    		
    	}
//Millera-Rabina
    	
    	public static boolean czyLiczbaPierwsza(BigInteger n, int k) {
    		if (n.compareTo(TRZY) < 0)
			return false;
    		
    		if (ZERO.equals((n.mod(DWA))) )
    			return false;
    		int s = 0;
    		BigInteger d = n.subtract(JEDEN);
    		while ((d.mod(DWA)).equals(ZERO)) {
    			s++;
    			d = d.divide(DWA);
    		}
    		LOOP:
    		for (int i = 0; i < k; i++) {
    			
    			BigInteger a = losuj(DWA, n.subtract(JEDEN));
    			BigInteger x = a.modPow(d, n);
    			if (x.equals(JEDEN) || x.equals(n.subtract(JEDEN)))
    				continue;
    			int r=1;
    			
    			for ( ; r < s; r++) {
    				
    				 x = x.modPow(DWA, n);
    				if (x.equals(JEDEN))
    					return false;
    				if (x.equals(n.subtract(JEDEN)))
    					continue LOOP;
    			}
    			if(r==s) return false;
    		}
    	   
    		return true;
    	}

    	private static BigInteger losuj(BigInteger bottom, BigInteger top) {
    		SecureRandom los = new SecureRandom();
    		BigInteger liczba;
    		do {
    			liczba = new BigInteger(top.bitLength(), los);
    		} while (liczba.compareTo(bottom) < 0 || liczba.compareTo(top) > 0);
    		return liczba;
    	}
 
        public BigInteger generuj(int d) {
                SecureRandom los = new SecureRandom();
                BigInteger liczba;
                do {
                        liczba = new BigInteger(d, los);
                      //  System.out.println("Start" + "  " + liczba);
                } while(!czyLiczbaPierwsza(liczba));
                //System.out.println("Numb");
 
                return liczba;
        }
        
        public Thread w¹tek(int i, int size) 
        {
        	Thread t= new Thread() {
                 public void run() {
                         System.out.println("Liczba" + i + " = " + generuj(size));
                 }
         };
        	t.start();
        	return t;
        }
        
        public void szukajLiczb(int iloœæ,int wielkoœæ) 
        {   Thread[] tab = new Thread[iloœæ];
        	  for(int i = 0; i < iloœæ; i++) 
        	  {
        		  tab[i]= w¹tek(i, wielkoœæ);
                
              }  
        	  for(int i = 0; i < iloœæ; i++) 
        	  {
        		  try {
					tab[i].join();
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
              } 
        	  System.out.println("Koniec");
        	
        }
        
        
 
        public static void main(String args[]) {
                if (args.length < 2)
                        return;
                System.out.println("Start" );
                int k = Integer.parseInt(args[0]);
                 int d = Integer.parseInt(args[1]);
                 Liczby lp=new Liczby();
                lp.szukajLiczb(k,d);
                     
               
        }
}