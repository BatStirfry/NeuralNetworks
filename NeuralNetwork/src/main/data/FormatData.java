package main.data;

import main.math.Vec;

public class FormatData {
	
	private int label;
	
	private Vec activation;
	
	private Vec image;
	
	public FormatData(int label, float[] imageData) {
		
		this.label = label;
		
		this.activation = new Vec(10);
		
		initActivation();
		
		this.image = new Vec(imageData);
		
	}
	
	public Vec getImage() {
		
		return this.image;
		
	}
	
	public int getLabel() {
		
		return this.label;
		
	}
	
	public void initActivation() {
		
		for (int i = 0; i < 10; i ++) {
			
			activation.set(i, 0);
			
		}
		
		activation.set(this.label, 1.0f);
		
	}
	
	public Vec getActivation() {
		
		return this.activation;
	}
	
}
