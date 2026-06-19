package autotrade;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.controller.ApiController.ITickByTickDataHandler;

public class ATickTread   implements ITickByTickDataHandler{
	Contract contract; 
//	boolean complete = false; 
	String symbol;
	double bid;
	double ask; 
	static ArrayList<String> data = new ArrayList<String>();
	static ArrayList<Double> askdata = new ArrayList<>();
	ATickTread(String symbol){
		
		this.symbol = symbol.split("/")[0];
		contract = ContractATS.getContractFX(symbol);
		data.add(symbol);

	}
	public void run() {
		getTickData();
		
//		while(!complete) {
//			
//		}
//		complete = false;
	}
	public void process() {
			System.out.println(data.size());
			System.out.println(askdata.size());
	}
	public void getTickData() {
		System.out.println(">>");
		String tickType = "BidAsk"; 
		int numberOfTicks = 0;
		boolean ignoreSize = false; 
		ITickByTickDataHandler handler = this;
		API.INSTANCE.m_controller.reqTickByTickData(contract, tickType, numberOfTicks, ignoreSize, handler);
	}
	

	@Override
	public void tickByTickMidPoint(int reqId, long time, double midPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickByTickHistoricalTickAllLast(int reqId, List<HistoricalTickLast> ticks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickByTickHistoricalTickBidAsk(int reqId, List<HistoricalTickBidAsk> ticks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickByTickHistoricalTick(int reqId, List<HistoricalTick> ticks) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size,
			TickAttribLast tickAttribLast, String exchange, String specialConditions) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize,
			Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
		// TODO Auto-generated method stub
//		System.out.println(""+time);
		this.bid = bidPrice;
		this.ask = askPrice;
		
		askdata.add(askPrice);
		process();
//		complete=true;
	}

}
