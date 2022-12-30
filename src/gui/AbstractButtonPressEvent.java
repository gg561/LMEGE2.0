package gui;

import events.AbstractEvent;

public abstract class AbstractButtonPressEvent extends AbstractEvent<Button> {
	
	private Button button;
	
	public AbstractButtonPressEvent(Button button) {
		this.button = button;
		
	}

	@Override
	public void execute(Button t) {
		// TODO Auto-generated method stub
		
	}

}
