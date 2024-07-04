package main.entity.mob;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import main.Item.Pistol;
import main.Item.Axe;
import main.entity.Entity;
import main.entity.Map;
import main.entity.tile.Tile;
import main.gfx.Display;
import main.gfx.SpriteSheet;
import main.io.Input;
import main.level.Level;

public class Player extends Mob {
	
	public final int REACH = 16;
	private final int BUILD_REACH = 16;
	private final int SPAWN_X = 1;
	private final int SPAWN_Y = 1;
	private int knockbackSpeed = 6;
	private int knockBack;
	private int ammo = 5;
	private boolean swinging;
	
	private BufferedImage bullet, heart, halfHeart, emptyHeart;
	
	private Pistol pistol;
	private Axe axe;
	private Input input;
	private Map map;
	private Game game;
	private int activeSlot;
	private int playerNumber;
		
	private boolean sideWalkSwitch;
	private boolean frozen, flipSide;
	private int screenCenterX, screenCenterY;
	
	public Player(Game game, int playerNumber) {
		this(game);
		this.playerNumber = playerNumber;
	}
	
	public Player(Game game) {
		this.game = game;
		input = game.getInput();
		
		killable = true;
		
		reset();
		loadImages();
		
		x = SPAWN_X * TILE_SIZE;
		y = SPAWN_Y * TILE_SIZE;
		
		screenCenterX = game.getDisplay().cameraWidth/2;
		screenCenterY = game.getDisplay().cameraHeight/2;
		
		initHitbox();
		image = front;
	}
	
	public void init(Level level) {
		pistol = new Pistol(level);
		axe = new Axe();
		
		this.level = level;
		}
 	
	public void tick() {
		super.tick();
				
		pistol.tick();
		axe.tick();
		
		handleInputs();
		handleEntities();
		doKnockBack();
		isGameOver();
	}
	
	protected void handleImages() {
		
		switch(lastDir) {
		case 'u':
			if (walking) {
				image = backWalk;
			} else {
				image = back;
			}
			break;
		case 'd':
			if (walking) {
				image = frontWalk;
			} else {
				image = front;
			}
			break;
		case 'l':
			flip = 2;
			if (walking) {
				if (tickCount % 12 == 0) {
					sideWalkSwitch = !sideWalkSwitch;
				}
				if (sideWalkSwitch) {
					image = sideWalk;
				} else {
					image = sideWalk2;
				}
			} else {
				image = side;
			}
			break;
		case 'r':
			flip = 0;
			if (walking) {
				if (tickCount % 12 == 0) {
					sideWalkSwitch = !sideWalkSwitch;
				}
				if (sideWalkSwitch) {
					image = sideWalk;
				} else {
					image = sideWalk2;
				}
			} else {
				image = side;
			}
			break;
		}
	}
	
	public void render(Display display) {
		//draw player
		super.render(display);
		
		pistol.render(display, x, y, 0);
		axe.render(display, x, y, 0);
		
		//health
		int fullHearts = hp/2;
		
		
		int emptyHearts = (maxHp - hp)/2;
		int nHalfHeart = 0;
		
		if (hp % 2 == 1) {
			nHalfHeart++;
		}
		
		for (int i = 0; i < fullHearts; i++) {
			display.render(heart, 8+(i)*8 + display.xScroll, 8 + display.yScroll, 0);
		}
		
		if (nHalfHeart == 1) {
			display.render(halfHeart, 8+(fullHearts)*8 + display.xScroll, 8 + display.yScroll, 0);
		}
		
		for (int i = 0; i < emptyHearts; i++) {
			display.render(emptyHeart, 8+(fullHearts+nHalfHeart+i)*8 + display.xScroll, 8 + display.yScroll, 0);
		}
		
		//bullets
		for (int i = ammo; i > 0; i--) {
			display.render(bullet, 15*16 -(i)*8 + display.xScroll, 8 + display.yScroll, 0);
		}
	}
	
	public void handleEntities() {
		handleTileEvent();
		handleEntityCollision();
	}
	
	public void handleInputs() {
		if (!frozen) {
			handleMovements();
		}
		handleActions();
	}

	private void handleActions() {
			
		if (input.back) {
			Game.paused = true;
		}
		
		if (input.shoot) {
			pistol.use(this);
		}
		
		if (input.axe) {
			axe.use(this);
		}
	}
	
