/*
 * (c) Samy Chambi and Daniel Lemire
 */

public class Main {
	
	/**
	 * This function was dveloped by Alessandro Colantonio
	 * see : https://github.com/metamx/extendedset/blob/master/src/main/java/it/uniroma3/mat/extendedset/utilities/BitCount.java#L44-L49 
	 * 
	 * Population count
	 * <p>
	 * It counts a single word
	 * 
	 * @param word
	 *            word to count
	 * @return population count
	 */
	public static int count(int word) {
		word -= ((word >>> 1) & 0x55555555);
		word = (word & 0x33333333) + ((word >>> 2) & 0x33333333);
		word = (word + (word >>> 4)) & 0x0F0F0F0F;
		return (word * 0x01010101) >>> 24;
	}

    public static int sumConciseCount(int[] ints) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=count(ints[k]);
        return card;
    }

    public static int sumIntegerBitCount(int[] ints) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=Integer.bitCount(ints[k]);
        return card;
    }
    
    public static int sumLongBitCount(long[] ints) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=Long.bitCount(ints[k]);
        return card;
    }
    
    public static int sumLookup(short[] ints, short shcount[]) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=shcount[ints[k]];
        return card;
    }

    public static void main(String[] args) {
       // performing a dry-run
        for(int n=100; n<=1000000; n*=10) {
            System.out.println("Dry-run Number of ints = "+n);
            DataGenerator gen = new DataGenerator(n);
            gen.setUniform();
            int ints[] = gen.getUniform(0.500);
            for(int i=0; i<ints.length; i++)
            	ints[i]&=0x7FFF; //To don't meet negative numbers when dealing with short integers
            int card = 0;
            for(int i=0; i<100; i++) {
                card += sumConciseCount(ints);
            }
            card = 0;
            for(int i=0; i<100; i++) {
                card += sumIntegerBitCount(ints);
            }
          //Trying Long.bitcount as what Roaring does
            long longInts[] = new long[ints.length/2];
            for(int i=0; i<ints.length; i+=2)
            	longInts[i/2]=(ints[i]<<32)|ints[i+1];
            for(int i=0; i<100; i++) {
                card = sumLongBitCount(longInts);
            }
            //trying O'Neil Lookup COUNT function
            short shortInts[] = new short[ints.length*2];
            for(int i=0; i<ints.length; i++) {
            	shortInts[2*i] = (short) (ints[i]>>16);
            	shortInts[2*i+1] = (short) (ints[i]);
            }
            card = 0;
            short[] shcount = new short[1<<16];
            for(int i=0; i<shcount.length; i++)
            	shcount[i]=(short) Integer.bitCount(i);
            for(int i=0; i<100; i++) {
                card = sumLookup(shortInts, shcount);
            }
        }
        
        //Launching the benchmarks
        for(int n=100; n<=10000000; n*=10) {
            System.out.println("Number of ints = "+n);
            long bef, after;
            DataGenerator gen = new DataGenerator(n);
            gen.setUniform();
            int ints[] = gen.getUniform(0.500);            
            for(int i=0; i<ints.length; i++)
            	ints[i]&=0x7FFF; //To don't meet negative numbers when dealing with short integers
            //trying count of Concise
            int card = 0;
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card = sumConciseCount(ints);
            }
            after = System.nanoTime();

            System.out.println("card = "+card+", Count Concise time = "+((after-bef)/100)+" nanosec");

            //Trying Integer.bitCount (popcnt)
            card = 0;
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card = sumIntegerBitCount(ints);
            }
            after = System.nanoTime();

            System.out.println("card = "+card+", Integer.bitCount(popcnt) time = "+((after-bef)/100)+" nanosec");
            
            //Trying Long.bitcount as what Roaring does
            card = 0;
            long longInts[] = new long[ints.length/2];
            for(int i=0; i<ints.length; i+=2)
            	longInts[i/2]=((long)ints[i]<<32)|(long)ints[i+1];
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card = sumLongBitCount(longInts);
            }
            after = System.nanoTime();

            System.out.println("card = "+card+", Long.bitCount time = "+((after-bef)/100)+" nanosec ");
            
            //Trying O'Neil Lookup COUNT function
            short shortInts[] = new short[ints.length*2];
            for(int i=0; i<ints.length; i++) {
            	shortInts[2*i] = (short) (ints[i]>>16);
            	shortInts[2*i+1] = (short) (ints[i]);
            }
            card = 0;
            short[] shcount = new short[1<<16];
            for(int i=0; i<shcount.length; i++)
            	shcount[i]=(short) Integer.bitCount(i);
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card = sumLookup(shortInts, shcount);
            }
            after = System.nanoTime();

            System.out.println("card = "+card+", O'Neil look-up count time = "+((after-bef)/100)+" nanosec");
        }
    }
}
