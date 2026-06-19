package autotrade;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.Order;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.controller.Formats;
import com.ib.controller.Position;
import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.ITickByTickDataHandler;

public class AutoTradeAbi implements ITickByTickDataHandler, IPositionHandler, IAccountHandler {

	static ArrayList<String> key = new ArrayList<>();

	String symbol = "";
	ArrayList<AutoTradeAbi> tickObj = new ArrayList<>();
	static Dictionary<String, ArrayList<Double>> data = new Hashtable();

	static Dictionary<String, ArrayList<Double>> position = new Hashtable();

	static double a_bid, a_ask, b_bid, b_ask, c_bid, c_ask;
	static double plTraget = 10, qty = 100000;
	static double diffMax = 30, diffMin = -30, minEntryValue=60;
	double commision = 0;
	static ArrayList<Double> bid = new ArrayList<Double>();
	static ArrayList<Double> ask = new ArrayList<Double>();

	static boolean filledOpen = false;
	static boolean filledClose = false;
//	AutoTradeAbi aTickObj, bTickObj, cTickObj;

	AutoTradeAbi(String a, String b, String c) { // strat new trade
		key.add(a);
		key.add(b);
		key.add(c);
	}
	

	AutoTradeAbi(String symbol) {
		this.symbol = symbol;
		// key.add(symbol);
		requestTickData(symbol);

	}

	public void startTrade() {

		reqPosition();
		req3FXTick();

		String accountID = API.INSTANCE.accountList().get(0);
//		System.out.println(">>>>" + accountID);// #DU213972
		API.print(">>>>" , accountID);
		API.INSTANCE.m_controller.reqAccountUpdates(true, accountID, this);
	}

	public void reqPosition() {
		API.INSTANCE.m_controller.reqPositions(this);

	}

	public void reqStopAccount() {
		String accountID = API.INSTANCE.accountList().get(0);
		System.out.println(">>>>" + accountID);// #DU213972
//		API.INSTANCE.m_controller.reqAccountUpdates(true, accountID, this);
//		API.INSTANCE.m_controller.reqAccountUpdates(false, accountID, this);
//		API.INSTANCE.m_controller.ac
	}

	public void processTick() {
		if (data.size() == 3) {
			a_bid = getBid(key.get(0));
			a_ask = getAsk(key.get(0));
			b_bid = getBid(key.get(1));
			b_ask = getAsk(key.get(1));
			c_bid = getBid(key.get(2));
			c_ask = getAsk(key.get(2));

			System.out.println(key.get(0) + "(" + a_bid + "," + a_ask + "), " + key.get(0) + " (" + b_bid + ", " + b_ask
					+ ") " + key.get(0) + " (" + c_bid + "," + c_ask + ")");
			System.out.println(position);
			if (getPos(key.get(0)) == 0 && getPos(key.get(0)) == 0 && getPos(key.get(0)) == 0) {
				System.out.println("Can opennnnnnnnnnnn");
				canOpen();
			} else if (getPos(key.get(0)) != 0 && getPos(key.get(0)) != 0 && getPos(key.get(0)) != 0) {
				System.out.println("Can closeeeeeeeeeee");

				canClose();
			} else {
				System.out.println("Position is not balance, Do no thing");
			}

		}
	}

	ArrayList<ArrayList<Double>> pos_data = new ArrayList<ArrayList<Double>>();

	public boolean checkKeyPosition(String key) {
		if (position.get(key) == null) {
			return false;
		}
		return true;
	}

	public double getPos(String key) {
		double pos = 0;
		if (checkKeyPosition(key) == true)
			pos = position.get(key).get(0);
		return pos;
	}

	public double getAvg(String key) {
		double avg = 0;
		if (checkKeyPosition(key) == true)
			avg = position.get(key).get(1);
		return avg;
	}

	public void processPostion() {

	}

	public void requestTickData(String symbol) {
		this.symbol = symbol;
		String tickType = "BidAsk";
		int numberOfTicks = 0;
		boolean ignoreSize = false;

		API.INSTANCE.m_controller.reqTickByTickData(ContractATS.getContractFX(this.symbol), tickType, numberOfTicks,
				ignoreSize, this);
	}

	public void req3FXTick() {
		for (int i = 0; i < key.size(); i++) {
			tickObj.add(new AutoTradeAbi(key.get(i)));
		}
	}

	public void reqStop() {
		for (int i = 0; i < key.size(); i++) {
			tickObj.get(i).reqStopTick();
		}
	}

