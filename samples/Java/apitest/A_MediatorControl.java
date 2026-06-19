package apitest;

public class A_MediatorControl {
	
	
	A_Mediator mediator = new A_MediatorImp();
	A_Position m_position = new A_PositionImp(mediator, "Position");
	A_Tick m_tick = new A_TickImp(mediator, "Tick");
	A_Account m_account = new A_AccountImp(mediator, "Account");
	A_Historical m_hist = new A_HistoricalImp(mediator, "Hist");

	A_OrderSend m_orderSend = new A_OrderSendImp(mediator, "OrderSend");
	A_OrderStatus m_OrdeStatus = new A_OrderStatusImp(mediator, "OrderStatus");
	A_OptionChain m_optionChain = new A_OptionChainImp(mediator, "Option Chain");
	A_Control m_control = new A_ControlImp(mediator, "Control");
	A_LiveOrder m_liveOrder = new A_LiveOrderImp(mediator, "live order");
	
	// GUI for option trading
	A_Gui m_gui = new A_GuiOptionImp(mediator,"Gui");
	private A_Detail detail = new A_Detail();
	
	A_MediatorControl(){
		
		// register component in register before using.
		mediator.add(m_position);
		mediator.add(m_tick);
		mediator.add(m_account);
		mediator.add(m_hist);
		mediator.add(m_orderSend);
		mediator.add(m_OrdeStatus);
		mediator.add(m_optionChain);
		mediator.add(m_control);
		mediator.add(m_liveOrder);
		mediator.add(m_gui);
		

	}
	

	
}
