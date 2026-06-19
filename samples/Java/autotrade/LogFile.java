package autotrade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class LogFile {
//	public static void main(String[] args) {
//		LogFile f = new LogFile();
//		String data = "Files in Java might be tricky, but it is fun enough!";
//		
////		f.clearPlaceOrder();
//		f.appendPlaceOrder(data);
//
//		
//
//	}

	public void clearPlaceOrder() {
		String path = "\\TWS API\\samples\\Java\\autotrade\\";
		String filename = "placeOrder.txt";
		try {
			FileWriter myWriter = new FileWriter(path + filename);
			myWriter.write("");
			myWriter.close();
			System.out.println("Successfully clear data in file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void appendPlaceOrder(String data) {
		LocalDate myObj = LocalDate.now(); // Create a date object
//		System.out.println(myObj); // Display the current date

		String path = "\\TWS API\\samples\\Java\\autotrade\\LogFile\\";
		String filename = "placeOrder_" + myObj + ".txt";

		try {
			FileWriter myWriter = new FileWriter(path + filename, true); // true is append active
			myWriter.write(data);
			myWriter.write("\n");

			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void wirteFile(String data, String filename) {
		LocalDate myObj = LocalDate.now(); // Create a date object
//		System.out.println(myObj); // Display the current date

		try {
			FileWriter myWriter = new FileWriter(filename, false); // true is append active
			myWriter.write(data);
			myWriter.write("\n");

			myWriter.close();
			System.out.println("Successfully wrote to the file: " + filename);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readFile(String filename) {
		ArrayList<String> dataList = new ArrayList<String>();
		try {
			File myObj = new File(filename);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
//				System.out.println(data);
				dataList.add(data);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return dataList;
	}
}
