package main.menu;

import main.Game;
import main.gfx.Display;
import main.io.Input;

public class TitleMenu extends Menu {

	public TitleMenu(Game game, Input input) {
		super(game, input);
		MAX_SELECTION = 1;
	}
	
	@Override
	public void tick() {
		if (input.interact) {
			switch (selected) {
			case 0 -> game.play();
			case 1 -> System.exit(0);
			}
		}
		super.tick();
	}

	public void render(Display display) {
		
		display.fillColor(0xFFFFFFFF); // white

		if (font != null) {
			//Title
			font.draw("Haunted Labyrinth", display, 5*8);
			
			//Buttons
			font.draw("Start", display, 10*8, 16*8);
			font.draw("Quit", display, 10*8, 18*8);
			
			switch (selected) {
				case 0 -> font.draw("a", display, 21*8, 16*8);
				case 1 -> font.draw("a", display, 21*8, 18*8);
			}
		}
	}
}