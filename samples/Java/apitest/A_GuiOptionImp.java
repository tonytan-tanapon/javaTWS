package apitest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;
import com.ib.controller.AccountSummaryTag;

import apidemo.util.VerticalPanel;
import apidemo.util.NewTabbedPanel.INewTab;
import apidemo.util.VerticalPanel.HorzPanel;
import apidemo.util.VerticalPanel.StackPanel;
import autotrade.ContractATS;

public class A_GuiOptionImp extends A_Gui implements INewTab{
	public StackPanel stackPanel =  new StackPanel();
	
	String buyType[] = {"Simple", "Dynamic", "Bracket"}; 
	JComboBox buysellType = new JComboBox(buyType);
	
	String symbol[] = {"AAPL","AMD","TSLA", "TSLA","EUR"}; 
	JComboBox symbolBox = new JComboBox(symbol);

	String tradeType[] = {"OPT", "STK", "CASH"}; 
	JComboBox tradeTypeBox = new JComboBox(tradeType);
	
//	public Contract m_contract = new ContractATS().getContractOption("AAPL", "C", 170.0, A_Detail.getOptionExpireDate());
	public Contract m_contract = new ContractATS().getContractStock("AAPL");
	public ContractPanel m_contractPanel = new ContractPanel(m_contract);
	public BarDetailPanel m_barDetailPanel = new BarDetailPanel();
	
	InputText m_signal = new InputText("signal", "0.0");
	InputText m_qty = new InputText("QTY","0");
	InputText m_buyingPower = new InputText("Buying","");
	InputText m_netLiq = new InputText("Net", "");
	InputText m_time = new InputText("Time","");
	InputText m_bid = new InputText("Bid","");
	InputText m_ask = new InputText("Ask", "");
	InputText m_call = new InputText("Call","");
	InputText m_put = new InputText("Put", "");

    
	A_Table posTable = new A_Table(A_PositionImp.columnNames);
	A_Table hisTable = new A_Table(A_HistoricalImp.columnNames);
	A_Table detailTable = new A_Table(A_Detail.columnNames);
	
	
	public A_GuiOptionImp(A_Mediator med, String name) {
		// A_GUI()
//		public A_Gui(A_Mediator med, String name){
//			this.mediator=med;
//			this.name=name;
//		
//		}
		super(med, name);
		
		createPanel();
		// TODO Auto-generated constructor stub
	}
	
