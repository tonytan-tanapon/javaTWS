package autotrade;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class AnalisysStraddle {
	public static void main(String[] args) {

		String strikeStart = "125.0";
		String strikeStop = "150.0";
		double i = Double.parseDouble(strikeStart);
//		AAPL20230127C125.0ASK
//		AAPL20230127C126.0ASK.csv
//		AAPL20230127P126.0ASK.csv
		for(;i<=Double.parseDouble(strikeStop);i++  ) {
			analyse(""+i);
		}
//		analyse(strike);

//		analyse2(strike);
		i = Double.parseDouble(strikeStart);
		for (; i <= Double.parseDouble(strikeStop); i++) {
			analyse2("" + i);
		}
//		analyse3(strikeStart);
		i = Double.parseDouble(strikeStart);
		for (; i <= Double.parseDouble(strikeStop); i++) {
			analyse3("" + i);
		}
	}
	public static String createListdata(String key, Dictionary dict) {
		 String data = key+",0.0,0.0,0.0,0.0"; 
		try {
			data = dict.get(key).toString();
		}
		catch(Exception ex) {	}

		return data;
	}
	public static void analyse3(String strike) {
		
//		AAPL20230127C125.0ASK
		String filenameCAsk =  "AAPL20230127" + "C" + strike + "ASK" + ".csv";
		String filenameCBis =  "AAPL20230127" + "C"  + strike + "BID" + ".csv";
		
		String filenamePAsk =  "AAPL20230127" + "P" + strike + "ASK" + ".csv";
		String filenamePBid = "AAPL20230127" + "P"  + strike + "BID" + ".csv";
		
		String filenameStock = "AAPLASK.csv";

		ArrayList<String> dataListCASk = LogFile.readFile("D:\\optionStraddle\\" + filenameCAsk);
		ArrayList<String> dataListCBid = LogFile.readFile("D:\\optionStraddle\\" + filenameCBis);
		ArrayList<String> dataListPAsk = LogFile.readFile("D:\\optionStraddle\\" + filenamePAsk);
		ArrayList<String> dataListPBid = LogFile.readFile("D:\\optionStraddle\\" + filenamePBid);
		
		ArrayList<String> dataList3 = LogFile.readFile("D:\\optionStraddle\\" + filenameStock);


		// convert data to Dict

		Dictionary askCallData = createDict(dataListCASk);
		Dictionary askPutData = createDict(dataListCBid);
		Dictionary bidCallData = createDict(dataListPAsk);
		Dictionary bidPutData = createDict(dataListPBid);
		Dictionary dataStock = createDict(dataList3);
		
		
		String out = "DateTime,open,high,low,close,DateTime,open,high,low,close,DateTime,open,high,low,close,DateTime,open,high,low,close,DateTime,open,high,low,close\n";
		for (int i = 0; i < dataListCASk.size(); i++) {

			// prints the value
		
			String key = dataListCASk.get(i).split(",")[0];

			if (key.equals(""))
				break;
			
			String askCallval="";
			String askPutval = "";
			String bidCallval = "";
			String bidPutval = "";
			String stockVal = "";
			
			askCallval = createListdata(key, askCallData);
			askPutval = createListdata(key, askPutData);
			bidCallval = createListdata(key, bidCallData);
			bidPutval = createListdata(key, bidPutData);
			stockVal = createListdata(key, dataStock);
			
			out += askCallval+","+askPutval+","+bidCallval+","+bidPutval+","+stockVal+"\n";

		}
		
		LogFile.wirteFile(out, "D:\\optionStraddle\\" + "CPOption" + "AAPL20230127" + strike + ".csv");
	}
	public static void analyse2(String strike) {
		String filename1 = "Straddle" + "AAPL20230127" + strike + "ASK" + ".csv";
		String filename2 = "Straddle" + "AAPL20230127" + strike + "BID" + ".csv";
		String filenameStock = "AAPLASK.csv";

		ArrayList<String> dataList1 = LogFile.readFile("D:\\optionStraddle\\" + filename1);
		ArrayList<String> dataList2 = LogFile.readFile("D:\\optionStraddle\\" + filename2);
		ArrayList<String> dataList3 = LogFile.readFile("D:\\optionStraddle\\" + filenameStock);

		Dictionary stock = new Hashtable();
		for (int i = 0; i < dataList3.size(); i++) {
			stock.put(dataList3.get(i).split(",")[0], dataList3.get(i));
		}
		
		Dictionary bid = new Hashtable();
		for (int i = 0; i < dataList2.size(); i++) {
			bid.put(dataList2.get(i).split(",")[0], dataList2.get(i));
		}
		String out = "DateTime,open,high,low,close,DateTime,open,high,low,close,DateTime,open,high,low,close\n";
		for (int i = 0; i < dataList1.size(); i++) {
			String d1 = dataList1.get(i);
			String d2 = (String) bid.get(dataList1.get(i).split(",")[0]);
			String d3 = (String) stock.get(dataList1.get(i).split(",")[0]);
//			System.out.println(d1 + " "+d2);
			out += d1 + "," + d2 + "," + d3 + "\n";

//			System.out.println(dataList1.get(i).split(",")[0].toString());

		}
//		System.out.println(dict.get(dataList1.get(1).split(",")[0].toString()));
//		System.out.println(out);
		LogFile.wirteFile(out, "D:\\optionStraddle\\" + "AllStraddle" + "AAPL20230127" + strike + ".csv");
	}

	public static void analyse(String strike) {

		String right = "C";

		String whatToShow = "ASK";

		String filename1 = "AAPL20230127" + "C" + strike + "ASK" + ".csv";
		String filename2 = "AAPL20230127" + "P" + strike + "ASK" + ".csv";

		String filename3 = "AAPL20230127" + "C" + strike + "BID" + ".csv";
		String filename4 = "AAPL20230127" + "P" + strike + "BID" + ".csv";

		String filename5 = "AAPL" + "ASK" + ".csv";
		String filename6 = "AAPL" + "BID" + ".csv";
		System.out.println(filename1);

		ArrayList<String> dataList1 = LogFile.readFile("D:\\optionStraddle\\" + filename1);
		ArrayList<String> dataList2 = LogFile.readFile("D:\\optionStraddle\\" + filename2);
		ArrayList<String> dataList3 = LogFile.readFile("D:\\optionStraddle\\" + filename3);
		ArrayList<String> dataList4 = LogFile.readFile("D:\\optionStraddle\\" + filename4);

//		ArrayList<String> dataList5 = LogFile.readFile("D:\\optionStraddle\\"+filename5);
//		ArrayList<String> dataList6 = LogFile.readFile("D:\\optionStraddle\\"+filename6);
//		System.out.println(dataList3.get(index));
		String data1 = "";
		String data2 = "";

		// convert data to Dict

		Dictionary askCallData = createDict(dataList1);
		Dictionary askPutData = createDict(dataList2);
		Dictionary bidCallData = createDict(dataList3);
		Dictionary bidPutData = createDict(dataList4);

//		System.out.println(askCallData);
//		for (Enumeration eum = askCallData.keys(); eum.hasMoreElements();) {
		for (int i = 0; i < dataList1.size(); i++) {

			// prints the value
		
			String key = dataList1.get(i).split(",")[0];

			if (key.equals(""))
				break;
			
			
			String askCallval = key+",0.0,0.0,0.0,0.0"; 
			try {
				askCallval = askCallData.get(key).toString();
			}
			catch(Exception ex) {	}
			
			String askPutval = key+",0.0,0.0,0.0,0.0"; 
			try {
				askPutval = askPutData.get(key).toString();
			}
			catch(Exception ex) {	}
			
			String bidCallval =key+",0.0,0.0,0.0,0.0"; 
			try {
				bidCallval = bidCallData.get(key).toString();
			}
			catch(Exception ex) {	}
			
			String bidPutval=key+",0.0,0.0,0.0,0.0";
			try {
				bidPutval = bidPutData.get(key).toString();
			}
			catch(Exception ex) {	}
			
			String date = key;
			String openASK = ""
					+ (Double.parseDouble(askCallval.split(",")[1]) + Double.parseDouble(askPutval.split(",")[1]));
			String highASK = ""
					+ (Double.parseDouble(askCallval.split(",")[2]) + Double.parseDouble(askPutval.split(",")[2]));
			String lowASK = ""
					+ (Double.parseDouble(askCallval.split(",")[3]) + Double.parseDouble(askPutval.split(",")[3]));
			String closeASK = ""
					+ (Double.parseDouble(askCallval.split(",")[4]) + Double.parseDouble(askPutval.split(",")[4]));

			String openBID = ""
					+ (Double.parseDouble(bidCallval.split(",")[1]) + Double.parseDouble(bidPutval.split(",")[1]));
			String highBID = ""
					+ (Double.parseDouble(bidCallval.split(",")[2]) + Double.parseDouble(bidPutval.split(",")[2]));
			String lowBID = ""
					+ (Double.parseDouble(bidCallval.split(",")[3]) + Double.parseDouble(bidPutval.split(",")[3]));
			String closeBID = ""
					+ (Double.parseDouble(bidCallval.split(",")[4]) + Double.parseDouble(bidPutval.split(",")[4]));

			data1 += date + "," + openASK + "," + highASK + "," + lowASK + "," + closeASK;
			data2 += date + "," + openBID + "," + highBID + "," + lowBID + "," + closeBID;
			data1 += "\n";
			data2 += "\n";

		}

		String filenameNew1 = "D:\\optionStraddle\\" + "Straddle" + "AAPL20230127" + strike + "ASK" + ".csv";
		String filenameNew2 = "D:\\optionStraddle\\" + "Straddle" + "AAPL20230127" + strike + "BID" + ".csv";
		LogFile.wirteFile(data1, filenameNew1);
		LogFile.wirteFile(data2, filenameNew2);

	}

	public static Dictionary<String, Object> createDict(ArrayList<String> data) {
		Dictionary dictData = new Hashtable();
		for (int i = 0; i < data.size(); i++) {
			dictData.put(data.get(i).split(",")[0], data.get(i));
		}
		return dictData;
	}

	
}
