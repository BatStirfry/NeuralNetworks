package main.math;

public class Vec {
	
	private int length;
	
	private float[] v;
	
	public Vec(int length) {
		
		this.length = length;
		
		v = new float[length];
		
	}
	
	public Vec(byte[] array) {
		
		this.length = array.length;
		
		v = new float[length];
		
		for (int i = 0; i < length; i ++) {
			
			v[i] = array[i];
			
		}
		
	}
	
	public Vec(float[] array) {
		
		this.length = array.length;
		
		v = array;
		
	}
	
	public Vec scale(float c) {
		
		Vec result = new Vec(length);
		
		for (int i = 0; i < length; i ++) {
			
			result.set(i, c * get(i));
			
		}
		
		return result;
		
	}
	
	public float length() {
		
		float result = 0;
		
		for (int i = 0; i < this.length; i ++) {
			
			result += get(i) * get(i);
			
		}
		
		return (float) Math.sqrt(result);
		
	}
	
	public float lengthSquared() {
		
		float result = 0;
		
		for (int i = 0; i < this.length; i ++) {
			
			result += get(i) * get(i);
			
		}
		
		return result;
		
	}
	
	public Vec add(float c) {
		
		Vec result = new Vec(length);
		
		for (int i = 0; i < length; i ++) {
			
			result.set(i, c + get(i) );
			
		}
		
		return result;
		
	}
	
	public Vec add(Vec w) {
		
		if(length != w.length) {
			
			return null;
			
		}
		
		Vec result = new Vec(length);
		
		for (int i = 0; i < length; i ++) {
			
			result.set(i, get(i) + w.get(i));
			
		}
		
		return result;
		
	}
	
	public Mat outer(Vec w) {
		
		Mat result = new Mat(length, w.getLength());
		
		float val;
		
		for (int i = 0; i < length; i ++) {
			
			for (int j = 0; j < w.getLength(); j ++) {
				
				val = get(i) * w.get(j);
				
				result.set(i, j, val);
				
			}
			
		}
		
		return result;
		
	}
	
	public Vec mult(Mat m) {
		
		if (length != m.getJ()) {
			
			return null;
			
		}
		
		Vec result = new Vec(m.getI());
		
		float val;
		
		for (int i = 0; i < m.getI(); i ++) {
			
			val = 0;
				
			for (int k = 0; k < this.length; k ++) {
				
				val += this.get(k) * m.get(i, k);
				
			}
			
			result.set(i, val);
			
		}
		
		return result;
		
	}
	
	public Vec mult(Vec w) {
		
		if (length != w.getLength()) {
			
			return null;
			
		}
		
		Vec result = new Vec(length);
		
		for (int i = 0; i < length; i ++) {
			
			result.set(i, get(i) * w.get(i));
			
		}
		
		return result;
		
	}
	
	public Vec sigmoid() {
		
		Vec result = new Vec(this.length);
		
		for (int i = 0; i < this.length; i ++) {
			
			result.set(i, (float) (1.0f/(float)(1.0f + (float) Math.exp(-get(i)))));
			
		}
		
		return result;
		
	}
	
	public Vec sigmoidPrime() {
		
		Vec result = new Vec(this.length);
		
		for (int i = 0; i < this.length; i ++) {
			
			result.set(i, (float) (1.0f/(Math.exp(get(i)) + Math.exp(-get(i)) + 2)));
			
		}
		
		return result;
		
	}
	
	public float dot(Vec w) {
		
		if (this.length != w.length) {
			
			return 0;
			
		}
		
		float result = 0;
		
		for(int i = 0; i < this.length; i ++) {
			
			result += this.get(i) * w.get(i);
			
		}
		
		return result;
		
	}
	
	public void print() {
		
		System.out.print("[");
		
		for (int i = 0; i < this.length; i ++) {
			
			System.out.print(get(i));
			
			if (i < this.length - 1) {
				
				System.out.print(", ");
				
			}
			
		}
		
		System.out.println("]^T");
		
		System.out.println();
		
	}

	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		
		this.length = length;
		
		this.v = new float[length];
		
	}

	public float get(int i) {
		return v[i];
	}

	public void set(int i, float val) {
		this.v[i] = val;
	}	

}
