/* Copyright (C) 2019 Interactive Brokers LLC. All rights reserved. This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */

package apitest;

import static com.ib.controller.Formats.fmtNz;
import static com.ib.controller.Formats.fmtPct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.print.attribute.standard.Media;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.Decimal;
import com.ib.client.HistoricalTick;
import com.ib.client.HistoricalTickBidAsk;
import com.ib.client.HistoricalTickLast;
import com.ib.client.MarketDataType;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.client.TickAttrib;
import com.ib.client.TickAttribBidAsk;
import com.ib.client.TickAttribLast;
import com.ib.client.TickType;
import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.ApiController.IAccountSummaryHandler;
import com.ib.controller.ApiController.IContractDetailsHandler;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.ApiController.ILiveOrderHandler;
import com.ib.controller.ApiController.IOptHandler;
import com.ib.controller.ApiController.IPositionHandler;
import com.ib.controller.ApiController.IRealTimeBarHandler;
import com.ib.controller.ApiController.ITickByTickDataHandler;
import com.ib.controller.ApiController.TopMktDataAdapter;



import com.ib.controller.AccountSummaryTag;
import com.ib.controller.Bar;
import com.ib.controller.Formats;
import com.ib.controller.Position;

import apidemo.util.HtmlButton;
import apidemo.util.NewTabbedPanel;
import apidemo.util.NewTabbedPanel.INewTab;
import apidemo.util.NewTabbedPanel.NewTabPanel;
import apidemo.util.VerticalPanel.HorzPanel;
import apidemo.util.VerticalPanel.StackPanel;
import apidemo.util.TCombo;
import apidemo.util.UpperField;
import apidemo.util.Util;
import apidemo.util.VerticalPanel;
import autotrade.ContractATS;

class A_AutoTradePanel extends StackPanel implements INewTab  {
	String buytype[]={"Simple", "Dynamic", "Bracket"}; 
	JComboBox buysellType=new JComboBox(buytype);
	

	public Contract m_contract = new ContractATS().getContractOption("AAPL", "C", 170.0, A_Detail.getOptionExpireDate());
	public ContractPanel m_contractPanel = new ContractPanel(m_contract);
	public BarDetailPanel m_barDetailPanel = new BarDetailPanel();
	//////
	JButton btnBuy = new JButton("Buy");
	JButton btnSell = new JButton("Sell");
	JButton btnWait = new JButton("Wait");

	InputText inputSignal = new InputText("Signal","0.0");
	InputText inputPos = new  InputText("PosN","0.0");
	InputText inputLivePos = new InputText("Live pos","0.0");
//	InputText inputQty = new InputText("Qty","20000.0");
	InputText inputQty = new InputText("Qty","10.0");
	
	InputText inputBuyingPower = new InputText("Buy Power","0.0");
	InputText inputNetLiquidation = new InputText("NetLiquidation","0.0");
	

	
	DayOfWeek dayOfWeek = DayOfWeek.FRIDAY;
	boolean autoTrade = false;
	
	double buyingPower = 0.0; 
	double netLiquidation = 0.0;
	String symbol = "AAPL";
	
	InputText inputSymbol = new InputText("Symbol", symbol); 
	InputText inputLivePrice = new InputText("Close", "0"); 
	InputText inputTime = new InputText("Time", "0", 15); 
	InputText inputBid = new InputText("Bid", "0"); 
	InputText inputAsk = new InputText("Ask", "0"); 
	InputText inputOptionCall = new InputText("Call ATM","0");
	InputText inputOptionPut = new InputText("Put ATM","0");
	InputText inputContract = new InputText("Current contract", "Contract");
	InputText inputlastTradeDate = new InputText("Last Trade Date", "DDMMYYY", 10);
	
	InputText inputLiveContract = new InputText("Live Contract", "Live contract",20);
	InputText inputLiveOrder = new InputText("Live Order", "Live order");
	A_MediatorControl myMediatorControl = new A_MediatorControl();
	JTable tableHistorical = new JTable((TableModel) myMediatorControl.m_hist.getTableData());
	JTable tablePosition = new JTable(myMediatorControl.m_position.getTabelData())  ;
	
	JScrollPane scrolHistorical;
	JComboBox listDay;
	A_Button btn = new A_Button();
	
