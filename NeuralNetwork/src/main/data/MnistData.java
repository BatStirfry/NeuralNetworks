package main.data;

import main.math.Vec;

public class MnistData {

	private int label;
	
	private byte[] image;
	
	public MnistData(int label, byte[] imageData) {
		
		this.label = label;
		
		this.image = new byte[imageData.length];
		
		this.image = imageData;
		
	}
	
	public byte[] getImage() {
		
		return this.image;
		
	}
	
	public byte getImage(int i) {
		
		return this.image[i];
		
	}
	
	public int getLabel() {
		
		return this.label;
		
	}
	
	public Vec getActivation() {
		
		Vec activation = new Vec(10);
		
		for (int i = 0; i < activation.getLength(); i ++) {
			
			activation.set(i, 0);
			
			if (i == label) {
				
				activation.set(i, 1);
				
			}
			
		}
		
		return activation;
		
	}
	
	public FormatData toFormatData() {
		
		float[] image = new float[this.image.length];
		
		for (int i = 0; i < image.length; i ++) {
			
			image[i] = (float) (this.image[i] & 0xFF)/256.0f;
			
		}
		
		return new FormatData(label, image);
		
	}
	
}
