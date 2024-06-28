package main.Item;

import main.entity.mob.Player;
import main.gfx.SpriteSheet;

public class Axe extends Item {

	public Axe() {
		image = SpriteSheet.getSpriteImage(1*16, 6*16, 16, 16);
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!cooldown) {
			active = false;
		}
	}

	@Override
	public void use(Player player) {
		if (!active) {
			player.removeTile();
			
			if (player.getMobInFront() != null) {
				player.getMobInFront().damage(10);
			}
			
			player.swing(true);
			active = true;
			setCooldown(20);
		}
	}
}