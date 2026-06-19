package apitest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class A_Button extends JPanel {
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
	public A_Button(){
	
	this.add(btn_buy);
	this.add(btn_sell);
	this.add(btn_wait);
	this.add(btn_close);
	
	this.add(btn_hist);
	this.add(btn_pos);
	this.add(btn_acc);
	this.add(btn_opt);
	this.add(btn_con);
	this.add(btn_open);
	
//	btn_hist.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			System.out.println("Hist");
//			control.reqHistorical();
//		}
//	});
//	
//	btn_pos.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			control.reqPosition();
//		}
//	});
//	
//	btn_acc.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			control.reqAccount();
//		}
//	});
//	
//	btn_opt.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			System.out.println("Req Option chain");
//			control.reqOptionChain("AAPL", A_Detail.getOptionExpireDate());
//		}
//	});
//	
//	btn_open.addActionListener(new ActionListener() {
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			// TODO Auto-generated method stub
//			System.out.println("Open order");
////			orderSend.receive("buycall", detail);
//			String msg = "buycall";
//			A_Detail detail = new A_Detail();
//			detail.symbol = "AAPL";
////			detail.callAT
//			detail.fridayOpion = A_Detail.getOptionExpireDate();
//			control.reqOrderSend(msg, detail);
//		}
//	});
	
	}
}
