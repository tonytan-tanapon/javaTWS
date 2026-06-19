package autotrade;

import java.util.List;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.MarketDataType;
import com.ib.client.Order;
import com.ib.client.TickAttrib;
import com.ib.client.TickType;
import com.ib.client.Types.ExerciseType;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;
import com.ib.controller.ApiController.IContractDetailsHandler;
import com.ib.controller.ApiController.IOptHandler;
import com.ib.controller.ApiController.TopMktDataAdapter;

import apidemo.ApiDemo;

import apidemo.util.Util;

public class OptionChain implements IContractDetailsHandler{

	public void reqOptionChain() {
		System.out.println("SHOW OPTION CHAIN");
		
//		m_symbol.setText( "IBKR");
//		m_secType.setSelectedItem( SecType.STK);
//		m_exchange.setText( "SMART"); 
//		m_lastTradeDateOrContractMonth.setText("20170616");
//		m_currency.setText( "USD");
//		m_optExch.setText( "SMART");
//		m_marketDataType.setSelectedItem( MarketDataType.REALTIME);
//
//
//		m_underContract.symbol( m_symbol.getText().toUpperCase() ); 
//		m_underContract.secType( m_secType.getSelectedItem() ); 
//		m_underContract.exchange( m_exchange.getText().toUpperCase() ); 
//		m_underContract.currency( m_currency.getText().toUpperCase() ); 
		
//		Contract contract = ContractATS.getContractStock("AAPL");
		Contract contract = new Contract();
		contract.symbol("AAPL");
		contract.secType(SecType.STK);
		contract.lastTradeDateOrContractMonth("20230127");
		contract.currency("USD");
		contract.exchange("SMART");
//		contract
		API.INSTANCE.m_controller.reqContractDetails(contract, this);
	}
	
	public void placeStaddle() {
		String symbol = "AAPL";
		String right = "CALL";
		double strike = 140;
		String date = "20230127";
		
		Contract con1 = ContractATS.getContractOption(symbol, "C", strike, date);
		Contract con2 = ContractATS.getContractOption(symbol, "P", strike, date);
		
		OrderATS orderAts1 = new OrderATS();
		OrderATS orderAts2 = new OrderATS();
		
		Order order1 = orderAts1.buyMarket(1);
		Order order2 = orderAts2.buyMarket(1);
		
		PlaceOrderATS send = new PlaceOrderATS();
		send.placeOrder(con1, order1);
		send.placeOrder(con2, order2);
//		send.BracketOrder(parentOrderId, action, quantity, limitPrice, takeProfitLimitPrice, stopLossPrice)

	}

	@Override
	public void contractDetails(List<ContractDetails> list) {
		// TODO Auto-generated method stub
		System.out.println("LIST");
		System.out.println(list.toString());
		for (ContractDetails data : list) {
			System.out.println( "data ");
			Contract contract = data.contract();
			
//			if (contract.right() == Right.Put) {
//				System.out.println("PUT "+contract.strike());
//				addRow( contract, true );
//			}
//			else { 
//				System.out.println("CALL "+contract.strike());
//				addRow( contract, true );
//			}
		}
		
	}
	
	void addRow(Contract contract, boolean snapshot) {
		System.out.println("Contract strike " + contract.strike());
		ChainRow row = new ChainRow( contract);
		
//		m_list.add( row);
		
		ApiDemo.INSTANCE.controller().reqOptionMktData(contract, "", snapshot, false, row);
		
		if (snapshot) {
			Util.sleep( 11); // try to avoid pacing violation at TWS
		}
	}
	private class ChainRow extends TopMktDataAdapter implements IOptHandler {
		Contract m_c;
		double m_bid;
		double m_ask;
		int m_tickAttrib;
		double m_impVol;
		double m_delta;
		double m_gamma;
		double m_vega;
		double m_theta;
		boolean m_done;

		ChainRow(Contract contract) {
			m_c = contract;
		}

		@Override public void tickPrice(TickType tickType, double price, TickAttrib attribs) {
			switch( tickType) {
				case BID:
				case DELAYED_BID:
					m_bid = price;
					break;
				case ASK:
				case DELAYED_ASK:
					m_ask = price;
					break;
                default: break; 
			}
		}

		@Override public void tickOptionComputation( TickType tickType, int tickAttrib, double impVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
			if (tickType == TickType.MODEL_OPTION || tickType == TickType.DELAYED_MODEL_OPTION) {
				m_tickAttrib = tickAttrib;
				m_impVol = impVol;
				m_delta = delta;
				m_gamma = gamma;
				m_vega = vega;
				m_theta = theta;
			}
			System.out.println("optPrice "+optPrice);
		}
		
		@Override public void tickSnapshotEnd() {
			m_done = true;
		}
	}
}

