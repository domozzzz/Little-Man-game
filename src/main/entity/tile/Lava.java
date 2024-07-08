package main.entity.tile;

import main.gfx.SpriteSheet;

public class Lava extends Tile {

	public Lava() {
		super(SpriteSheet.getSpriteImage(5*16, 0, Tile.TILE_SIZE, Tile.TILE_SIZE));
		
		collision = false;
		breakable  = false;
	}

}
