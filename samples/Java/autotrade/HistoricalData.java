package autotrade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.Bar;

public class HistoricalData implements IHistoricalDataHandler {

	double strikeStart = 147;
	double StrikeEnd = 150;
	
	String symbol =  "AAPL";
	String right = "C"; // C or P  // change
	WhatToShow  whatToShow = WhatToShow.ASK; // change
	int whattoShow = 1;
	
	double strike = strikeStart;  // change auto 	
	String exprise = "20230203";  // change
	
	
	String endDateTime;
	
	int getOption = 11 ;
	int complete = 1;
	public void reqHistoricalData(){

		
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		Calendar cal = Calendar.getInstance();	
		endDateTime = form.format(cal.getTime());
		System.out.println("endDateTime " +endDateTime); // 20230128-06:00:00
		
		//option
		if (getOption ==1) {
		API.INSTANCE.m_controller.reqHistoricalData(ContractATS.getContractOption(symbol,  right, strike, exprise), 
				endDateTime, 14, 
				DurationUnit.DAY, BarSize._1_min, whatToShow, false, false, this);
		}
		//stock
		else {
		API.INSTANCE.m_controller.reqHistoricalData(ContractATS.getContractStock(symbol), 
				endDateTime, 14, 
				DurationUnit.DAY, BarSize._1_min, whatToShow, false, false, this);
		}
//		
		
	}
	ArrayList<Bar> data = new ArrayList<Bar>();
	@Override
	public void historicalData(Bar bar) {
		// TODO Auto-generated method stub
		
		data.add(bar);
		
	}
	
	@Override
	public void historicalDataEnd() {
		// TODO Auto-generated method stub
		String filename ;
		// option
		if(getOption ==1) {
			filename= "D:\\optionStraddle\\"+symbol+exprise+right+strike+whatToShow.toString()+".csv";
		}
		else {
			 filename = "D:\\optionStraddle\\"+symbol+whatToShow.toString()+".csv";
		}
		
		

		String out = ""; 
		for(Bar bar : data) {
			out += bar.formattedTime()+","+bar.open()+","+bar.high()+","+bar.low()+","+bar.close()+"\n";
		}
//		System.out.println(out);
		
		LogFile.wirteFile(out, filename);
		
		
		/// preprocessing before next round
		strike++;
		data.clear(); //reset data
		
		if(strike> StrikeEnd && right.equals("C") && whatToShow.equals(WhatToShow.ASK)) {
			strike = strikeStart ;
			right = "P";
			whatToShow = WhatToShow.ASK; // change
			
		}else if(strike> StrikeEnd && right.equals("P") && whatToShow.equals(WhatToShow.ASK) ){
			strike = strikeStart ;
			right = "P";
			whatToShow = WhatToShow.BID; // change
		}else if(strike> StrikeEnd && right.equals("P") && whatToShow.equals(WhatToShow.BID) ){
			strike = strikeStart ;
			right = "C";
			whatToShow = WhatToShow.BID; // change
		}
			
	    // C or P  // change
		
		//System.out.println(strike +" "+right+" "+whatToShow);
		
		/// netround
		if(strike<= StrikeEnd) {
			System.out.println("get "+strike+" "+right+" "+whatToShow);
			API.INSTANCE.m_controller.reqHistoricalData(ContractATS.getContractOption(symbol,  right, strike, exprise), 
					endDateTime, 14, 
					DurationUnit.DAY, BarSize._1_min, whatToShow, false, false, this);
		}
		else {
			System.out.println("Completed");
		}
	}

}
