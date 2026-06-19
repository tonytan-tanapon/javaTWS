package apitest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Types.Right;
import com.ib.controller.AccountSummaryTag;

public class A_MediatorImp implements A_Mediator {

	private A_Position position;
	private A_Tick tick;
	private A_Gui gui;
	private A_Account account;
	private A_Historical historical;
	private A_OrderSend orderSend;
	private A_OrderStatus OrderStatus;
	private A_Control control;
	private A_OptionChain optionChain;
	private A_Indicator indicator;
	private A_LiveOrder liveOrder;
	
	A_Detail detail = new A_Detail();

	public A_MediatorImp() {

	}
	public void init(A_Detail detail) {
		this.detail = detail;
		System.out.println("Init...");
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);		
		this.historical.receive(A_MSG.Req);
	

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("symbol", detail.symbol);
		map.put("date", detail.fridayOpion);
		this.optionChain.receive(map.toString());
		this.liveOrder.receive("");

	}
	
	// Start function 
	public void start(Contract contract, BarDetailPanel barDetail) {
		detail.StockContract = contract;
		detail.barDetail = barDetail;
		
		System.out.println("Start Trade !!!!");
		
		// function receive is that request data from server.
		this.tick.receive(A_MSG.Tick, contract);
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);
	
		this.historical.receive(A_MSG.Req);
