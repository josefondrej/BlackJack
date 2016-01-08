package maxExpectation;

import java.awt.font.NumericShaper.Range;
import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;



public class Draft {

	public double pow2(double x){
		return x*x;
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		double[] cards = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		

		Arrays.stream(cards).parallel().forEach();
		
		
	}

}
