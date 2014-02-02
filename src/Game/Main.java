package Game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Main extends BasicGame{
	SpriteSheet playerSprites;
	Image player;
	Input input;
	int x;
	int y;
	
	public Main(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		playerSprites = new SpriteSheet("gfx/Sprites.png", 16, 24);
		
		player = playerSprites.getSprite(0, 0);
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		if(input.isKeyDown(Input.KEY_W)){
			y++;
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		player.draw(x,y);
	}

	
	
	//Running the game.
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("RPG"));
			appgc.setDisplayMode(640, 480, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}