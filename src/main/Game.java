package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import main.entity.mob.Player;
import main.gfx.Display;
import main.io.Input;
import main.io.Sound;
import main.level.Level;
import main.level.Level1;
import main.level.Level2;
import main.level.Level3;
import main.menu.GameOverMenu;
import main.menu.Menu;
import main.menu.PauseMenu;
import main.menu.TitleMenu;
import main.menu.WinMenu;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public Menu menu, characterMenu, winMenu, titleMenu, pauseMenu, gameOverMenu;
	private Level level;
	public static boolean paused;
	
	private final String TITLE = "Maze";
	public static final int SCREEN_HEIGHT = 256;
	public static final int SCREEN_WIDTH = 256;
	public static final int FPS = 40;

	private LevelType levelType;
	private boolean running;
	private int[] pixels;
	private Level[] levels;
	private BufferedImage displayImage;
	private Display display;
	private Input input;
	private Player player;
	private Sound audio;
	
	public enum LevelType {
		MAIN,
		ONE,
		TWO,
		THREE,
		WIN,
		LOSE;
	    
	    public LevelType next() {
	    	return values()[(this.ordinal() + 1) % values().length];
	    }
	}

	public Game() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

		displayImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();

		display = new Display();
		input = new Input();
		addKeyListener(input);
		player = new Player(this, 1);

		loadMenus();
		setMenu(titleMenu);
		loadLevels();
		level = levels[0];
		levelType = LevelType.ONE;
	}
	
	private void loadLevels() {
		levels = new Level[3];
		levels[0] = new Level1();
		levels[1] = new Level2();
		levels[2] = new Level3();
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void run() {

		long lastTime = System.nanoTime();
		double unprocessedTime = 0;
		double nsPerTick = 1000000000.0 / FPS;

		while (running) {
			long currentTime = System.nanoTime();
			unprocessedTime += (currentTime - lastTime) / nsPerTick;
			lastTime = currentTime;

			if (unprocessedTime >= 1) {
				tick();
				render();
				unprocessedTime--;
			}
		}
	}

	private void tick() {
		if (menu != null) {
			menu.tick();
		} else  {
			level.tick();
		}
	}

	private void render() {

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = displayImage.getGraphics();
		display.clear();
		
		if (menu != null) {
			menu.render(display);
		} else {
			level.render(display);
		}

		// switch pixels
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = display.pixels[i];

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	private void loadMenus() {
		titleMenu = new TitleMenu(this, input);
		//characterMenu = new CharacterMenu(this, input, player);
		pauseMenu = new PauseMenu(this, input);
		gameOverMenu = new GameOverMenu(this, input);
		winMenu = new WinMenu(this, input);
	}

	public Display getDisplay() {
		return display;
	}

	public void setLevel(Level level) {
		this.level = level;
		player.setMap(level.getMap());
		player.init(level);
		
		level.addPlayer(player);
		level.loadEntities();
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public void reset() {
		setMenu(null);
		player.reset();
		levelType = LevelType.ONE;
		level = new Level1();
		setLevel(level);
	}

	public Player getPlayer() {
		return player;
	}
	
	public Menu getMenu() {
		return menu;
	}

	public Input getInput() {
		return input;
	}
	
	public Sound getAudio() {
		return audio;
	}
	
	public void nextLevel() {
		levelType = levelType.next();
		
		switch (levelType) {
		case ONE:
			level = levels[0];
			break;
		case TWO:
			level = levels[1];
			break;
		case THREE:
			level = levels[2];
			break;
		case WIN:
			setMenu(winMenu);
			//(GameOverMenugameOverMenu.setCoolDown(20);
			break;
		case LOSE:
			setMenu(gameOverMenu);
			break;
		default:
			break;
		}
		
		setLevel(level);
		player.sendToSpawn();
		player.setMap(level.getMap());
		level.addPlayer(player);
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.add(game);
		frame.setTitle(game.TITLE);
		frame.pack();
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	

		game.start();
	
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public Level getLevel() {
		return level;
	}
	
	public void play() {
		setMenu(null);
		setLevel(level);
	}
}
