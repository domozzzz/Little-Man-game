package main.menu;

import main.Game;
import main.gfx.Display;
import main.io.Input;

public class PauseMenu extends Menu {

	public PauseMenu(Game game, Input input) {
		super(game, input);
		MAX_SELECTION = 2;
	}
	
	public void tick() {
		if (input.back) {
			Game.paused = false;
		}
		input.clear();
	}
	
	public void render(Display display) {
		font.draw("paused", display, 16);
		font.draw("unpause", display, 40);
		font.draw("back to main menu", display, 58);
	}
}
