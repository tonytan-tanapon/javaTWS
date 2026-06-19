package autotrade;

import java.util.ArrayList;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.Order;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.ApiController.ITickByTickDataHandler;
import com.ib.controller.Bar;
import com.ib.controller.Formats;

public class AutoAbi implements ITickByTickDataHandler {
	static AutoAbiPanel autoAbiPanel = null;

	Contract contract; 
	String Sysmbol;
	
	
	public AutoAbi(String Symbol, AutoAbiPanel autoAbiPanel) {
		this.Sysmbol = Symbol.split("/")[0];
		contract = ContractATS.getContractFX(Symbol);

	}

	public void reqTickData() {
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
		System.out.println(Sysmbol+" "+Formats.fmtDate(time * 1000)+" "+bidPrice+", "+askPrice);
//		if (Symbol.equals("GBP/AUD")) {
//			data1.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);
//
//		} else if (Symbol.equals("GBP/USD")) {
//			data2.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);
//		} else if (Symbol.equals("AUD/USD")) {
//			data3.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);
//		}
	}

}
