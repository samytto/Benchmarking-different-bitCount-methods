
public class Main {

	public static void main(String[] args) {
		
		for(int n=100; n<=10000000; n*=10) {
		System.out.println("Number of ints = "+n);
		long bef, after;
		DataGenerator gen = new DataGenerator(n);
		gen.setUniform();
		int ints[] = gen.getUniform(0.500);
		//trying count of concise
		int card = 0;
		bef = System.nanoTime();
		for(int i=0; i<100; i++){
			for(int k=0; k<n; k++)
				card+=count(ints[k]);
		}
		after = System.nanoTime();
		
		System.out.println("card = "+card+", Count concise time = "+((after-bef)/100)+" nanosec");
		
		//trying Integer.bitCount (popcnt)
		 card = 0;
		 bef = System.nanoTime();
		 for(int i=0; i<100; i++){
			 for(int k=0; k<n; k++)
				card+=Integer.bitCount(ints[k]);
			}
				after = System.nanoTime();
				
				System.out.println("card = "+card+", Integer.bitCount(popcnt) time = "+((after-bef)/100)+" nanosec");
		}
	}
	
	/**
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

}
