package main.level;

import main.entity.mob.Ghost;
import main.entity.tile.Chest;
import main.entity.tile.Door;
import main.entity.tile.Floor;
import main.entity.tile.Tree;
import main.entity.tile.Wall;
import main.entity.tile.Water;
import main.gfx.SpriteSheet;
import main.io.IO;

public class Level2 extends Level {
	
	public Level2() {
		mapString = "/res/maps/map2.txt";
		map = IO.loadMap(mapString);
		loadTiles();
	}
	
	@Override
	protected void loadTiles() {
		tiles[0] = new Floor(SpriteSheet.getSpriteImage(4*TILE_SIZE, 0, TILE_SIZE, TILE_SIZE));
		tiles[1] = new Wall(SpriteSheet.getSpriteImage(0, 0, TILE_SIZE, TILE_SIZE));
		tiles[2] = new Tree(SpriteSheet.getSpriteImage(0, 3*TILE_SIZE, TILE_SIZE, TILE_SIZE));
		tiles[3] = new Door();
		tiles[4] = new Chest(this);
		tiles[5] = new Water();
		
	}
	
	@Override
	public void loadEntities() {
		for (int i = 0; i < ghosts; i++) {
			entities.add(new Ghost(this));
		}
	}
}