////		this.liveOrder.receive("");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("symbol", detail.symbol);
		map.put("date", detail.getOptionExpireDate());
		this.optionChain.receive(map.toString());
		this.liveOrder.receive("");

	}
	
	// init function
	private void init(Contract contract, BarDetailPanel barDetail) {
		// TODO Auto-generated method stub
		detail.StockContract = contract;
		detail.barDetail = barDetail;
		
		System.out.println("Test Trade !!!!");
		
		// function receive is that request data from server.
		//this.tick.receive(A_MSG.Tick, contract);
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);
		this.historical.receive(A_MSG.Req);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("symbol", detail.symbol);
		map.put("date", detail.getOptionExpireDate());
		this.optionChain.receive(map.toString());
		this.liveOrder.receive("");

		
		processSignal();
	}
	@Override
	public void start(A_Detail detail) {
		this.detail = detail;

		System.out.println("Start...");
		this.position.receive(A_MSG.Req);
		this.account.receive(A_MSG.Req);

		
		HashMap<String, String> msg_account = new HashMap<String, String>();
		msg_account.put("cmd", A_MSG.Tick);
		msg_account.put("symbol", detail.symbol);
		this.tick.receive(msg_account.toString());
//		this.tick.receive(detail);
		
		this.historical.receive(A_MSG.Req);
		this.liveOrder.receive("");


		HashMap<String, String> map = new HashMap<String, String>();
		map.put("symbol", detail.symbol);
		map.put("date", detail.fridayOpion);
		this.optionChain.receive(map.toString());


	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		tick.receive(A_MSG.Stop);
	}
	
	public void processSignal() {
		System.out.println("===================Detail=======================");
		System.out.println("detail.positionState:" +detail.positionState);
		System.out.println("detail.accountState:" +detail.accountState );
		System.out.println("detail.liveorderState:" +detail.liveOrderState );
		System.out.println("detail.Tick:" +detail.bidTick);
		System.out.println("detail.optionState:" +detail.optionState);
		System.out.println("orderSend.state:" +orderSend.state);
		System.out.println("detail.pos: "+detail.pos);
		System.out.println("detail.signal:" +detail.signal);
		System.out.println("detail.strikeList:" +detail.strikeList);
		System.out.println("detail.bidTick:" +detail.bidTick);
		System.out.println("====================END======================");
		if (detail.positionState && detail.accountState 
				&& detail.optionState 
				&& detail.liveOrderState) {
//			System.out.println("ALL true");
			if (detail.pos == 0.0) {
				// buy to open
				if (detail.signal == 1.0) {
					System.out.println(" buy call");
					// buy call
					detail.positionState = false;
					detail.callATM = A_MSG.getATM(detail.bidTick, detail.strikeList, Right.Call);
					orderSend.receive("buycall", detail);

				} else if (detail.signal == -1.0) {
					// buy put
					System.out.println(" buy put");
					detail.positionState = false;
					detail.putATM = A_MSG.getATM(detail.bidTick, detail.strikeList, Right.Put);
					orderSend.receive("buyput", detail);
				}
			} else if (detail.pos > 0.0) {
				if (detail.signal == 1.0 && detail.ContractPosition.right().equals(Right.Put)) {
					System.out.println(" sellput");
					detail.positionState = false;
					orderSend.receive("sellput", detail);

				} else if (detail.signal == -1.0 && detail.ContractPosition.right().equals(Right.Call)) {
					System.out.println(" sellcall");
					detail.positionState = false;
					orderSend.receive("sellcall", detail);

				}
			}

		}
		

	}
	
	@Override
	public void sendMessage(String msg, A_Tick tick) {
		// TODO Auto-generated method stub
		// === IN ===
		
		
		detail.d_tick = (HashMap<String, String>) tick.getData();
		
		detail.dateTick = detail.d_tick.get("time");
		detail.bidTick = Double.parseDouble(detail.d_tick.get("bid"));
		detail.askTick = Double.parseDouble(detail.d_tick.get("ask"));

		processSignal();
//		System.out.println(detail.bidTick);
		
		/// === OUT ===
		
		gui.receive("Tick", tick);

	}

	@Override
	public void sendMessage(String msg, A_Control control) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_Historical hist) {
		// TODO Auto-generated method stub
		// === IN ===
//		ArrayList<Bar> d_hist = (ArrayList<Bar>) hist.getData();
		System.out.println("Close from Hist: "+hist.getTableData().getValueAt(hist.getTableData().getRowCount()-1, 4));
		detail.bidTick = (double) hist.getTableData().getValueAt(hist.getTableData().getRowCount()-1, 4);
		// === OUT ===
		System.out.println("Historical data Done !!!");
		
		gui.receive("hist", hist);
	}

	@Override
	public void sendMessage(String msg, A_OptionChain option) {
		// TODO Auto-generated method stub
		// === IN === 
		detail.optionState = true;
		detail.strikeList = (ArrayList<Double>) option.strikeList();
		detail.callData = (HashMap<Double, Contract>) option.getCallData();
		detail.putData = (HashMap<Double, Contract>) option.getPutData();
		
		/// === OUT ===
		
//		gui.receive("option"+detail.callATM, option);
	}

	@Override
	public void sendMessage(String msg, A_Position position) {
		
		// TODO Auto-generated method stub
		// === IN ===  set position
		detail.positionState = position.state;

		detail.d_position = (HashMap<Contract, List<Object>>) position.getData();
		detail.d_position.forEach((key, value) -> processPosition(key, value));

		// === OUT ===
		gui.receive("position", position);
		

	}

	public void processPosition(Contract key, List<Object> value) {
		if (Double.parseDouble(value.get(0).toString()) != 0) {
			if (key.symbol().equals(detail.symbol)) {
				detail.pos = Double.parseDouble(value.get(0).toString());
				detail.ContractPosition = key;
				detail.ContractPosition.exchange("SMART");
			}
		} 
	}

	@Override
	public void sendMessage(String msg, A_Account acc) {
		// TODO Auto-generated method stub
		// === IN === 
		HashMap<AccountSummaryTag, String> data = (HashMap<AccountSummaryTag, String>) acc.getData();
		detail.d_buyingPower = Double.parseDouble(data.get(AccountSummaryTag.BuyingPower));
		detail.d_netLiquidation = Double.parseDouble(data.get(AccountSummaryTag.NetLiquidation));
		detail.accountState = true;
//		System.out.println(detail.d_buyingPower + " " + detail.d_netLiquidation);

		// === OUT ===
		gui.receive(msg, acc);
	
	}
	@Override
	public void sendMessage(String msg, A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		// === IN === 
		
		if(msg.equals(A_MSG.Filled)) {
			System.out.println("complete");
			detail.liveOrderState = true;
			
		}
		else {
			System.out.println("still remaining");
			detail.liveOrderState = false;
		}
//		if(position.getTabelData().getRowCount() != 0) {
//			detail.pos = Double.parseDouble(""+position.getTabelData().getValueAt(0, 1));	
//		}else {
//			detail.pos = 0;
//		}
		
		// === OUT ===
//		this.position.receive(A_MSG.Req);
//		this.account.receive(A_MSG.Req);
	}
	@Override
	public void sendMessage(String msg, A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_Indicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMessage(String msg, A_OrderSend openSend) {
		// TODO Auto-generated method stub
		detail.positionState = true;
		this.position.receive(A_MSG.Req);
	}

	@Override
	public void sendMessage(String msg, A_Gui gui) {
		// TODO Auto-generated method stub
		System.out.println("Incomming message: "+msg);
		HashMap<String, String> map = A_MSG.toHashMap(msg);
		System.out.println("Convert message: "+map);
		
		
		
		
		// When click stop button in GUI
		if(msg.equals(A_MSG.Stop)) {
			
			System.out.println("Stop");
			stop();
		}
		else if(msg.equals(A_MSG.Buy)) {
			detail.signal = Double.parseDouble(A_MSG.Buy.toString());
		}
		else if(msg.equals(A_MSG.Sell)) {
			detail.signal = Double.parseDouble(A_MSG.Sell.toString());
		}
		else if(msg.equals(A_MSG.Wait)) {
			detail.signal = Double.parseDouble(A_MSG.Wait.toString());
		}// When click Init in GUI, Start hear.
		else if(map.get("cmd").equals(A_MSG.Init)) {
			
		}
		
		
//		HashMap<String, String> map = A_MSG.toHashMap(msg);
//		String symbol = map.get("symbol");
//		String dateFriday = map.get("dateFirday");
//		String cmd = map.get("cmd");
//		System.out.println(msg);
//		
//		this.detail.symbol = symbol;
//		this.detail.fridayOpion = dateFriday;
//		if(cmd.equals(A_MSG.Start)) {
//			start(detail);
//		}
//		else if(cmd.equals(A_MSG.Stop)) {
//			stop();
//		}
//		else if(cmd.equals(A_MSG.Init)) {
//			init(detail);
//		}
//		else if(cmd.equals(A_MSG.Historical)) {
//			Contract contract = new Contract();
//			BarDetailPanel barDetail = new BarDetailPanel();
//			this.historical.receive(A_MSG.Req, contract , barDetail);
//		}
		

	}

	@Override
	public void add(A_Control control) {
		// TODO Auto-generated method stub
		this.control = control;
	}

	@Override
	public void add(A_Tick tick) {
		// TODO Auto-generated method stub
		this.tick = tick;
	}

	@Override
	public void add(A_Historical hist) {
		// TODO Auto-generated method stub
		this.historical = hist;
	}

	@Override
	public void add(A_OptionChain optionChain) {
		// TODO Auto-generated method stub
		this.optionChain = optionChain;
	}

	@Override
	public void add(A_Position position) {
		// TODO Auto-generated method stub
		this.position = position;
	}

	@Override
	public void add(A_Account acc) {
		// TODO Auto-generated method stub
		this.account = acc;
	}

	@Override
	public void add(A_OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		this.OrderStatus = orderStatus;
	}

	@Override
	public void add(A_Indicator indicator) {
		// TODO Auto-generated method stub
		this.indicator = indicator;
	}


	@Override
	public void add(A_OrderSend openSend) {
		// TODO Auto-generated method stub
		this.orderSend = openSend;

//		HashMap <String, String> data = new HashMap<String, String>();
//		data.put("symbol", stockSymbol);
//		data.put("date", dateFriday);
//		openSend.receive(data.toString());
	}

	@Override
	public void add(A_Gui gui) {
		// TODO Auto-generated method stub
		this.gui = gui;
	}



	@Override
	public void add(A_LiveOrder liveOrder) {
		// TODO Auto-generated method stub
		this.liveOrder = liveOrder;
	}
	
	@Override
	public void sendMessage(String msg, Contract contract, BarDetailPanel barDetail) {
		// TODO Auto-generated method stub
		// 1.this is Server side that wait request message from Client
		// 2.filter the message
		
		// When click start button
		if(msg.equals(A_MSG.Start)) {
			System.out.println("Start Trade ...");
			detail.StockContract = contract;
			detail.barDetail = barDetail;
			start(contract, barDetail );
		}
		else if(msg.equals(A_MSG.Init)){
			System.out.println("Init Trade ...");
			
			detail.StockContract = contract;
			detail.barDetail = barDetail;
			init(contract, barDetail );
		}
//		else {
//			System.out.println("COME");
//			detail.StockContract = contract;
//			detail.barDetail = barDetail;
//			historical.receive(msg, contract, barDetail);
//		}
		
		
	}
	

}
