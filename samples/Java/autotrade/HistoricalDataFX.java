package autotrade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.ib.client.Types.BarSize;
import com.ib.client.Types.DurationUnit;
import com.ib.client.Types.WhatToShow;
import com.ib.controller.ApiController.IHistoricalDataHandler;
import com.ib.controller.Bar;

public class HistoricalDataFX implements IHistoricalDataHandler {

	String symbol =  "GBP/USD";
	String filesymbol = symbol.split("/")[0]+symbol.split("/")[1];
//	EURGBP-(EURUSD/GBPUSD)
	WhatToShow  whatToShow = WhatToShow.ASK; // change

	int duration = 20;
	DurationUnit durationUnit = DurationUnit.DAY;
	BarSize barsize = BarSize._5_mins;
	
	String endDateTime;

	int complete = 1;
	public void reqHistoricalData(){

		
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
		Calendar cal = Calendar.getInstance();	
		endDateTime = form.format(cal.getTime());
		System.out.println("endDateTime " +endDateTime); // 20230128-06:00:00
		API.saveStart();
		System.out.println("get FX data");
		API.INSTANCE.m_controller.reqHistoricalData(ContractATS.getContractFX(symbol), 
				endDateTime, duration, 
				durationUnit, barsize, whatToShow, false, false, this);
	
		
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
		String filename= "D:\\FXdata\\"+filesymbol+whatToShow.toString()+".csv";

		String out = ""; 
		for(Bar bar : data) {
			out += bar.formattedTime()+","+bar.open()+","+bar.high()+","+bar.low()+","+bar.close()+"\n";
		}
//		System.out.println(out);
		
		LogFile.wirteFile(out, filename);
		
		API.saveStop();
		API.timeprocess();
		if( whatToShow.equals(WhatToShow.ASK)) {
			whatToShow = WhatToShow.BID; // change
			System.out.println("get "+symbol+" "+whatToShow);
			
			data.clear();
			API.saveStart();
			API.INSTANCE.m_controller.reqHistoricalData(ContractATS.getContractFX(symbol), 
					endDateTime, duration, 
					durationUnit, barsize, whatToShow, false, false, this);
		}
		else {
			System.out.println("Completed");
		}
	}

}
