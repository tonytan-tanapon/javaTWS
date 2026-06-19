package apitest;

import apidemo.util.NewTabbedPanel;

public class OptionsPanelTrade extends NewTabbedPanel {
	private final A_AutoTradePanel m_optionChains = new A_AutoTradePanel();
//	private final ExercisePanel m_exercisePanel = new ExercisePanel();
	
	OptionsPanelTrade() {
		NewTabbedPanel tabs = this;
		tabs.addTab( "Option Chains", m_optionChains);
//		tabs.addTab( "Option Exercise", m_exercisePanel);
	}
}
