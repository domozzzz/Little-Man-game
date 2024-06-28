package main.level;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.entity.Entity;
import main.entity.Map;
import main.entity.mob.Mob;
import main.entity.mob.Player;
import main.entity.tile.Tile;
import main.gfx.Display;
import main.io.Sound;

public abstract class Level {
	
	protected String mapString;
	protected int humans = 0;
	protected int ghosts = 4;

	protected ArrayList<Entity> entities = new ArrayList<>();
	public Tile[] tiles = new Tile[9999];
	public static final int TILE_SIZE = 16;
	protected final int CAMERA_TILE_WIDTH = 16;
	protected final int CAMERA_TILE_HEIGHT = 16;
	protected Map map;
	protected Player player;
	private Player player2;
	
	public Level() {
		//map = IO.loadMap(mapString);
		loadTiles();
	}

	public void tick() {
		
		if (player != null) {
			player.tick();
		}
		
		if (player2 != null) {
			player2.tick();
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != null) {
				tiles[i].tick();
			}
		}
	}
	
	public void render(Display display) {
		//render tiles
		for (int x = 0; x < map.cols ; x++) {
			for (int y = 0; y < map.rows; y++) {
				Tile tile = tiles[map.tileMap[x + y*map.cols]];
				if (x - display.xScroll <= CAMERA_TILE_WIDTH && y - display.yScroll <= CAMERA_TILE_HEIGHT) {
					tile.render(display, x, y);
				}
			}
		}
		
		if (player != null) {
			player.render(display);
		}
		
		if (player2 != null) {
			player2.render(display);
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(display);
		}
	}
	
	public void removeMob(Mob mob) {
		entities.remove(mob);
	}
	
	//Divide for pixels to coordinates
	public Tile getTileAt(int x, int y) {
		return tiles[map.tileMap[x/CAMERA_TILE_WIDTH + y/CAMERA_TILE_HEIGHT*map.cols]];
	}
	
	protected abstract void loadTiles();
	
	public abstract void loadEntities();
	
	public Entity getEntityCollision(int x, int y, int w, int h) {
		
		Rectangle rect = new Rectangle(x, y, w, h);
		
		for (Entity e : entities) {
			if (e.rect != null) {
				Rectangle posRect = new Rectangle(e.x, e.y, e.rect.width, e.rect.height);
				if (rect.intersects(posRect)) {
					return e;
				}
			}
		}
		return null;
	}
	
	public int getTileTopLeftX(int x) {
		return x/CAMERA_TILE_WIDTH;
	}
	
	public int getTileTopLeftY(int y) {
		return y/CAMERA_TILE_HEIGHT*map.cols;
	}
	
	public boolean isSameTile(int tileNum, int x, int y) {
		int gridPos = x/CAMERA_TILE_WIDTH + y/CAMERA_TILE_HEIGHT*map.cols;
		
		if (tileNum == map.tileMap[gridPos]) {
			return true;
		}
		return false;
	}
	
	public void addTile(int tileNum, int x, int y) {
	    int gridPos = x/CAMERA_TILE_WIDTH + y/CAMERA_TILE_HEIGHT*map.cols;
		if (tiles[map.tileMap[gridPos]].isBreakable() && !isSameTile(tileNum, x, y)) {
			map.tileMap[gridPos] = tileNum;
			Sound.hit.play();
		}
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
		
	}
	
	public void addPlayer2(Player player2) {
		this.player2 = player2;
		player2.setLevel(this);
	}
	
	public void addPlayer(Player player) {
		this.player = player;
		player.setLevel(this);
	}
	
	public void removePlayer() {
		entities.remove(player);
	}

	public void removeTile(int x, int y) {	
		addTile(0, x, y);
	}

	public Map getMap() {
		return map;
	}

	public Player getPlayer() {
		return player;
	}
}
