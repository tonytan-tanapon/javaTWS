package autotrade;

public class Abi {
	String symbol = ""; 
	String datatime = "";
	double bid = 0 ;
	double ask = 0;
	boolean complete = false;
	
	public void addData(String symbol, String datatime, double bid, double ask) {
		this.symbol = symbol;
		this.datatime = datatime; 
		this.bid = bid;
		this.ask = ask;
		complete = true;
	}
	
	public void showData() {
		System.out.println(symbol + " "+datatime + " "+bid + " "+ask );
	}
	
	public boolean complete() {
		return complete;
	}
}
