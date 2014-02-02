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
	int counter;
	int walkSpeed = 250;
	float x;
	float y;
	
	public Main(String gamename)
	{
		super(gamename);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		playerSprites = new SpriteSheet("gfx/Sprites.png", 32, 48);
		player = playerSprites.getSprite(0, 0);
		
		x = gc.getWidth() / 2;
		y = gc.getHeight() / 2;
	}

	@Override
	public void update(GameContainer gc, int i) throws SlickException {
		input = gc.getInput();
			if(input.isKeyDown(Input.KEY_W)){
				y-=0.1 * i;
				timer(0,1,i);
			}else if(input.isKeyDown(Input.KEY_S)){
				y+=0.1 * i;
				timer(0,0,i);
			}else if(input.isKeyDown(Input.KEY_A)){
				x-=0.1 * i;
				timer(0,2,i);
			}else if(input.isKeyDown(Input.KEY_D)){
				x+=0.1 * i;
				timer(0,3,i);
			}
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		player.draw(x,y);
	}
	
	public void timer(int row, int column, int delta){
		counter += delta;
		if(counter < walkSpeed){
			player = playerSprites.getSprite(row, column);
		}else if(counter < walkSpeed * 2){
			player = playerSprites.getSprite(row + 2, column);
		}else{
			counter = 0;
		}
	}
	
	
	//Running the game.
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("RPG"));
			appgc.setDisplayMode(640, 480, false);
			appgc.setTargetFrameRate(60); 
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}