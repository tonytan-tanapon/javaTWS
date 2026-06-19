package autotrade;


import java.util.HashMap;

import java.util.Map;
import java.util.Vector;

import com.ib.controller.ApiController.IAccountHandler;
import com.ib.controller.Position;


public class AccountATS implements IAccountHandler {

	String accountID = "";
	Map<String, Account> m_account = new HashMap<>();
	TableData table = new TableData();
	
//	public AccountATS(String accountID) {
//		System.out.println("Create Account");
//		System.out.println("Account ID: " + accountID);
//		this.accountID = accountID;
//	}
	public AccountATS(String accountID, TableData table) {
		System.out.println("Create Account");
		System.out.println("Account ID: " + accountID);
		this.accountID = accountID;
		
		this.table = table;
	}

	public void reqAccount() {
		API.INSTANCE.m_controller.reqAccountUpdates(true, accountID, this);
	}

	public void setAccount() {
//		System.out.println("m_account >>" + m_account);
		table.clearRows();
//		table.setRowCount(0);
		
		for (Map.Entry<String, Account> entry : m_account.entrySet()) {
			table.addRow(new String[] { 
					entry.getValue().account(), 
					entry.getValue().key(),
					entry.getValue().value(), 
					entry.getValue().currency()});

			if(entry.getValue().key().equals("NetLiquidation")) {
				API.txtMoney.setText(entry.getValue().value());
			}
			
		
		}
		
		
		
	}

	@Override
	public synchronized void accountValue(String account, String key, String value, String currency) {
		// TODO Auto-generated method stub
//		System.out.println("accountValue: " + " " + account + " " + key + " " + value + " " + currency);
		String MapKey = key + "," + currency;
		m_account.put(MapKey, new Account(account, key, value, currency));
		setAccount();
	}

	@Override
	public synchronized  void accountTime(String timeStamp) {
		// TODO Auto-generated method stub
		System.out.println("accountTime:" +timeStamp);
		
	}

	@Override
	public void accountDownloadEnd(String account) {
		// TODO Auto-generated method stub
//		setAccount();		
		table.setScrollToButtom();
		System.out.println("==============accountDownloadEnd========"+account);

	}

	@Override
	public void updatePortfolio(Position position) {
		// TODO Auto-generated method stub
		System.out.println(">>>>>>>>>>>>>>updatePortfolio");
		System.out.println("position update #### "+position.contract().localSymbol()+" "+position.position());
	}

}
