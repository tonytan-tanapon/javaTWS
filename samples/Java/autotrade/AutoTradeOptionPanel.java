package autotrade;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ib.client.Types.WhatToShow;

public class AutoTradeOptionPanel extends JPanel {


	public TableData tb_data = new TableData(new String[] { "Symbol", "time", "Bid", "Ask", "Avg", "Positon", "PL" });

	JButton btn_autoStart = new JButton("Start Trade");

	JButton btn_autoStop = new JButton("Stop Trade");
	JButton btnBuy = new JButton("Buy");
	JButton btnClose = new JButton("Close");

	InputText fxPos1 = new InputText("GBP/AUD", "20000");
	InputText fxPos2 = new InputText("GBP/USD", "40000");
	InputText fxPos3 = new InputText("AUD/USD", "40000");

	String symbol = "";
	String qty = "";

	AutoTradeOptionPanel(String symbol, String qty) {
		this.symbol = symbol;
		this.qty = qty;

		GridLayout grid_autotrade = new GridLayout(5, 1);
		JPanel sub_autotrade = new JPanel();
		sub_autotrade.setLayout(grid_autotrade);

		btn_autoStop.setEnabled(false);

		sub_autotrade.add(btn_autoStart);
		sub_autotrade.add(btn_autoStop);
		sub_autotrade.add(btnBuy);
		sub_autotrade.add(btnClose);
		sub_autotrade.add(fxPos1);
		sub_autotrade.add(fxPos2);
		sub_autotrade.add(fxPos3);

		add(tb_data.getScroll());
		add(sub_autotrade);

		btn_autoStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				autoStart();
			}
		});
		btn_autoStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				autoStop();
			}
		});

		btnBuy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				buyPosition();

			}
		});

		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeAllPosition();

			}
		});

	}




	public void autoStart() {
		AutoTradeOption autoTrade = new AutoTradeOption(this);
		autoTrade.requestRealTimeBar();

		btn_autoStop.setEnabled(true);
		btn_autoStart.setEnabled(false);
	}

	public void autoStop() {

		

		btn_autoStop.setEnabled(false);
		btn_autoStart.setEnabled(true);
	}

	public void buyPosition() {
		OptionChain optionChainATS = new OptionChain();
		optionChainATS.placeStaddle();
		
	}

	public void closeAllPosition() {

	}
}
