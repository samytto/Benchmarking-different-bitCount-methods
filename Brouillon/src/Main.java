
public class Main {

    public static int summe1(int[] ints) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=count(ints[k]);
        return card;
    }

    public static int summe2(int[] ints) {
        int card = 0;
        for(int k=0; k<ints.length; k++)
            card+=Integer.bitCount(ints[k]);
        return card;
    }

    public static void main(String[] args) {
        // performing a dry-run
        for(int n=100; n<=1000000; n*=10) {
            System.out.println("Dry-run Number of ints = "+n);
            DataGenerator gen = new DataGenerator(n);
            gen.setUniform();
            int ints[] = gen.getUniform(0.500);
            int card = 0;
            for(int i=0; i<100; i++) {
                card += summe1(ints);
            }
            card = 0;
            for(int i=0; i<100; i++) {
                card += summe2(ints);
            }

        }
        //launching the benchmarks
        for(int n=100; n<=10000000; n*=10) {
            System.out.println("Number of ints = "+n);
            long bef, after;
            DataGenerator gen = new DataGenerator(n);
            gen.setUniform();
            int ints[] = gen.getUniform(0.500);
            //trying count of concise
            int card = 0;
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card += summe1(ints);
            }
            after = System.nanoTime();

            System.out.println("card = "+card+", Count concise time = "+((after-bef)/100)+" nanosec");

            //trying Integer.bitCount (popcnt)
            card = 0;
            bef = System.nanoTime();
            for(int i=0; i<100; i++) {
                card += summe2(ints);
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
