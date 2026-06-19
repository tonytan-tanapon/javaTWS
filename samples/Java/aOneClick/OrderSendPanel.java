package aOneClick;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.OrderStatus;
import com.ib.controller.ApiController.IOrderHandler;

import aOneClick.OrdersPanel.OrdersModel;
import apidemo.util.HtmlButton;
import apidemo.util.VerticalPanel;


public class OrderSendPanel  extends JPanel{
	private OrdersModel m_model = new OrdersModel();
	private JTable m_table = new JTable( m_model);
	
	Contract contract;
	ContractPanel m_contractPanel ;
	Order order = new Order();
	
//	Contract contract;
	
//	private OrderPanel orderPanel = new OrderPanel();
	public OrderSendPanel(){
		
		// create contract panel
		contract =  ContractATS.getContractStock("AAPL");
		
		m_contractPanel= new ContractPanel(contract);
		
		HtmlButton ticket = new HtmlButton( "Place New Order") {
			@Override public void actionPerformed() {
				onPlaceOrder();
			}
		};
		HtmlButton cancel = new HtmlButton( "cancel") {
			@Override public void actionPerformed() {
				onPlaceOrder();
			}
		};
		
		JPanel buts = new VerticalPanel();
		buts.add( ticket);
//		buts.add( modify);
//		buts.add( attach);
		buts.add( cancel);
//		buts.add( cancelAll);
//		buts.add( reqExisting);
//		buts.add( reqFuture);
//		buts.add( refresh);
		
		setLayout( new BorderLayout() );
		
		add( buts, BorderLayout.EAST);
		add( m_contractPanel);
	}
	public void activated() {
		onRefresh();
	}
	protected void onRefresh() {
		m_model.clear();
		m_model.fireTableDataChanged();
//		ApiDemo.INSTANCE.controller().reqLiveOrders( m_model);
	}
	public void onPlaceOrder() {
//		TicketDlg dlg = new TicketDlg( null, null);
//		dlg.setVisible( true);
//		
		/// check margin
		
		
		/// place order
		Order order = OrderATS.buyMarket(100);
		PlaceOrderATS place = new PlaceOrderATS();
		place.placeOrder(contract, order);
	
	}

}
