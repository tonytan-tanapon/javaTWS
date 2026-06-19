package autotrade;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class AnalisysFX {
	public static void main(String[] args) {
//		String symbol = new String() {"GBPUSD","EURUSD","GBPUSD"};
		String [] symbols = {"EURGBP","EURUSD","GBPUSD"};
		//		EURGBP-(EURUSD/GBPUSD)
//		for(String symbol:symbols) {
//			meargeBidAsk(symbol);
//		}
		
//		processABI(symbols);
		processABIClose(symbols);

	}
	
	public static void processABIClose(String []symbol) {
		ArrayList<String> EURGBPASK = LogFile.readFile("D:\\FXdata\\" + "EURGBPASK.csv");
		ArrayList<String> EURGBPBID = LogFile.readFile("D:\\FXdata\\" + "EURGBPBID.csv");
		ArrayList<String> EURUSDASK = LogFile.readFile("D:\\FXdata\\" + "EURUSDASK.csv");
		ArrayList<String> EURUSDBID = LogFile.readFile("D:\\FXdata\\" + "EURUSDBID.csv");
		ArrayList<String> GBPUSDASK = LogFile.readFile("D:\\FXdata\\" + "GBPUSDASK.csv");
		ArrayList<String> GBPUSDBID = LogFile.readFile("D:\\FXdata\\" + "GBPUSDBID.csv");
		
		Dictionary askDataEURGBP = createDict(EURGBPASK);
		Dictionary bidDataEURGBP = createDict(EURGBPBID);
		Dictionary askDataEURUSD = createDict(EURUSDASK);
		Dictionary bidDataEURUSD = createDict(EURUSDBID);
		Dictionary askDataGBPUSD = createDict(GBPUSDASK);
		Dictionary bidDataGBPUSD = createDict(GBPUSDBID);
		
		String out="date,EURGBPASK,EURGBPbid,EURUSDASK,EURUSDbid,GBPUSDASK,GBPUSDbid,abc\n";
		for (int i = 0; i < askDataEURGBP.size(); i++) {

			String key = EURGBPASK.get(i).split(",")[0];

			if (key.equals(""))
				break;
			//EURGBP-(EURUSD/GBPUSD)
			String askEURGBP = createListdata(key, askDataEURGBP);
			String bidEURGBP = createListdata(key, bidDataEURGBP);
			String askEURUSD = createListdata(key, askDataEURUSD);
			String bidEURUSD = createListdata(key, bidDataEURUSD);
			String askGBPUSD = createListdata(key, askDataGBPUSD);
			String bidGBPUSD = createListdata(key, bidDataGBPUSD);
			
			double abc = 0 ;
			
			double closeEURGBP = getPrice(askEURGBP, 4);
			double closeURUSD = getPrice(askEURUSD, 4);
			double closeGBPUSD = getPrice(askGBPUSD, 4);
			
			double closeEURGBPBid = getPrice(bidEURGBP, 4);
			double closeURUSDBid = getPrice(bidEURUSD, 4);
			double closeGBPUSDBid = getPrice(bidGBPUSD, 4);
			
			abc = closeEURGBP - (closeURUSD/closeGBPUSD);
			
			
			out+= key+","+closeEURGBP+","+closeEURGBPBid+","+closeURUSD+","+closeURUSDBid+","+closeGBPUSD+","+closeGBPUSDBid+","+
					abc+","+"\n";
		}
		LogFile.wirteFile(out, "D:\\FXdata\\"  + "Allclose.csv");
	}
	public static void processABI(String []symbol) {
		ArrayList<String> EURGBPASK = LogFile.readFile("D:\\FXdata\\" + "EURGBPASK.csv");
		ArrayList<String> EURGBPBID = LogFile.readFile("D:\\FXdata\\" + "EURGBPBID.csv");
		ArrayList<String> EURUSDASK = LogFile.readFile("D:\\FXdata\\" + "EURUSDASK.csv");
		ArrayList<String> EURUSDBID = LogFile.readFile("D:\\FXdata\\" + "EURUSDBID.csv");
		ArrayList<String> GBPUSDASK = LogFile.readFile("D:\\FXdata\\" + "GBPUSDASK.csv");
		ArrayList<String> GBPUSDBID = LogFile.readFile("D:\\FXdata\\" + "GBPUSDBID.csv");
		
		Dictionary askDataEURGBP = createDict(EURGBPASK);
		Dictionary bidDataEURGBP = createDict(EURGBPBID);
		Dictionary askDataEURUSD = createDict(EURUSDASK);
		Dictionary bidDataEURUSD = createDict(EURUSDBID);
		Dictionary askDataGBPUSD = createDict(GBPUSDASK);
		Dictionary bidDataGBPUSD = createDict(GBPUSDBID);
		
		String out="ABC,date,open,high,low,close,date,open,high,low,close,date,open,high,low,close,date,open,high,low,close,date,open,high,low,close,date,open,high,low,close\n";
		for (int i = 0; i < askDataEURGBP.size(); i++) {

			String key = EURGBPASK.get(i).split(",")[0];

			if (key.equals(""))
				break;
			//EURGBP-(EURUSD/GBPUSD)
			String askEURGBP = createListdata(key, askDataEURGBP);
			String bidEURGBP = createListdata(key, bidDataEURGBP);
			String askEURUSD = createListdata(key, askDataEURUSD);
			String bidEURUSD = createListdata(key, bidDataEURUSD);
			String askGBPUSD = createListdata(key, askDataGBPUSD);
			String bidGBPUSD = createListdata(key, bidDataGBPUSD);
			
			double abc = 0 ;
			
			double openEURGBP = getPrice(askEURGBP, 1);
			double openEURUSD = getPrice(askEURUSD, 1);
			double openGBPUSD = getPrice(askGBPUSD, 1);
			abc = openEURGBP - (openEURUSD/openGBPUSD);
			
			out+= abc+","+askEURGBP+","+bidEURGBP+","+askEURUSD+","+bidEURUSD+","+askGBPUSD+","+bidGBPUSD+"\n";
		}
		LogFile.wirteFile(out, "D:\\FXdata\\"  + "All.csv");
	}
	
	public static double getPrice(String data, int index) {
		
		String out = data.split(",")[index];
		return Double.parseDouble(out);
	}
	
	public static void meargeBidAsk(String symbol) {
		ArrayList<String> symbol1ASK = LogFile.readFile("D:\\FXdata\\" + symbol+"ASK.csv");
		ArrayList<String> symbol1BID = LogFile.readFile("D:\\FXdata\\" + symbol+"BID.csv");
		Dictionary askData = createDict(symbol1ASK);
		Dictionary bidData = createDict(symbol1BID);
		String out="date,open,high,low,close,date,open,high,low,close\n";
		for (int i = 0; i < symbol1ASK.size(); i++) {

			String key = symbol1ASK.get(i).split(",")[0];

			if (key.equals(""))
				break;
			
			String ask = createListdata(key, askData);
			String bid = createListdata(key, bidData);
			
			out+= ask+","+bid+"\n";
		}
		LogFile.wirteFile(out, "D:\\FXdata\\" + symbol + ".csv");
	}
	public static String createListdata(String key, Dictionary dict) {
		String data = key+",0.0,0.0,0.0,0.0"; 
		try {
			data = dict.get(key).toString();
		}
		catch(Exception ex) { }

		return data;
	}

	public static Dictionary<String, Object> createDict(ArrayList<String> data) {
		Dictionary dictData = new Hashtable();
		for (int i = 0; i < data.size(); i++) {
			dictData.put(data.get(i).split(",")[0], data.get(i));
		}
		return dictData;
	}

	
}