	public void reqStopTick() {

		API.INSTANCE.m_controller.cancelTickByTickData(this);

	}

	public static double getBid(String keyIndex) {
		return data.get(keyIndex).get(0);

	}

	public static double getAsk(String keyIndex) {
		return data.get(keyIndex).get(1);

	}

	public void canOpen() {

		double main = (a_bid + a_ask) / 2;
		double b_mid = (b_bid + b_ask) / 2;
		double c_mid = (c_bid + c_ask) / 2;
		double sub = b_mid / c_mid;

		double diff = main - sub;
		double sum_spread = (a_bid - a_ask) + (b_bid - b_ask) + (c_bid - c_ask);

	

		double minEntry = minEntryValue / qty;
		
		double sumarize = sum_spread + minEntry + 0;

//				DIFF + and ABS(DIFF) >= SUM+MINEntry ==> SBS 
//				DIFF - and ABS(DIFF) >=  SUM+MINEntry ==> SBS 
		System.out.println("Main=" + main + " Sub=" + sub + " " + "DIFF= " + diff + ",sum=  " + sumarize + ",>? "
				+ (Math.abs(diff) >= sumarize));
		System.out.println();
		if (diff < 0 && Math.abs(diff) >= sumarize) {
			System.out.println("Open SBS");

			openOrderSBS();

		} else if (diff > 0 && Math.abs(diff) >= sumarize) {
			System.out.println("Open BSB");
			openOrderBSB();
		} else {
			System.out.println("Wait");
			// closeOrder();
		}

	}

	public void canClose() {

		closePostion();
	}

	public void openOrderBSB() {

		if (!filledOpen) {
			API.saveStart();
			Contract con1 = ContractATS.getContractFX("" + key.get(0));
			Contract con2 = ContractATS.getContractFX("" + key.get(1));
			Contract con3 = ContractATS.getContractFX("" + key.get(2));

			OrderATS orderAts = new OrderATS();
			int qty1 = (int) (qty / b_bid);
			int qty2 = (int) (qty / b_bid);
			int qty3 = (int) (qty / c_ask);
			System.out.println(qty + " " + b_bid + " " + (int) (qty / c_bid));
			System.out.println(qty1 + " " + qty2 + " " + qty3 + " ");
			Order order1 = orderAts.buyMarket(qty1);
			Order order2 = orderAts.sellMarket(qty2);
			Order order3 = orderAts.buyMarket(qty3);

			PlaceOrderATS send = new PlaceOrderATS();
			send.reqPlaceMarketOrder(con1, order1);
			send.reqPlaceMarketOrder(con2, order2);
			send.reqPlaceMarketOrder(con3, order3);
		}
		filledOpen = true;
		filledClose = false;
	}

	public void openOrderSBS() {
		if (!filledOpen) {
			API.saveStart();
			Contract con1 = ContractATS.getContractFX("" + key.get(0));
			Contract con2 = ContractATS.getContractFX("" + key.get(1));
			Contract con3 = ContractATS.getContractFX("" + key.get(2));

			OrderATS orderAts = new OrderATS();
			int qty1 = (int) (qty / b_ask);
			int qty2 = (int) (qty / b_ask);
			int qty3 = (int) (qty / c_bid);
			System.out.println(qty + " " + b_bid + " " + (int) (qty / c_ask));
			System.out.println(qty1 + " " + qty2 + " " + qty3 + " ");
			Order order1 = orderAts.sellMarket(qty1);
			Order order2 = orderAts.buyMarket(qty2);
			Order order3 = orderAts.sellMarket(qty3);

			PlaceOrderATS send = new PlaceOrderATS();
			send.reqPlaceMarketOrder(con1, order1);
			send.reqPlaceMarketOrder(con2, order2);
			send.reqPlaceMarketOrder(con3, order3);
		}
		filledOpen = true;
		filledClose = false;
	}

