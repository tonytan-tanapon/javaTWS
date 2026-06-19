package autotrade;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.controller.ApiController.IPositionHandler;

public class APosition extends Thread implements IPositionHandler{

	boolean complete = false;
	Dictionary<String, ArrayList<Double>> position = new Hashtable();

	public void reqPosition() {
		API.INSTANCE.m_controller.reqPositions(this);
	}
	
	public void run() {
		reqPosition();
		System.out.println("Load position ... ");
		while(!complete) {
//			System.out.println(complete);
		}
//		 complete = false;
		
	}


	@Override
	public void positionEnd() {
		System.out.println("END");
		 API.INSTANCE.m_controller.cancelPositions(this);
		 complete = true;
	}

	@Override
	public void position(String account, Contract contract, Decimal pos, double avgCost) {
		// TODO Auto-generated method stub
		String key = contract.symbol() +"/"+contract.currency();
		ArrayList<Double> value = new ArrayList<Double>();
		value.add(Double.parseDouble(""+pos));
		value.add(avgCost);
		position.put(key, value);
	}

}
