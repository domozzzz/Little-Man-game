package main.Item;

import main.gfx.SpriteSheet;

public class Ammo extends Item{

	public Ammo() {
		image = SpriteSheet.getSpriteImage(2*TILE_SIZE, 6*TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
}