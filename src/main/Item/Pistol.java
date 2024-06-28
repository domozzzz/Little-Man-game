package main.Item;

import main.entity.Bullet;
import main.entity.mob.Player;
import main.gfx.SpriteSheet;
import main.io.Sound;
import main.level.Level;

public class Pistol extends Item {
	
	
	public Pistol(Level level) {
		
		this.level = level;
		
		image = SpriteSheet.getSpriteImage(1*16, 12*8, 16, 16);		 
	}

	@Override
	public void use(Player player) {
		if (!cooldown && player.getAmmo() > 0) {
			
			level.addEntity(new Bullet(level, player.x, player.y, player.getXDir(), player.getYDir()));
			player.removeAmmo();
			
			Sound.shoot.play();
			active = true;
			setCooldown(20);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!cooldown) {
			active = false;
		}
	}
}
