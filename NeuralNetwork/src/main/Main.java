package main;

import main.network.Autoencoder;
import main.network.FeedForward;

public class Main {

	public static void main(String[] args) {
		
		//FeedForward network = new FeedForward(new int[] {784, 30, 10});
		
		Autoencoder network = new Autoencoder(new int[] {784, 100, 30, 100, 784});
		
		network.run();

	}

}
