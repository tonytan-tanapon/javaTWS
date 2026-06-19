package autotrade;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PanelOrder extends JPanel{
	PanelOrder(){
		GridLayout gridCenter = new GridLayout(0, 1);
		this.setLayout(gridCenter);
	
	}
	public JPanel getPanelOrder() {
		JPanel center_buttom = new JPanel();
		center_buttom.add(API.INSTANCE.pos.table.getScroll());
		center_buttom.add(API.INSTANCE.liveOrder.table.getScroll());
		this.add(center_buttom);
		return this;
	}
}
