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

public class AutoAbi2 implements IPositionHandler, ITickByTickDataHandler , IRealTimeBarHandler{
	static AutoAbiPanel autoAbiPanel = null;

	String Symbol = "";
	String symbolname = "";;
	String currency = "";

	ArrayList<Abi> data = new ArrayList<Abi>();

	static Abi data1 = new Abi();
	static Abi data2 = new Abi();
	static Abi data3 = new Abi();

	static double fxAvg1 = 0;
	static double fxAvg2 = 0;
	static double fxAvg3 = 0;

	static double fxpos1 = 0;
	static double fxpos2 = 0;
	static double fxpos3 = 0;

	static double fxpl1 = 0;
	static double fxpl2 = 0;
	static double fxpl3 = 0;

	static double sumTotal = 0;
	static boolean cansend = false;

	public AutoAbi2(String Symbol, AutoAbiPanel autoAbiPanel) {
		this.autoAbiPanel = autoAbiPanel;
		this.Symbol = Symbol;

		this.symbolname = Symbol.split("/")[0];
		this.currency = Symbol.split("/")[1];

	}

	public void reqTickData() {

		API.INSTANCE.m_controller.reqTickByTickData(ContractATS.getContractFX(Symbol), "BidAsk", 0, false, this);

	}

	public void reqPosistion() {
		API.INSTANCE.m_controller.reqPositions(this);
	}

	public void reqStopTick() {
		API.INSTANCE.m_controller.cancelTickByTickData(this);
	}

	static double sigmax = -999;

	public void showData() {

		System.out.println("show");
		data1.showData();
		data2.showData();
		data3.showData();

		double sig = (data1.ask - (data2.bid / data3.ask));

		if (sig > sigmax) {
			sigmax = sig;
		}

		System.out.println("ask - (bid/ask) = " + sig * 100 + " max = " + sigmax * 100);
		System.out.println("bid - (aks/bid) = " + (data1.bid - (data2.ask / data3.bid)) * 100);

		if (data1.complete() && data2.complete() && data3.complete()) {

			System.out.println(fxpos1+" "+fxpos2+" "+fxpos2+ " "+cansend);
			if (fxpos1 == 0 && fxpos2 == 0 && fxpos3 == 0 && cansend) {
				cansend = false;
				fxpos1 = 20000 ;
				fxpos2 = -20000 ; 
				fxpos3 = 20000;
				System.out.println("send order");
				API.start = System.currentTimeMillis();
				System.out.println("API.start  = " + API.start);

				Contract con1 = ContractATS.getContractFX(data1.symbol);
				Contract con2 = ContractATS.getContractFX(data2.symbol);
				Contract con3 = ContractATS.getContractFX(data3.symbol);

				OrderATS orderAts = new OrderATS();
				Order order1 = orderAts.buyMarket(20000);
				Order order2 = orderAts.sellMarket(20000);
				Order order3 = orderAts.buyMarket(20000);

				PlaceOrderATS send = new PlaceOrderATS();

				//send.reqPlaceMarketOrder(con1, order1);
				//send.reqPlaceMarketOrder(con2, order2);
				//send.reqPlaceMarketOrder(con3, order3);

			}
			
			/// close position
			if (fxpos1 != 0 && fxpos2 != 0 && fxpos3 != 0 && closePos ) {
				closePos = false;
				
				cansend = true;
				Contract con1 = ContractATS.getContractFX(data1.symbol);
				Contract con2 = ContractATS.getContractFX(data2.symbol);
				Contract con3 = ContractATS.getContractFX(data3.symbol);

				OrderATS orderAts = new OrderATS();
				Order order1 = orderAts.sellMarket(20000);
				Order order2 = orderAts.buyMarket(20000);
				Order order3 = orderAts.sellMarket(20000);

				PlaceOrderATS send = new PlaceOrderATS();

				//send.reqPlaceMarketOrder(con1, order1);
				//send.reqPlaceMarketOrder(con2, order2);
				//send.reqPlaceMarketOrder(con3, order3);
			}

		}

		fxpl1 = (data1.bid - fxAvg1) * fxpos1;
		fxpl2 = (data2.ask - fxAvg2) * fxpos2;
		fxpl3 = (data3.bid - fxAvg3) * fxpos3;

		sumTotal = fxpl1 + fxpl2 + fxpl3;

		autoAbiPanel.tb_data.clearRows();
		autoAbiPanel.tb_data.addRow(new String[] { data1.symbol, data1.datatime, "" + data1.bid, "" + data1.ask,
				"" + fxAvg1, "" + fxpos1, "" + fxpl1 });
		autoAbiPanel.tb_data.addRow(new String[] { data2.symbol, data2.datatime, "" + data2.bid, "" + data1.ask,
				"" + fxAvg2, "" + fxpos2, "" + fxpl2 });
		autoAbiPanel.tb_data.addRow(new String[] { data3.symbol, data3.datatime, "" + data3.bid, "" + data1.ask,
				"" + fxAvg3, "" + fxpos3, "" + fxpl3 });
		autoAbiPanel.tb_data.addRow(new String[] { "Total", "", "", "", "", "", "" + "" + sumTotal });
	}
	static double a = 20000;
	public static void buyPostion() {
		
		API.saveStart();
		Contract con1 = ContractATS.getContractFX("EUR/GBP");
		Contract con2 = ContractATS.getContractFX("EUR/USD");
		Contract con3 = ContractATS.getContractFX("GBP/USD");

		OrderATS orderAts = new OrderATS();
		Order order1 = orderAts.buyMarket(100000);
		Order order2 = orderAts.sellMarket(100000);
		Order order3 = orderAts.buyMarket(100000);

		PlaceOrderATS send = new PlaceOrderATS();
		send.reqPlaceMarketOrder(con1, order1);
		send.reqPlaceMarketOrder(con2, order2);
		send.reqPlaceMarketOrder(con3, order3);
	}
	