	////
//	A_Gui gui = new A_Gui(myMediatorControl.getMediator(), symbol) {
//		
//		@Override
//		public void send(String msg) {
//			// TODO Auto-generated method stub
//			this.mediator.sendMessage(msg, this);
//		}
//	
//		@Override
//		public void receive(String msg, apitest.A_Position position) {
//			// TODO Auto-generated method stub
////			HashMap<Contract,List<Object> > info = (HashMap<Contract, List<Object>>) position.getData();
////			System.out.println("intfo size = "+info.size());
////			info.forEach(
////		            (key, value)-> showPosition( key,  value) );
//			
//			DefaultTableModel model = position.getTabelData();
////			System.out.println("model.getRowCount():" +model.getRowCount());
//			if(model.getRowCount()!=0) {
//				
//				inputPos.setText(""+model.getValueAt(0, 1));
//			}
//			
//		}
//	
//
//		@Override
//		public void receive(String msg, A_Tick tick) {
//			// TODO Auto-generated method stub
//			// === IN ===
//			HashMap<String, Double> data = (HashMap<String, Double>) tick.getData();
//			
//			inputTime.setText(""+data.get("time"));
//			inputBid.setText(""+data.get("bid"));
//			inputAsk.setText(""+data.get("ask"));
//			
//			String  sig = inputSignal.getText();
//			
//			// === OUT === 
//			gui.send(sig);
//		}
//
//		@Override
//		public void receive(String msg, A_Historical hist) {
//			// TODO Auto-generated method stub
//			
//		}
//
//	
//		@Override
//		public void receive(String msg, A_OrderStatus orderStatus) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void receive(String msg, A_Account account) {
//			// TODO Auto-generated method stub
////			System.out.println("ACcount GUI "+msg);
////			System.out.println(A_MSG.toHashMap(msg).get(AccountSummaryTag.BuyingPower.toString()));
//			inputBuyingPower.setText(A_MSG.toHashMap(msg).get(AccountSummaryTag.BuyingPower.toString()));
//			inputNetLiquidation.setText(A_MSG.toHashMap(msg).get(AccountSummaryTag.NetLiquidation.toString()));
//		}
//
//		@Override
//		public void receive(String msg, A_Indicator indicator) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
	
