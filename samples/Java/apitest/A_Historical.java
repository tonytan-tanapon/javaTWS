package apitest;

import javax.swing.table.DefaultTableModel;

import com.ib.client.Contract;

public abstract class A_Historical {
	protected A_Mediator mediator;
	protected String name;
	boolean state = true;
	public A_Historical(A_Mediator med, String name){
		this.mediator=med;
		this.name=name;
	
	}
	
	public abstract void send(String msg,Object data);
	
	public abstract void receive(String msg);
	public abstract void receive(String msg, Contract contract, BarDetailPanel barDetail);
	public abstract void reqHistorical();
	
	public abstract Object getData();
	public abstract DefaultTableModel getTableData();
}
