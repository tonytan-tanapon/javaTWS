package aOneClick;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import apidemo.util.NewTabbedPanel.NewTabPanel;

public class AoneClickPanel extends NewTabPanel {
//	private final OrdersPanel m_ordersPanel = new OrdersPanel();
//    private final CompletedOrdersPanel m_completedOrdersPanel = new CompletedOrdersPanel();
//	private final TradesPanel m_tradesPanel = new TradesPanel();
	
	private final OrderSendPanel m_orderSendPanel = new OrderSendPanel();
	
	AoneClickPanel() {
		m_orderSendPanel.setPreferredSize( new Dimension( 1, 10000));
//        m_completedOrdersPanel.setPreferredSize( new Dimension( 1, 10000));
//		m_tradesPanel.setPreferredSize( new Dimension( 1, 10000));
		
		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));
		add( m_orderSendPanel);
//        add( m_completedOrdersPanel);
//		add( m_tradesPanel);
	}

	/** Called when the tab is first visited. */
	@Override public void activated() {
		m_orderSendPanel.activated();
//        m_completedOrdersPanel.activated();
//		m_tradesPanel.activated();
		
		
		
	}

	/** Called when the tab is closed by clicking the X. */
	@Override public void closed() {
	}
}