	public void closePostion() {
		ArrayList<Double> pos = new ArrayList<Double>();
		ArrayList<Double> avg = new ArrayList<Double>();
		for (int i = 0; i < key.size(); i++) {
			pos.add(getPos(key.get(i)));
			avg.add(getAvg(key.get(i)));
		}

		double pl = 0;

		/// calculate PL
		double pla, plb, plc;
		if (pos.get(0) > 0) {
			pla = pos.get(0) * (a_bid - avg.get(0));
			plb = pos.get(1) * (b_ask - avg.get(1));
			plc = pos.get(2) * (c_bid - avg.get(2));
			pl = (pla + plb + plc);
			System.out.println("BSB PL = " + pl + " " + pla + " " + plb + " " + plc);

		} else {

			pla = pos.get(0) * (a_ask - avg.get(0));
			plb = pos.get(1) * ( b_bid -avg.get(1) );
			plc = pos.get(2) * (c_ask - avg.get(2));
			pl = (pla + plb + plc);
			System.out.println("SBS PL = " + pl + " " + pla + " " + plb + " " + plc);


		}
		
		// Fill order
		if (!filledClose) {
			ArrayList<Contract> con = new ArrayList<Contract>();
			for (int i = 0; i < key.size(); i++) {
				con.add(ContractATS.getContractFX(key.get(i)));
			}
//			Contract con1 = ContractATS.getContractFX(key.get(0));
//			Contract con2 = ContractATS.getContractFX(key.get(1));
//			Contract con3 = ContractATS.getContractFX(key.get(2));

			OrderATS orderAts = new OrderATS();
			Order order1;
			Order order2;
			Order order3;
			ArrayList<Order> order = new ArrayList<Order>();

			if (pos.get(0) > 0) {

				if (pl > plTraget) {
					filledClose = true;
					filledOpen = false;
					System.out.println("Close postion BSB " + pos.get(0) + " " + pos.get(1) + " " + pos.get(2));
					order.add(orderAts.sellMarket((int) Math.abs(pos.get(0))));
					order.add(orderAts.buyMarket((int) Math.abs(pos.get(1))));
					order.add(orderAts.sellMarket((int) Math.abs(pos.get(2))));
					PlaceOrderATS send = new PlaceOrderATS();
					for (int i = 0; i < key.size(); i++) {
						send.reqPlaceMarketOrder(con.get(i), order.get(i));
					}

				}

			} else {

				if (pl > plTraget) {
					filledClose = true;
					filledOpen = false;
					System.out.println("Close postion SBS" + pos.get(0) + " " + pos.get(1) + " " + pos.get(2));
					order.add(orderAts.buyMarket((int) Math.abs(pos.get(0))));
					order.add(orderAts.sellMarket((int) Math.abs(pos.get(1))));
					order.add(orderAts.buyMarket((int) Math.abs(pos.get(2))));

					PlaceOrderATS send = new PlaceOrderATS();
					for (int i = 0; i < key.size(); i++) {
						send.reqPlaceMarketOrder(con.get(i), order.get(i));
					}

				}

			}

		}

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


	boolean complete = false;

	@Override
	public void positionEnd() {
		// TODO Auto-generated method stub
		processPostion();
		API.INSTANCE.m_controller.cancelPositions(this);
		complete = true;
	}

	/// account update
	@Override
	public void accountValue(String account, String key, String value, String currency) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accountTime(String timeStamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accountDownloadEnd(String account) {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void updatePortfolio(Position position) {
//		// TODO Auto-generated method stub
//		System.out.println("====port man ====== ");
//
//		ArrayList<Double> pos = new ArrayList<Double>();
//		pos.add(position.position());
//		pos.add(position.averageCost());
//		String key = position.contract().symbol() + "/" + position.contract().currency();
//		this.position.put(key, pos);
//		processPostion();
//	}


	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		ArrayList<Double> position = new ArrayList<Double>();
		position.add(Double.parseDouble(""+pos));
		position.add(avgCost);

		String key = contract.symbol() + "/" + contract.currency();
//		System.out.println("keyyyyyy " + key);

		this.position.put(key, position);

//		System.out.println("position list " + this.position);
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
		System.out.println(symbol + " " + Formats.fmtDate(time * 1000) + " " + bidPrice + ", " + askPrice);

		ArrayList<Double> price = new ArrayList<Double>();
		price.add(bidPrice);
		price.add(askPrice);
		data.put(symbol, price);

//		System.out.println(bidPrice+" "+askPrice);
		processTick();
	}


	@Override
	public void updatePortfolio(Position position) {
		// TODO Auto-generated method stub
		System.out.println("====port man ====== ");

		ArrayList<Double> pos = new ArrayList<Double>();
		//pos.add(position.position());
		pos.add(position.averageCost());
		String key = position.contract().symbol() + "/" + position.contract().currency();
		//this.position.put(key, pos);
		processPostion();
	}

}
