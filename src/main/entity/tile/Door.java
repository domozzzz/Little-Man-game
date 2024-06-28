package main.entity.tile;

import main.Game;
import main.gfx.SpriteSheet;
import main.io.Sound;

public class Door extends Tile {

	public Door() {
		super(SpriteSheet.getSpriteImage(0, 2*SpriteSheet.GRID_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE));
		
		collision = false;
		breakable = false;
	}	
	
	@Override
	public void event(Game game) {
		if (!isCooldown()) {
			game.nextLevel();
			Sound.open.play();
			setCooldown(20);
		}
	}	
}
