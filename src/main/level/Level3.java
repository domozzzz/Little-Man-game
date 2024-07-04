package main.level;

import main.entity.tile.Chest;
import main.entity.tile.Door;
import main.entity.tile.Floor;
import main.entity.tile.Tree;
import main.entity.tile.Wall;
import main.entity.tile.Water;
import main.gfx.SpriteSheet;
import main.io.IO;

public class Level3 extends Level {
	
	public Level3() {
		ghosts = 16;
		mapString = "/res/maps/map3.txt";
		map = IO.loadMap(mapString);
		loadTiles();
	}
	
	@Override
	protected void loadTiles() {
		tiles[0] = new Floor(SpriteSheet.getSpriteImage(4*TILE_SIZE, 0, TILE_SIZE, TILE_SIZE));
		tiles[1] = new Wall(SpriteSheet.getSpriteImage(0, 0, TILE_SIZE, TILE_SIZE));
		tiles[2] = new Tree(SpriteSheet.getSpriteImage(1*TILE_SIZE, 2*TILE_SIZE, TILE_SIZE, TILE_SIZE));
		tiles[3] = new Door();
		tiles[4] = new Chest(this);
		tiles[5] = new Water();
		
	}
}
