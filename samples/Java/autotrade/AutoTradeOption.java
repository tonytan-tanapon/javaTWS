package autotrade;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.Bar;

public class AutoTradeOption implements IRealTimeBarHandler {
	AutoTradeOptionPanel autoPanel;

	Bar bar;
	String symbol = "AAPL";
	double strike = 144;
	String date = "20230127";
	double  qty =1;
	int position  = 0 ;
	AutoTradeOption(AutoTradeOptionPanel autoPanel) {
		this.autoPanel = autoPanel;
	}

	public void requestRealTimeBar() {
		Contract contract = ContractATS.getContractStock(symbol);
		WhatToShow whatToShow = WhatToShow.TRADES;
		boolean rthOnly = false;
		IRealTimeBarHandler handler = this;

//		API.INSTANCE.m_controller.reqRealTimeBars(contract, whatToShow, rthOnly, handler);
		
//		placeStaddle(symbol, 140, "20230127", 1);
//		closeStraddle(symbol, strike,date,qty);
		
		HistoricalData data = new HistoricalData();
		data.reqHistoricalData();
	}
	
	int signal = 0; // 0 wait, 1 open, -1 close
	public void processData() {
		System.out.println(bar);

		double close = bar.close();
		double open = bar.open();
		double low = bar.low();
		double high = bar.high();
		
		double closeRound = Math.round(close);		
		double range = 2; 
		
		double highCheck = closeRound +(closeRound%range); // calculate High Low price by rounding close price and add or sub close price
		double lowChecck = closeRound -(closeRound%range);
		
		signal = calulateSignal(bar,highCheck,lowChecck);
		
		switch(signal) {
		case 0: break; // wait
		case 1:
			System.out.println("========OPEN STRADDLE========");
			placeStraddle(symbol, strike,date,qty);
			break; // open
		case -1: 
			System.out.println("========CLOSE STRADDLE========");
			closeStraddle(symbol, strike,date,qty);
			break; // close
		}
		

	}
	public int calulateSignal(Bar bar,double highCheck, double lowChecck) {
		double close = bar.close();
		double open = bar.open();
		double low = bar.low();
		double high = bar.high();
		if(position ==0 ){ //  can buy 
			return 1;
			
		}
		else { // Can Open straddle 
			
			if ((high >= highCheck && low <= highCheck) || (high >= lowChecck && low <= lowChecck))
				return 1;
		}
//		else if(signal == -1) { // Close straddle
//			return -1;
//		}
		return 0;
	}

	public void placeStraddle(String symbol, double strike, String date, double qty) {

		Contract con1 = ContractATS.getContractOption(symbol, "C", strike, date);
		Contract con2 = ContractATS.getContractOption(symbol, "P", strike, date);

		OrderATS orderAts1 = new OrderATS();
		OrderATS orderAts2 = new OrderATS();

		Order order1 = orderAts1.buyMarket(qty);
		Order order2 = orderAts2.buyMarket(qty);

		PlaceOrderATS send = new PlaceOrderATS();
		send.placeOrder(con1, order1);
		send.placeOrder(con2, order2);

	}
	
	public void closeStraddle(String symbol, double strike, String date, double qty) {
		Contract con1 = ContractATS.getContractOption(symbol, "C", strike, date);
		Contract con2 = ContractATS.getContractOption(symbol, "P", strike, date);

		OrderATS orderAts1 = new OrderATS();
		OrderATS orderAts2 = new OrderATS();

		Order order1 = orderAts1.sellMarket(qty);
		Order order2 = orderAts2.sellMarket(qty);

		PlaceOrderATS send = new PlaceOrderATS();
		send.placeOrder(con1, order1);
		send.placeOrder(con2, order2);
	}
	

	@Override
	public void realtimeBar(Bar bar) {
		// TODO Auto-generated method stub
		this.bar = bar;

		processData();
	}

	
}
