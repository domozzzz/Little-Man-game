package main.entity.tile;

import java.awt.image.BufferedImage;

public class Floor extends Tile {

	public Floor(BufferedImage image) {
		super(image);
		
		collision = false;
		breakable = true;
	}
}