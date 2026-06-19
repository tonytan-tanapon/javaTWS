package apitest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.ib.client.Contract;

public class A_Detail {
	
	TableModel t_position;
	BarDetailPanel barDetail;
	
	// Contract  
	String symbol="AAPL";
	String fridayOpion = "20240405";
	Contract StockContract;
	Contract optionContract;
	
	
	//Option chain 
	ArrayList<Double> strikeList ;
	HashMap<Double, Contract> callData ;
	HashMap<Double, Contract> putData;
	double callATM =0.0;
	double putATM = 0.0;
	
	// Tick and Historical
	public HashMap<String , String > d_tick;
	String dateTick = "";
	double bidTick = 0.0;
	double askTick = 0.0;
	
	// Account 
	public double d_buyingPower = 0.0;
	public double d_netLiquidation = 0.0;
	
	
	// Position and order
	public HashMap<Contract, List<Object>> d_position;
	Contract ContractPosition;
	public double pos;
	public double avgCost;
	public double qty = 1;
	
	// Status
	public boolean positionState = false;
	public boolean accountState = false;
	public boolean optionState = false;
	public boolean liveOrderState = false;

	
	
	// Signal
	public double signal=0.0;
	
	
	static String[] columnNames = {"Contract","Pos","AvgCost","Price","PL"};
	DefaultTableModel tableModel = new DefaultTableModel();
	A_Detail(){
		
	}
	public static String getOptionExpireDate(DayOfWeek dayOfWeek) {
		LocalDate dt = LocalDate.now();   
		// next or previous
		String Friday = dt.with(TemporalAdjusters.next(dayOfWeek)).toString();
		String daySplit[] = Friday.split("-");
		Friday = daySplit[0]+daySplit[1]+daySplit[2];
		return Friday;
		
	}
	
	public static String getOptionExpireDate() {
		return getOptionExpireDate(DayOfWeek.FRIDAY);
	}



}
