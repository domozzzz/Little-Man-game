package main.io;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
	
	public boolean interact, back, up, down, left, right, shoot, axe;

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		//universal
		case KeyEvent.VK_ENTER -> interact = true;
		case KeyEvent.VK_ESCAPE -> back = true;
		
		//unused
//		case KeyEvent.VK_Q -> dispose = true;
//		case KeyEvent.VK_E -> inv = true;
		
		//player1
		case KeyEvent.VK_W -> up = true;
		case KeyEvent.VK_S -> down = true;
		case KeyEvent.VK_A -> left = true;
		case KeyEvent.VK_D -> right = true;
		
		case KeyEvent.VK_SPACE -> shoot = true;
		case KeyEvent.VK_CONTROL -> axe = true;
		
		//player2
//		case KeyEvent.VK_UP -> up = true;
//		case KeyEvent.VK_DOWN -> down = true;
//		case KeyEvent.VK_LEFT -> left = true;
//		case KeyEvent.VK_RIGHT -> right = true;
		
//		case KeyEvent.VK_1 -> num = 0;
//		case KeyEvent.VK_2 -> num = 1;
//		case KeyEvent.VK_3 -> num = 2;
//		case KeyEvent.VK_4 -> num = 3;
//		case KeyEvent.VK_5 -> num = 4;
//		case KeyEvent.VK_6 -> num = 5;
//		case KeyEvent.VK_7 -> num = 6;
//		case KeyEvent.VK_8 -> num = 7;
//		case KeyEvent.VK_9 -> num = 8;
//		case KeyEvent.VK_0 -> num = 9;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		//player 1
			case KeyEvent.VK_W -> up = false;
			case KeyEvent.VK_S -> down = false;
			case KeyEvent.VK_A -> left = false;
			case KeyEvent.VK_D -> right = false;
			case KeyEvent.VK_ENTER -> interact = false;
			case KeyEvent.VK_SPACE ->  shoot = false;
			case KeyEvent.VK_CONTROL -> axe = false;
			case KeyEvent.VK_ESCAPE -> back = false;
		}
	}
	
	public void clear() {
		up = false;
		down = false;
		left = false;
		right = false;
		interact = false;
		shoot = false;
		back = false;
		axe = false;
	}
	
	public void clearNonMovement() {
		interact = false;
		back = false;
		shoot = false;
	}
}
