package main.entity.tile;

import java.awt.image.BufferedImage;

import main.gfx.SpriteSheet;

public class Wall extends Tile {

	public Wall(BufferedImage image) {
		super(image);
		
		collision = true;
		breakable = false;
	}
}