	private void handleMovements() {
		
		if (playerNumber == 1) {
			if (input.up && y >= 0) {
				y--;
				walking = true;
				if (y >= screenCenterY && y < map.ph - screenCenterY) {
					game.getDisplay().yScroll--;
				}
				if (isCollision()) {
					y++;
					walking = false;
					if (y >= screenCenterY && y < map.ph - screenCenterY) {
						game.getDisplay().yScroll++;
					}
				}
				lastDir = 'u';
			}
			
			if (input.down && y < map.ph - Display.TILE_SIZE) {
				y++;
				walking = true;
				if (y > screenCenterY && y <= map.ph - screenCenterY) {
					game.getDisplay().yScroll++;
				}
				if (isCollision()) {
					y--;
					walking = false;
					if (y > screenCenterY && y <= map.ph - screenCenterY) {
						game.getDisplay().yScroll--;
					}
				}
				lastDir = 'd';
			}
			
			if (input.left && x >= 0) {
				x--;
				walking = true;
				if (x >= screenCenterX && x < map.pw - screenCenterX) {
					game.getDisplay().xScroll--;
				}
				if (isCollision()) {
					x++;
					walking = false;
					if (x >= screenCenterX && x < map.pw - screenCenterX) {
						game.getDisplay().xScroll++;
					}
				}
				lastDir = 'l';
			}
			
			if (input.right && x < map.pw - Display.TILE_SIZE) {
				x++;
				walking = true;
				if (x > screenCenterX && x <= map.pw - screenCenterX) {
					game.getDisplay().xScroll++;
				}
	
				if (isCollision()) {
					x--;
					walking = false;
					if (x > screenCenterX && x <= map.pw - screenCenterX) {
						game.getDisplay().xScroll--;
					}
				}
					
				lastDir = 'r';
			}
			handleImages();
			
			if (walking && (lastDir == 'u' || lastDir == 'd') && tickCount % 12 == 0) {
				flip = 2 - flip;
				//game.getAudio().playAudio(STEP_SOUND);
			}
			
			if (walking && (lastDir == 'l' || lastDir == 'r') && tickCount % 12 == 0) {
				flipSide = !flipSide;
			}
			walking = false;
		}
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Entity getEntityInFront() {
		return level.getEntityCollision(getXInFront(), getYInFront(), BUILD_REACH, BUILD_REACH);	
	}
	
	public Entity getTileInFront() {
		return level.getTileAt(getXInFront(), getYInFront());
	}
	
	private void handleTileEvent() {
		level.getTileAt(getCenterX(), getCenterY()).event(game);
	}
	
	private void isGameOver() {
		if (hp <= 0) {
			level.removePlayer();
			game.setMenu(game.titleMenu);
			reset();
		}
	}
	
	public void sendToSpawn() {
		x = SPAWN_X*SPRITE_SIZE;
		y = SPAWN_Y*SPRITE_SIZE;
		game.getDisplay().xScroll = 0;
		game.getDisplay().yScroll = 0;
	}
	
	public void reset() {
		hp = maxHp;
		sendToSpawn();
	}
		
	private void handleEntityCollision() {
		Entity entity = level.getEntityCollision(x + rect.x, y + rect.y, rect.width, rect.height);
		if (entity != null) {
			entity.event(level);
		}
	}

	public void loadImages() {
		//character
		front = SpriteSheet.getSpriteImage(0, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		back = SpriteSheet.getSpriteImage(1*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		side = SpriteSheet.getSpriteImage(2*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		frontWalk = SpriteSheet.getSpriteImage(3*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		backWalk = SpriteSheet.getSpriteImage(4*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		sideWalk = SpriteSheet.getSpriteImage(7*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		sideItem = SpriteSheet.getSpriteImage(6*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		sideWalk2 = SpriteSheet.getSpriteImage(8*SPRITE_SIZE, 3*SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
		
		//health
		heart = SpriteSheet.getSpriteImage(26*8, 17*8, Display.ICON_SIZE, Display.ICON_SIZE);
		halfHeart = SpriteSheet.getSpriteImage(27*8, 17*8, Display.ICON_SIZE, Display.ICON_SIZE);
		emptyHeart = SpriteSheet.getSpriteImage(28*8, 17*8, Display.ICON_SIZE, Display.ICON_SIZE);
		
		
		bullet = SpriteSheet.getSpriteImage(30*8, 17*8, Display.ICON_SIZE, Display.ICON_SIZE);
		//gui
	}
	
	@Override
	protected void initHitbox() {
		rect = new Rectangle();
		rect.x = 2;
		rect.y = 6;	
		rect.width = 7;
		rect.height = 9;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public void freeze() {
		frozen = true;
	}
	
	public void unfreeze() {
		frozen = false;
	}

	public Game getGame() {
		return game;
	}
	
	public int getXInFront() {
		int x = 0;
		switch (lastDir) {
			case 'u' -> x = getCenterX();
			case 'd' -> x = getCenterX();
			case 'l' -> x = getCenterX() - BUILD_REACH;
			case 'r' -> x = getCenterX() + BUILD_REACH;
		}
		return x;
	}
	
	public int getYInFront() {
		int y = 0;
		switch (lastDir) {
			case 'u' -> y = getCenterY() - BUILD_REACH;
			case 'd' -> y = getCenterY() + BUILD_REACH;
			case 'l' -> y = getCenterY();
			case 'r' -> y = getCenterY();
		}
		return y;
	}

	public void placeTile(Tile tile) {
		level.addTile(5, getXInFront(), getYInFront());
	}
	
	public void removeTile() {
		level.removeTile(getXInFront(), getYInFront());
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getDir() {
		return lastDir;
	}

	public int getMaxHp() {
		return maxHp;
	}
	
	public char getXDir() {
		char dir = lastDir;
		if (input.left) dir = 'l';
		if (input.right) dir = 'r';
		return dir;
	}

	public char getYDir() {
		char dir = lastDir;
		if (input.up) dir = 'u';
		if (input.down) dir = 'd';
		return dir;
	}
	
	public void knock(int dist)  {
		knockBack += dist;
	}
	
	public void doKnockBack() {
		if (knockBack > 0) {
		
			if (lastDir == 'd' && y >= 0) {
				y -= knockbackSpeed;
				walking = true;
				if (y >= screenCenterY && y < map.ph - screenCenterY) {
					game.getDisplay().yScroll -= knockbackSpeed;
				}
				if (isCollision()) {
					y += knockbackSpeed;
					walking = false;
					if (y >= screenCenterY && y < map.ph - screenCenterY) {
						game.getDisplay().yScroll += knockbackSpeed;
					}
					return;
				}
			}
			
			if (lastDir == 'u' && y < map.ph - Display.TILE_SIZE) {
				y += knockbackSpeed;
				walking = true;
				if (y > screenCenterY && y <= map.ph - screenCenterY) {
					game.getDisplay().yScroll += knockbackSpeed;
				}
				if (isCollision()) {
					y -= knockbackSpeed;
					walking = false;
					if (y > screenCenterY && y <= map.ph - screenCenterY) {
						game.getDisplay().yScroll -= knockbackSpeed;
					}
					return;
				}
			}
			
			if (lastDir == 'r' && x >= 0) {
				x -= knockbackSpeed;
				walking = true;
				if (x >= screenCenterX && x < map.pw - screenCenterX) {
					game.getDisplay().xScroll -= knockbackSpeed;
				}
				if (isCollision()) {
					x += knockbackSpeed;
					walking = false;
					if (x >= screenCenterX && x < map.pw - screenCenterX) {
						game.getDisplay().xScroll += knockbackSpeed;
					}
					return;
				}
			}
			
			if (lastDir == 'l' && x < map.pw - Display.TILE_SIZE) {
				x += knockbackSpeed;
				walking = true;
				if (x > screenCenterX && x <= map.pw - screenCenterX) {
					game.getDisplay().xScroll += knockbackSpeed;
				}
	
				if (isCollision()) {
					x -= knockbackSpeed;
					walking = false;
					if (x > screenCenterX && x <= map.pw - screenCenterX) {
						game.getDisplay().xScroll -= knockbackSpeed;
					}
					return;
				}
			}
			knockBack -= knockbackSpeed;
		} else  {
			knockBack = 0;
		}
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public int getActiveSlot() {
		return activeSlot;
	}

	public void setActiveSlot(int activeSlot) {
		this.activeSlot = activeSlot;
	}

	public Input getInput() {
		return input;
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public void removeAmmo() {
		if (ammo > 0) {
			ammo--;
		}
	}

	public void addAmmo() {
		ammo++;
	}

	public Mob getMobInFront() {
		if (getEntityInFront() instanceof Mob) {
			return (Mob) getEntityInFront();
		}
		return null;
	}

	public void swing(boolean bool) {
		swinging = bool;
	}
}