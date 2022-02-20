package main.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.data.FormatData;
import main.math.Mat;
import main.math.Vec;

public abstract class Network implements Runnable{
	
	private int[] sizes;

	private int length;

	private Mat[] weights;

	private Vec[] biases;

	/**Constructs a simple perceptron based feedforward neural network.
	 * 
	 * @param sizes array of ints specifying the size of each layer
	 */
	public Network(int[] sizes) {
		
		this.sizes = sizes;
		
		this.length = sizes.length;
		
		biases = new Vec[length - 1];

		weights = new Mat[length - 1];
		
	}
	
	/**Initialization method for initializing weights and biases to either random values
	 * or zeroes.
	 * 
	 * @param biases bias vectors in the network
	 * @param weights weight matrices in the network
	 * @param rand boolean to randomize weights and biases or set to zero
	 */
	public void init(Vec[] biases, Mat[] weights, boolean rand) {
		
		if (rand) {
			
			for (int i = 0; i < getLength() - 1; i++) {

				biases[i] = new Vec(getSizes()[i + 1]);

				weights[i] = new Mat(getSizes()[i + 1], getSizes()[i]);

				for (int k = 0; k < getSizes()[i + 1]; k++) {

					biases[i].set(k, (float) Math.random() * 2 - 1);

					for (int j = 0; j < getSizes()[i]; j++) {

						weights[i].set(k, j, (float) Math.random() * 2 - 1);

					}

				}

			}
			
		} else {
			
			for (int i = 0; i < getLength() - 1; i++) {

				biases[i] = new Vec(getSizes()[i + 1]);

				weights[i] = new Mat(getSizes()[i + 1], getSizes()[i]);

				for (int k = 0; k < getSizes()[i + 1]; k++) {

					biases[i].set(k, 0.0f);

					for (int j = 0; j < getSizes()[i]; j++) {

						weights[i].set(k, j, 0.0f);

					}

				}

			}
			
		}
		
	}
	
	/**Stochastic gradient descent method. Applies training over smaller, more manageable, batches of data
	 * with a parameter to adjust for overshoot.
	 * 
	 * @param data training data
	 * @param epochs number of epochs of training
	 * @param batchSize size of each batch 
	 * @param eta overshoot parameter
	 * @param testData testing data not used in training
	 */
	public void sgd(List<FormatData> data, int epochs, int batchSize, float eta, List<FormatData> testData) {

		int n = data.size();

		for (int i = 0; i < epochs; i++) {

			Collections.shuffle(data);

			FormatData[] batch = new FormatData[batchSize];

			for (int k = 0; k < n; k += batchSize) {

				data.subList(k, k + batchSize).toArray(batch);

				update(batch, eta);

			}
			
			System.out.println("Epoch {" + i + "}: ");
			
			eval(testData);

		}
		
		System.out.println("Complete.");

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

			dbdw = backprop(batch[i].getImage(), batch[i].getActivation());
			
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
	
	/**Feed {@code Vec} input data through the model. Use cost function and {@code Vec} 
	 * comparison to determine change in weights and biases. Returns the change in weights
	 * and biases as a {@code Levels} object.
	 * 
	 * @param input {@code Vec} input data
	 * @param compare {@code Vec} comparison data
	 * @return a {@code Levels} object containing the change in weights and biases
	 */
	public Levels backprop(Vec input, Vec compare) {

		Vec[] nabla_b = new Vec[getLength() - 1];
		
		Mat[] nabla_w = new Mat[getLength() - 1];
		
		init(nabla_b, nabla_w, false);

		Vec a = input;

		List<Vec> zVecs = new ArrayList<Vec>();
		
		List<Vec> aVecs = new ArrayList<Vec>();
		
		aVecs.add(a);
		
		//Feed forward;

		for (int i = 0; i < getLength() - 1; i++) {

			a = a.mult(getWeights()[i]).add(getBiases()[i]);

			zVecs.add(a);

			a = a.sigmoid();
			
			aVecs.add(a);

		}
		
		//delta = (dC/da(L))(da(L)/dz(L))
		Vec delta = cost(aVecs.get(aVecs.size() - 1), compare).mult(zVecs.get(zVecs.size() - 1).sigmoidPrime());
		
		// (dC/db(L))
		nabla_b[getLength() - 2] = delta;
		
		// (dC/dw(L))
		nabla_w[getLength() - 2] = delta.outer(aVecs.get(aVecs.size() - 2));
		
		for (int j = getLength() - 3; j >= 0; j --) {
		
			Vec z = zVecs.get(j);
			
			Vec sp = z.sigmoidPrime();
			
			Vec del = delta;
			
			delta = (del.mult(getWeights()[j + 1].transpose())).mult(sp);
			
			nabla_b[j] = delta;
			
			nabla_w[j] = delta.outer(aVecs.get(j));
			
		}
		
		Levels result = new Levels(nabla_b, nabla_w);

		return result;

	}
	
	/**Derivative of the cost function used in the model.
	 * 
	 * @param v {@code Vec} vector
	 * @param w {@code Vec} vector
	 * @return the {@code Vec} derivative of cost evaluated at input and comparison
	 */
	public static Vec cost(Vec v, Vec w) {
		
		Vec result = v.add(w.scale(-1.0f));
		
		return result;
		
	}
	
	/**Once the network has been trained, this method can be used to run data through
	 * the network and get results.
	 * 
	 * @param a input vector
	 * @return output vector from the network
	 */
	public Vec feedForward(Vec a) {

		for (int i = 0; i < getLength() - 1; i++) {

			a = a.mult(getWeights()[i]).add(getBiases()[i]).sigmoid();

		}

		return a;

	}
	
	/**Once the network has been trained, this method can be used to run data through
	 * the network and get results.
	 * 
	 * @param a input vector as a byte array
	 * @return output vector from the network
	 */
	public Vec feedForward(byte[] a) {
		
		return feedForward(new Vec(a));
		
	}
	
	/**Once the network has been trained, this method can be used to run data through
	 * the network and get results.
	 * 
	 * @param a input vector as a float array
	 * @return output vector from the network
	 */
	public Vec feedForward(float[] a) {
		
		return feedForward(new Vec(a));
		
	}
	
	public abstract void eval(List<FormatData> testData);

	public int[] getSizes() {
		return sizes;
	}

	public void setSizes(int[] sizes) {
		this.sizes = sizes;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Mat[] getWeights() {
		return weights;
	}

	public void setWeights(Mat[] weights) {
		this.weights = weights;
	}

	public Vec[] getBiases() {
		return biases;
	}

	public void setBiases(Vec[] biases) {
		this.biases = biases;
	}
	
}
