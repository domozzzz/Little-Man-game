package main.entity.tile;

import java.awt.image.BufferedImage;

public class Tree extends Tile{
	
	public Tree(BufferedImage image) {
		super(image);
		
		breakable = true;
		collision = true;
	}

}