	public void createPanel() {
		/////// left top
		StackPanel leftTop = new StackPanel();
		leftTop.setBorder(new TitledBorder("Buy sell type"));
		leftTop.add(buysellType);
		leftTop.add(symbolBox);
		leftTop.add(tradeTypeBox);
		
		///////////// left
		StackPanel leftPanel = new StackPanel();
		leftPanel.setBorder(new TitledBorder("Define Contract"));
		
		leftPanel.add(m_contractPanel);
		leftPanel.add(m_barDetailPanel);
		
		////////////////// right 
		JButton btn_init = new JButton("Init");
		JButton btn_start = new JButton("Start");
		JButton btn_stop = new JButton("Stop");
		
		
		
		HorzPanel buttonPanel = new HorzPanel();
		buttonPanel.add(btn_init);		
		buttonPanel.add(btn_start);		
		buttonPanel.add(btn_stop);
		buttonPanel.add(m_signal);
		buttonPanel.add(m_qty);
		buttonPanel.add(m_buyingPower);
		buttonPanel.add(m_netLiq);
		
		HorzPanel buttonPanel2 = new HorzPanel();
		buttonPanel2.add(m_time);
		buttonPanel2.add(m_bid);
		buttonPanel2.add(m_ask);
		
		buttonPanel2.add(m_call);
		buttonPanel2.add(m_put);
		
		VerticalPanel verti_barsig = new VerticalPanel();
		VerticalPanel verti_buysell = new VerticalPanel();
		verti_barsig.add(buttonPanel);
		verti_barsig.add(buttonPanel2);
		
//		JCheckBox c_signal = new JCheckBox("Real sig",true);
//		JCheckBox c_bar = new JCheckBox("Real bar",true);
		
		InformationModel info = new InformationModel();
		PanelData infoPanel = new PanelData(info);

		
//		verti_barsig.add(c_bar);
//		verti_barsig.add(c_signal);
		JButton btn_buy = new JButton("Buy");
		JButton btn_sell = new JButton("Sell");
		JButton btn_wait = new JButton("Wait");
		JButton btn_close = new JButton("Close");
		
		JButton btn_hist = new JButton("Hist");
		JButton btn_pos = new JButton("Pos");
		JButton btn_acc = new JButton("Acc");
		JButton btn_opt = new JButton("Opt");
		JButton btn_con = new JButton("Con");
		JButton btn_open = new JButton("Open");
		
		JPanel btn_panel = new JPanel();
		btn_panel.add(btn_buy);
		btn_panel.add(btn_sell);
		btn_panel.add(btn_wait);
		btn_panel.add(btn_close);
		btn_panel.add(btn_hist);
		btn_panel.add(btn_pos);
		btn_panel.add(btn_opt);
		btn_panel.add(btn_con);
		btn_panel.add(btn_open);
		
		verti_barsig.add(btn_panel);
		StackPanel rightPanel = new StackPanel();
		rightPanel.setBorder(new TitledBorder("Information"));
		rightPanel.add(verti_barsig);
		rightPanel.add(detailTable.scrollPane);
		rightPanel.add(posTable.scrollPane);
		rightPanel.add(hisTable.scrollPane);

		
		/////////////////////////
		
		
		
		HorzPanel horzPanel = new HorzPanel();
		VerticalPanel ver_main = new VerticalPanel();
		ver_main.add(leftTop);
		ver_main.add(leftPanel);
		horzPanel.add(ver_main);
		horzPanel.add(rightPanel);
		
		stackPanel.add(horzPanel);
		
		
		btn_init.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				initFunction();
			}
		});
		
		btn_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				startFunction();
			}
		});
		
		btn_stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				stopFunction();
			}
		});
		
		btn_hist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getHist();
			}

			
		});
		
		
		btn_buy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				m_signal.setText("1.0");
				setSignal(A_MSG.Buy);
			}
		});
		
		btn_sell.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				m_signal.setText("-1.0");
				setSignal(A_MSG.Sell);
			}
		});
		btn_wait.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				m_signal.setText("0.0");
				setSignal(A_MSG.Wait);
			}
		});
		
		btn_close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				m_signal.setText("10.0");
				setSignal("10.0");
			}
		});
		
		symbolBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange() == 1 ) {
					
				  	m_contractPanel.m_symbol.setText(symbolBox.getSelectedItem().toString());
				    	
				
				}
			}
		});
		
		
		tradeTypeBox.addItemListener(new  ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if(e.getStateChange() == 1 ) {
					switch(tradeTypeBox.getSelectedItem().toString()) {
					    case "OPT": 
					    	m_contractPanel.m_secType.setSelectedIndex(2);
					    	m_contractPanel.m_lastTradeDateOrContractMonth.setText(m_contract.lastTradeDateOrContractMonth());
					    	m_contractPanel.m_strike.setText(m_contract.strike());
					    	m_contractPanel.m_right.setSelectedIndex(1);
					    	m_contractPanel.m_multiplier.setText(m_contract.multiplier());
					    	m_contractPanel.m_exchange.setText("SMART");
					    	break;
					    case "CASH": 
					    	m_contractPanel.m_secType.setSelectedIndex(5);
					    	m_contractPanel.m_lastTradeDateOrContractMonth.setText("");
					    	m_contractPanel.m_strike.setText("");
					    	m_contractPanel.m_right.setSelectedIndex(0);
					    	m_contractPanel.m_multiplier.setText("");
					    	m_contractPanel.m_exchange.setText("IDEALPRO");
					    	
					    	break;
					    case "STK": 
					    	m_contractPanel.m_secType.setSelectedIndex(1);
					    	m_contractPanel.m_lastTradeDateOrContractMonth.setText("");
					    	m_contractPanel.m_strike.setText("");
					    	m_contractPanel.m_right.setSelectedIndex(0);
					    	m_contractPanel.m_multiplier.setText("");
					    	m_contractPanel.m_exchange.setText("SMART");
					    	
					    	break;
					
					}
						
//					m_contractPanel.m_secType.setSelectedItem(m_contractPanel.m_secType.item);
//					System.out.println(tradeTypeBox.getSelectedItem());
				}
				
			}
		});
	}
	
	public void setSignal(String sig) {
		// mediator use overload to select the function
		mediator.sendMessage(sig, this);
		
	}
	public void initFunction() {
		String symbol = m_contractPanel.getContract().symbol();
		String dateFriday = m_contractPanel.getContract().lastTradeDateOrContractMonth();
		send(A_MSG.Init, m_contractPanel.getContract(), m_barDetailPanel);	
		
		
//		HashMap<String, String> msg = new HashMap<String, String>();
//		msg.put("cmd", A_MSG.Init);
//		msg.put("symbol", symbol);
//		msg.put("dateFirday", dateFriday);
		
		//Message A_MSG.Init
//		{ 	"cmd": A_MSG.Init,
//			"symbol": symbol,
//			"dateFirday": dateFriday }
		
		// call send(HashMap<String, String> msg);
//		send(msg);
	}
	
	
	public void startFunction() {
		String symbol = m_contractPanel.getContract().symbol();
		String dateFriday = m_contractPanel.getContract().lastTradeDateOrContractMonth();
		
//		HashMap<String, String> msg = new HashMap<String, String>();
//		msg.put("cmd", A_MSG.Start);
//		msg.put("symbol", symbol);
//		msg.put("dateFirday", dateFriday);
//		System.out.println("aaaaaaaaaa");
		
		// send contact detail and time frame for trading 
		send(A_MSG.Start, m_contractPanel.getContract(), m_barDetailPanel);	
		
		
	
	}
	
	public void stopFunction() {
		System.out.println("Stop API");
//		
//		HashMap<String, String> msg = new HashMap<String, String>();
//		msg.put("cmd", A_MSG.Stop);
//		send(msg);
//		send(A_MSG.Stop, this);
		mediator.sendMessage(A_MSG.Stop, this);
		
	}
	public void send(HashMap<String, String> msg) {
		
		// calll send(HashMap<String, String> msg, Object data)
		// this is A_Gui
		send(msg.toString(), this);
		
	}
	
	public void getHist() {
		// TODO Auto-generated method stub
		String symbol = m_contractPanel.getContract().symbol();
		String currency = m_contractPanel.getContract().currency();
			
		HashMap<String, String> msg = new HashMap<String, String>();
		msg.put("cmd", A_MSG.Historical);
		msg.put("symbol", symbol);
		msg.put("currency", currency);
//		msg.put("dateFirday", dateFirday);
//		send(msg);
		System.out.println("GET HIST");
		mediator.sendMessage("Hist", m_contractPanel.getContract(), m_barDetailPanel);
	}
	@Override
	public void send(String msg, Object data) {
		// TODO Auto-generated method stub
		// override A_GUI
		// real sendMessage to A_MediatorIMP class
		mediator.sendMessage(msg, this);
	}


	@Override
	public void activated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return stackPanel;
	}

	@Override
	public void receive(String msg, A_Tick tick) {
		// TODO Auto-generated method stub
		HashMap<String, String> map =  (HashMap<String, String>) tick.getData();
		m_time.setText(map.get("time").split("-")[1]);
		m_bid.setText(map.get("bid"));
		m_ask.setText(map.get("ask"));
	}

	@Override
	public void receive(String msg, A_Historical hist) {
		// TODO Auto-generated method stub
//		System.out.println("Send to hist");
		hisTable.update( hist.getTableData());
//		detailTable.updateDetail(hist.getTableData(),"hist");
	}

	@Override
	public void receive(String msg, A_OptionChain optionChain) {
		// TODO Auto-generated method stub
		
		System.out.println("testtttt>>>>  " +msg);
		
	}

	@Override
	public void receive(String msg, A_Position position) {
		// TODO Auto-generated method stub

		posTable.update( position.getTabelData());
		detailTable.updateDetail(position.getTabelData(), "pos");
	}
	
	@Override
	public void receive(String msg, A_Account acc) {
		// TODO Auto-generated method stub
		HashMap<AccountSummaryTag, String> data = (HashMap<AccountSummaryTag, String>) acc.getData();

		m_buyingPower.setText(data.get(AccountSummaryTag.BuyingPower));
		m_netLiq.setText(data.get(AccountSummaryTag.NetLiquidation));
	}

	@Override
	public void receive(String msg, A_OrderSend openSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(String msg, Contract contract, BarDetailPanel bardetail) {
		// TODO Auto-generated method stub
		mediator.sendMessage(msg, contract, bardetail);
	}

	

}
