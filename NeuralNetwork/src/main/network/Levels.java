package main.network;

import main.math.Mat;
import main.math.Vec;

public class Levels {
	
	private Vec[] biases;
	
	private Mat[] weights;
	
	public Levels(Vec[] biases, Mat[] weights) {
		
		this.biases = biases;
		
		this.weights = weights;
		
	}

	public Vec[] getBiases() {
		return biases;
	}

	public void setBiases(Vec[] biases) {
		this.biases = biases;
	}

	public Mat[] getWeights() {
		return weights;
	}

	public void setWeights(Mat[] weights) {
		this.weights = weights;
	}
	
}
