package main.network;

import java.util.List;

import main.data.FormatData;
import main.data.MnistReader;
import main.math.Vec;

public class FeedForward extends Network {

	/**An artificial neural network wherein connections between the nodes do not form a cycle.
	 * 
	 * @param sizes array of ints specifying the size of each layer
	 */
	public FeedForward(int[] sizes) {
		
		super(sizes);

	}

	@Override
	public void run() {

		init(getBiases(), getWeights(), true);

		MnistReader mnistTraining = new MnistReader("train-images-idx3-ubyte", "train-labels-idx1-ubyte");
		
		MnistReader mnistTest = new MnistReader("t10k-images-idx3-ubyte", "t10k-labels-idx1-ubyte");

		sgd(mnistTraining.getFormatData(), 20, 100, 2f, mnistTest.getFormatData());

	}

	@Override
	public void eval(List<FormatData> testData) {
		
		int count = 0;
		
		for (int i = 0; i < testData.size(); i ++) {
			
			Vec a = feedForward(testData.get(i).getImage());
			
			int argmax = -1;
			
			float max = Float.MIN_VALUE;
			
			for (int j = 0; j < a.getLength(); j ++) {
				
				max = Math.max(max, a.get(j));
				
				if (max == a.get(j)) {
					
					argmax = j;
					
				}
				
			}
			
			if (argmax == testData.get(i).getLabel()) {
				
				count ++;
				
			}
			
		}
		
		System.out.println(count + "/" + testData.size() + " : " + (float)count/testData.size() + "% success rate for this epoch.");
		
		System.out.println();
		
	}
	
}
