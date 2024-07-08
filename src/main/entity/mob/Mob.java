package main.entity.mob;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.entity.Entity;
import main.entity.Map;
import main.gfx.Display;
import main.io.Sound;
import main.level.Level;

public abstract class Mob extends Entity {
	
	protected Level level;
	protected Map map;
	protected int speed;
	protected int hp;
	public BufferedImage front, back, side, frontWalk, backWalk,
	sideWalk, heart, halfHeart, emptyHeart, bubble, hatImage, sideItem, sideWalk2, shootingStanding, shootingRunning, shootingFront, choppingStanding, choppingRunning, choppingFront;
	protected boolean walking;
	protected char lastDir;
	protected int flip;
	protected static final int SPRITE_SIZE = 16;
	protected boolean killable = true;
	protected  int maxHp = 10;

	public Mob() {
		initHitbox();
	}
	
	@Override
	public void render(Display display) {
		display.render(image, x, y, flip);
	}
	
	protected abstract void initHitbox();
	
	public Rectangle getRect() {
		return rect;
	}
	
	protected boolean isCollision() {
		if (level.getTileAt(x + rect.x, y + rect.y).isCollision()
				|| level.getTileAt(x + rect.x + rect.width, y + rect.y + rect.height).isCollision()
				|| level.getTileAt(x + rect.x + rect.width, y + rect.y).isCollision() 
				|| level.getTileAt(x + rect.x, y + rect.y + rect.height).isCollision()) {
			return true;
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		isDead();
	}
	
	public int getHp() {
		return hp;
	}
	
	public void damage(int damage) {
		if (killable) {
			hp -= damage;
		}
	}
	
	public void heal(int hp) {
		if (this.hp + hp <= maxHp) {
			this.hp += hp;
		} else {
			this.hp = maxHp;
		}
	}
	
	protected void isDead() {
		if (hp <= 0) {
			Sound.death.play();
			level.removeMob(this);
		}
	}
}