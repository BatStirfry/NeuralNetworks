package main.math;

public class Mat {
	
	private int i, j;
	
	private float[][] m;
	
	public Mat(int i, int j) {
		
		this.i = i;
		
		this.j = j;
		
		this.m = new float[i][j];
		
	}
	
	public Mat scale(float c) {
		
		Mat result = new Mat(i, j);
		
		for (int i = 0; i < this.i; i ++) {
			
			for (int j = 0; j < this.j; j++) {
				
				result.set(i, j, c * get(i,j));
				
			}
			
		}
		
		return result;
		
	}
	
	public Mat add(Mat n) {
		
		if (i != n.getI() || j != n.getJ()) {
			
			return null;
			
		}
		
		Mat result = new Mat(i, j);
		
		for (int i = 0; i < this.i; i ++) {
			
			for (int j = 0; j < this.j; j++) {
				
				result.set(i, j, get(i,j) + n.get(i, j));
				
			}
			
		}
		
		return result;
		
	}
	
	public Mat mul(Mat n) {
		
		if(this.j != n.getI()) {
			
			return null;
			
		}
		
		Mat result = new Mat(this.i, n.getJ());
		
		float val = 0;
		
		for (int i = 0; i < this.i; i ++) {
			
			for (int j = 0; j < n.getJ(); j ++) {
				
				val = 0;
				
				for (int k = 0; k < this.j; k ++) {
					
					for (int l = 0; l < n.getI(); l ++) {
						
						val += this.get(i, k) * n.get(l, j);
						
					}
					
				}
				
				result.set(i, j, val);
				
			}
			
		}
		
		return result;
		
	}
	
	public Mat transpose() {
		
		Mat result = new Mat(j, i);
		
		for (int i = 0; i < this.i; i ++) {
			
			for (int j = 0; j < this.j; j++) {
				
				result.set(j, i, get(i,j));
				
			}
			
		}
		
		return result;
		
	}
	
	public void print() {
		
		for (int i = 0; i < this.i; i ++) {
			
			System.out.print("[");
			
			for (int j = 0; j < this.j; j ++) {
				
				System.out.print(get(i, j));
				
				if (j < this.j - 1) {
					
					System.out.print(", ");
					
				}
				
			}
			
			System.out.println("]");
			
		}
		
		System.out.println();
		
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public float get(int i, int j) {
		return m[i][j];
	}

	public void set(int i, int j, float m) {
		this.m[i][j] = m;
	}
	
	
	
	

}
