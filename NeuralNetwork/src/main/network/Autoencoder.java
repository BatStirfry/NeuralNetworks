package main.network;

import java.util.List;

import main.data.FormatData;
import main.data.MnistReader;
import main.math.Mat;
import main.math.Vec;

public class Autoencoder extends Network {

	/**Neural network that learns efficient encodings of data. Attempts to regenerate the input from
	 * the encoding, learning a representation of that set of data. Thus the decoder is a generative 
	 * model.
	 * 
	 * @param sizes  array of ints specifying the size of each layer, first element must equal last in
	 * an autoencoder
	 */
	public Autoencoder(int[] sizes) {
		
		super(sizes);
		
		if (sizes[0] != sizes[sizes.length - 1]) {
			
			System.err.println("Input layer must be same size as output layer");
			
			System.exit(0);
			
		}
		
	}

	@Override
	public void run() {
		
		init(getBiases(), getWeights(), true);

		MnistReader mnistTraining = new MnistReader("train-images-idx3-ubyte", "train-labels-idx1-ubyte");
		
		MnistReader mnistTest = new MnistReader("t10k-images-idx3-ubyte", "t10k-labels-idx1-ubyte");
		
		sgd(mnistTraining.getFormatData(), 20, 100, 2f, mnistTest.getFormatData());
		
	}
	
	/**Run the batch of data through the model. Backpropagate through the model. Obtain the 
	 * backpropagated change in weights and biases. Update the model with the change in weights
	 * and biases scaled by eta and the inverse of the batch size.
	 * 
	 * @param batch subset of training data to update the model with
	 * @param eta overshoot parameter
	 */ 
	public void update(FormatData[] batch, float eta) {

		Vec[] nabla_b = new Vec[getLength() - 1];
		
		Mat[] nabla_w = new Mat[getLength() - 1];
		
		init(nabla_b, nabla_w, false);
		
		Levels dbdw;

		for (int i = 0; i < batch.length; i++) {

			dbdw = backprop(batch[i].getImage(), batch[i].getImage());
			
			for (int j = 0; j < getLength() - 1; j ++) {
				
				nabla_b[j] = nabla_b[j].add(dbdw.getBiases()[j]);
				
				nabla_w[j] = nabla_w[j].add(dbdw.getWeights()[j]);
				
			}

		}
		
		for (int i = 0; i < getLength() - 1; i ++) {
			
			getBiases()[i] = getBiases()[i].add(nabla_b[i].scale(-eta/(batch.length)));
			
			getWeights()[i] = getWeights()[i].add(nabla_w[i].scale(-eta/(batch.length)));
			
		}

	}
	
	@Override
	public void eval(List<FormatData> testData) {
		
		FormatData test = testData.get((int) Math.floor(Math.random() * testData.size()));
		
		StringBuilder sBuilder = new StringBuilder();
		
		//Input
		sBuilder.append("Input data:\n");
		
		for(int i = 0; i < 784; i ++) {
			
			if(test.getImage().get(i) > 0.0f ) {
				
				sBuilder.append("%%");
				
			} else {
				
				sBuilder.append("  ");
				
			}
			
			if((i + 1) % 28 == 0) {
			
				sBuilder.append("\n");
				
			}
			
		}
		
		Vec a = test.getImage();
		
		Vec b = feedForward(a);
		
		float cost = 0.5f * a.add(b.scale(-1.0f)).lengthSquared();
		
		//Output
		sBuilder.append("Output data:\n");
		
		for(int i = 0; i < 784; i ++) {
			
			//System.out.println(aVecs.get(getLength() - 1).get(i));
			
			if(b.get(i) > 0.1f ) {
				
				sBuilder.append("%%");
				
			} else {
				
				sBuilder.append("  ");
				
			}
			
			if((i + 1) % 28 == 0) {
			
				sBuilder.append("\n");
				
			}
			
		}
		
		System.out.println(sBuilder.toString());
		
		System.out.println("Cost of this test: " + cost);
		
	}

}
