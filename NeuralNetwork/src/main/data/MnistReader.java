package main.data;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MnistReader {

	private String imageFile;
	private String labelFile;
	private List<MnistData> data;
	private List<FormatData> formatData;

	private static final int OFF_SIZE = 4;
	private static final int MAGIC_OFF = 0;
	private static final int NUM_ITM_OFF = 4;
	private static final int ITEM_SIZE = 4;

	private static final int LABEL_MAGIC_NUM = 2049;

	private static final int IMAGE_MAGIC_NUM = 2051;
	private static final int NUM_ROW_OFF = 8;
	private static final int NUM_COL_OFF = 12;
	private static final int IMAGE_OFF = 16;

	private static final int NUM_ROW_VAL = 28;
	private static final int NUM_COL_VAL = 28;
	private static final int IMAGE_SIZE = NUM_ROW_VAL * NUM_COL_VAL;

	public MnistReader(String imageFile, String labelFile) {

		this.labelFile = labelFile;
		this.imageFile = imageFile;

		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void read() throws IOException {

		data = new ArrayList<MnistData>();
		formatData = new ArrayList<FormatData>();

		ByteArrayOutputStream labelBuffer = new ByteArrayOutputStream();
		ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
		
		FileInputStream labelInputStream = new FileInputStream("./res/data/" + labelFile);
		FileInputStream imageInputStream = new FileInputStream("./res/data/" + imageFile);

		int read;

		byte[] buffer = new byte[16384];

		while ((read = labelInputStream.read(buffer, 0, buffer.length)) != -1) {

			labelBuffer.write(buffer, 0, read);

		}

		labelBuffer.flush();

		System.out.println("Labels read!");
		
		while ((read = imageInputStream.read(buffer, 0, buffer.length)) != -1) {

			imageBuffer.write(buffer, 0, read);

		}

		imageBuffer.flush();

		System.out.println("Images read!");
		
		labelInputStream.close();
		imageInputStream.close();
		
		byte[] labelBytes = labelBuffer.toByteArray();
		byte[] imageBytes = imageBuffer.toByteArray();

		byte[] labelMagic = Arrays.copyOfRange(labelBytes, MAGIC_OFF, OFF_SIZE);
		byte[] imageMagic = Arrays.copyOfRange(imageBytes, MAGIC_OFF, OFF_SIZE);

		if (ByteBuffer.wrap(labelMagic).getInt() != LABEL_MAGIC_NUM) {

			throw new IOException("Label magic number was not found at beginning of file!");

		}

		if (ByteBuffer.wrap(imageMagic).getInt() != IMAGE_MAGIC_NUM) {

			throw new IOException("Image magic number was not found at beginning of file!");

		}

		int numLabels = ByteBuffer.wrap(Arrays.copyOfRange(labelBytes, NUM_ITM_OFF, NUM_ITM_OFF + ITEM_SIZE)).getInt();
		int numImages = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUM_ITM_OFF, NUM_ITM_OFF + ITEM_SIZE)).getInt();

		if (numLabels != numImages) {

			throw new IOException("Number of labels does not match number of images!");

		}

		int numRows = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUM_ROW_OFF, NUM_ROW_OFF + ITEM_SIZE)).getInt();
		int numCols = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUM_COL_OFF, NUM_COL_OFF + ITEM_SIZE)).getInt();

		if (numRows != NUM_ROW_VAL || numCols != NUM_COL_VAL) {

			throw new IOException("Number of rows or columns is incorrect!");

		}
		
		MnistData mnist;

		for (int i = 0; i < numLabels; i++) {

			int label = labelBytes[OFF_SIZE + ITEM_SIZE + i];

			byte[] imageData = Arrays.copyOfRange(imageBytes, IMAGE_OFF + (i * IMAGE_SIZE),
					IMAGE_OFF + IMAGE_SIZE + (i * IMAGE_SIZE));
			
			mnist = new MnistData(label, imageData);
			
			data.add(mnist);
			
			formatData.add(mnist.toFormatData());

		}
		
	}
	
	public List<MnistData> getData(){
		
		return this.data;
		
	}
	
	public List<FormatData> getFormatData(){
		
		return this.formatData;
		
	}

}