	public static void closePostion() {
		Contract con1 = ContractATS.getContractFX("GBP/AUD");
		Contract con2 = ContractATS.getContractFX("GBP/USD");
		Contract con3 = ContractATS.getContractFX("AUD/USD");

		OrderATS orderAts = new OrderATS();
		
		double a = Double.parseDouble(autoAbiPanel.fxPos1.getValue());
		double b = Double.parseDouble(autoAbiPanel.fxPos2.getValue());
		double c =Double.parseDouble(autoAbiPanel.fxPos3.getValue());
		
		Order order1 = orderAts.sellMarket(a);
		Order order2 = orderAts.buyMarket(b);
		Order order3 = orderAts.sellMarket(c);

		PlaceOrderATS send = new PlaceOrderATS();
		send.reqPlaceMarketOrder(con1, order1);
		send.reqPlaceMarketOrder(con2, order2);
		send.reqPlaceMarketOrder(con3, order3);
	}
	

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		if(fxpos1 == 0 && fxpos2 == 0 && fxpos3 == 0 && cansend == false) {
			cansend = true;
		}
		else 
			cansend = false;
		
		showData();
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
	boolean closePos = false;
	public  void closeAllPostion() {
		closePos = true;
		// TODO Auto-generated method stub

		
	}

	public void reqRealtime() {
		API.INSTANCE.m_controller.reqRealTimeBars(ContractATS.getContractFX(Symbol), WhatToShow.MIDPOINT, false, this);
		
	}
	@Override
	public void realtimeBar(Bar bar) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (Symbol.equals("GBP/AUD")) {
			data1.addData(Symbol, "" + bar.formattedTime(), bar.close(), bar.close());

		} else if (Symbol.equals("GBP/USD")) {
			data2.addData(Symbol, "" + bar.formattedTime(), bar.close(), bar.close());
		} else if (Symbol.equals("AUD/USD")) {
			data3.addData(Symbol, "" + bar.formattedTime(), bar.close(), bar.close());
		}
		reqPosistion() ;
		
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
		// TODO Auto-generated method stub
				if (Symbol.equals("GBP/AUD")) {
					data1.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);

				} else if (Symbol.equals("GBP/USD")) {
					data2.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);
				} else if (Symbol.equals("AUD/USD")) {
					data3.addData(Symbol, "" + Formats.fmtDate(time * 1000), bidPrice, askPrice);
				}
				reqPosistion() ;
	}

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		if (Double.parseDouble(""+pos) != 0) {
			if (contract.symbol().equals("GBP") && contract.currency().equals("AUD")) {
				fxAvg1 = avgCost;
				fxpos1 = Double.parseDouble(""+pos);
			}
			if (contract.symbol().equals("GBP") && contract.currency().equals("USD")) {

				fxAvg2 = avgCost;
				fxpos2 = Double.parseDouble(""+pos);
			}
			if (contract.symbol().equals("AUD") && contract.currency().equals("USD")) {

				fxAvg3 = avgCost;
				fxpos3 = Double.parseDouble(""+pos);
			}

		}
	}
	
}
