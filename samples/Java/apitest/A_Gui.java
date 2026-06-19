package apitest;

import javax.swing.JPanel;

import com.ib.client.Contract;

public abstract class A_Gui {
	protected A_Mediator mediator;
	protected String name;
	public A_Gui(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}

	public abstract void send(String msg,Object data);
	public abstract void send(String msg,Contract contract, BarDetailPanel barDetail);
	public abstract void receive(String msg, A_Tick tick);
	public abstract void receive(String msg, A_Historical hist);
	public abstract void receive(String msg, A_OptionChain optionChain);
	public abstract void receive(String msg, A_Position position);
	public abstract void receive(String msg, A_Account acc);
	public abstract void receive(String msg, A_OrderSend openSend);
//	public abstract void receive(String msg, A_Position position);
	public abstract JPanel getPanel();
}