	A_AutoTradePanel(String msg){
		StackPanel leftTop = new StackPanel();
		leftTop.setBorder(new TitledBorder("Buy sell type"));
		leftTop.add(buysellType);
		
		
		///////////// left
		StackPanel leftPanel = new StackPanel();
		leftPanel.setBorder(new TitledBorder("Define Contract"));
		
		leftPanel.add(m_contractPanel);
		leftPanel.add(m_barDetailPanel);
		
		////////////////// right 
		JButton btn_start = new JButton("Start");
		JButton btn_stop = new JButton("Stop");
		InputText m_signal = new InputText("signal", "0");
		InputText m_qty = new InputText("QTY","0");
		InputText m_traget = new InputText("Traget","");
		InputText m_stop = new InputText("Stop", "");
		
		
		HorzPanel buttonPanel = new HorzPanel();
		buttonPanel.add(btn_start);		
		buttonPanel.add(btn_stop);
		buttonPanel.add(m_signal);
		buttonPanel.add(m_qty);
		buttonPanel.add(m_traget);
		buttonPanel.add(m_stop);
		
		VerticalPanel verti_barsig = new VerticalPanel();
		VerticalPanel verti_buysell = new VerticalPanel();
		verti_barsig.add(buttonPanel);
		
		JCheckBox c_signal = new JCheckBox("Real sig",true);
		JCheckBox c_bar = new JCheckBox("Real bar",true);
		
		InformationModel info = new InformationModel();
		PanelData infoPanel = new PanelData(info);
//		PanelData positionPanel = new PanelData();
////		PanelData accountPanel = new PanelData(m_accountsummaryModel);
//		PanelData livePanel = new PanelData(aTrade.aLiveOrder);
		
		verti_barsig.add(c_bar);
		verti_barsig.add(c_signal);
		StackPanel rightPanel = new StackPanel();
		rightPanel.setBorder(new TitledBorder("Information"));
		rightPanel.add(verti_barsig);
		rightPanel.add(infoPanel.getScrollPane());
//		rightPanel.add(histPanel.getScrollPane());
//		rightPanel.add(positionPanel.getScrollPane());
//		rightPanel.add(livePanel.getScrollPane());
		
		/////////////////////////
		
		
		
		HorzPanel horzPanel = new HorzPanel();
		VerticalPanel ver_main = new VerticalPanel();
		ver_main.add(leftTop);
		ver_main.add(leftPanel);
		horzPanel.add(ver_main);
//		horzPanel.add(m_chart);
		horzPanel.add(rightPanel);
		
		add(horzPanel);
		
		btn_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				System.out.println( m_contractPanel.getContract());
				String symbol = m_contractPanel.getContract().symbol();
				String dateFriday = m_contractPanel.getContract().lastTradeDateOrContractMonth();
				initFunction(symbol, dateFriday);
			}
		});
	}
	
	A_AutoTradePanel(){
		
//		myMediatorControl.mediator.add(gui);
//		myOption.mediator.add(gui);
		
		tableHistorical.setPreferredScrollableViewportSize(new Dimension(450,200));
		tableHistorical.setFillsViewportHeight(true);

		
	
		
		this.setLayout(new GridLayout(2, 3));
		JPanel column1 = new JPanel(new GridLayout(7, 1));
		column1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JButton btnInit = new JButton("Init");
		JButton btnSignal = new JButton("Signal");
		JButton btnGo = new JButton("Go");
		JButton btnStop = new JButton("Stop");
		
		JPanel panelBTN = new JPanel();
		panelBTN.add(btnInit);
		panelBTN.add(btnSignal);
		panelBTN.add(btnGo);
		panelBTN.add(btnStop);
		column1.add(panelBTN);
		
		JPanel panelStockDetail = new JPanel();
		panelStockDetail.add(inputSymbol);
		panelStockDetail.add(inputLivePrice);
		panelStockDetail.add(inputOptionCall);
		panelStockDetail.add(inputOptionPut);
		column1.add(panelStockDetail);

		
		JPanel panelDate = new JPanel(); 
		
		String s1[] = { "Thu", "Fri" , "Next Thu", "Next FRI"};
		 
        // create checkbox
		listDay = new JComboBox(s1);
		listDay.setSelectedItem(s1[1]);
		listDay.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if(e.getStateChange() == 1) {
					if(e.getItem().equals(s1[0])) {
//						myOption.setDayOfWeek(DayOfWeek.THURSDAY, false);
					}
					if(e.getItem().equals(s1[1])) {
//						myOption.setDayOfWeek(DayOfWeek.FRIDAY, false);
					}
					if(e.getItem().equals(s1[2])) {
//						myOption.setDayOfWeek(DayOfWeek.THURSDAY, true);
					}
					if(e.getItem().equals(s1[3])) {
//						myOption.setDayOfWeek(DayOfWeek.FRIDAY, true);
					}
//					inputlastTradeDate.setText(myOption.getLastDateOption());
				}

			}
		});

	
        // add ItemListener
		
		panelDate.add(inputlastTradeDate);
		panelDate.add(listDay);
		column1.add(panelDate);
		

		JPanel panelLiquiataion = new JPanel();
		panelLiquiataion.add(inputBuyingPower);
		panelLiquiataion.add(inputNetLiquidation);
		column1.add(panelLiquiataion);

		
		
		JPanel panelSig = new JPanel();
		panelSig.add(btnBuy);
		panelSig.add(btnSell);
		panelSig.add(btnWait);
		panelSig.add(inputSignal);
		column1.add(panelSig);
		
		JPanel bidAsk = new JPanel();
		bidAsk.add(inputTime);
		bidAsk.add(inputBid);
		bidAsk.add(inputAsk);
		column1.add(bidAsk);
		column1.add(btn);
		
		
		this.add(column1);
		
		JPanel column2 = new JPanel();
		scrolHistorical = new JScrollPane(tableHistorical);
		JScrollBar bar = scrolHistorical.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(40, 0));
		column2.add(scrolHistorical);
		this.add(column2);
		
		JPanel column3 = new JPanel(new GridLayout(10, 1));
		column3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		column3.add(inputContract);
		
		column3.add(inputPos);
		column3.add(inputLivePos);
		column3.add(inputQty);
		
		 
		column3.add(inputLiveContract);
		column3.add(inputLiveOrder);
		
		this.add(column3);
		
		JPanel column4 = new JPanel();
		JScrollPane scrollPosition = new JScrollPane(tablePosition);
		column4.add(scrollPosition);
		this.add(column4);
		
		
		btnInit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				initFunction();
			}
		});
		
		btnSignal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SignalFunction();
			}
		});
		
		btnGo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				goFunction();
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stopFunction();
			}
		});
		
		btnBuy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("1.0");
//				myOption.sig = Double.parseDouble(inputSignal.getText());
			}
		});
		btnSell.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("-1.0");
//				myOption.sig = Double.parseDouble(inputSignal.getText());
			}
		});
		btnWait.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputSignal.setText("0.0");
//				myOption.sig = Double.parseDouble(inputSignal.getText());
			}
		});
		
		
		
	}
	
	public void initFunction() {
		
		
		String symbol = inputSymbol.getText();
		String dateFirday = inputlastTradeDate.getText();
		
		initFunction( symbol,  dateFirday);
		
	
	}
	
	public void initFunction(String symbol, String dateFirday) {
//		myMediatorControl.start(symbol,dateFirday);
		
	
	}
	public void SignalFunction() {


	}
	

	public void  goFunction(){
		System.out.println("Start trade...");
		
		

	}

	public void stopFunction() {
//		gui.send("stop");
		System.out.println("Stop API");
		
	}

	@Override
	public void activated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}


