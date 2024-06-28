package main.menu;

import main.Game;
import main.gfx.Display;
import main.io.Input;

public class WinMenu extends GameOverMenu{

	public WinMenu(Game game, Input input) {
		super(game, input);
	}
	
	@Override
	public void render(Display display) {

		//Title
		
		display.fillColor(0xFFFFFFFF);
		
		if (font != null) {
			font.draw("you win!",display, 10*8, 5*8);
			
			//Buttons
			font.draw("Replay", display, 10*8, 16*8);
			font.draw("Quit", display, 10*8, 18*8);
			
			switch (selected) {
			case 0 -> font.draw("a", display, 21*8, 16*8);
			case 1 -> font.draw("a", display, 21*8, 18*8);
			}
		}
	}

}
