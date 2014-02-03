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
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame{
	//Player
	SpriteSheet playerSprites;
	Image player;
	Input input;
	int walkSpeed = 250;
	int playerWidth;
	int playerHeight;
	
	//Map
	TiledMap map;
	
	//For the timer
	int counter;
	
	//Temporary boundary control
	int screenRightEdge = 600;
	int screenLeftEdge = 0;
	int screenTopEdge = 0;
	int screenBottomEdge = 430;
	
	//Collision
	boolean[][] blocked;
	
	//Player position 
	float x;
	float y;
	
	public Main(String gamename)
	{
		super(gamename);
	}

	//On startup method
	@Override
	public void init(GameContainer gc) throws SlickException {
		//Player
		playerWidth = 32;
		playerHeight = 48;
		
		playerSprites = new SpriteSheet("gfx/Sprites.png", playerWidth, playerHeight);
		player = playerSprites.getSprite(0,0);
		
		x = gc.getWidth() / 3;
		y = gc.getHeight() / 3;
		
		//Map
		map = new TiledMap("gfx/map.tmx");
		
		//Collision code from thejavablog.wordpress.com	
		blocked = new boolean[map.getWidth()][map.getHeight()];
		
		for (int xAxis=0;xAxis<map.getWidth(); xAxis++){
             for (int yAxis=0;yAxis<map.getHeight(); yAxis++){
                 int tileID = map.getTileId(xAxis, yAxis, 0);
                 String value = map.getTileProperty(tileID, "blocked", "false");
                 System.out.println(value);
                 if (value.equals("true")){
                     blocked[xAxis][yAxis] = true;
                 }
             }
         }
	}

	//Updating
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		input = gc.getInput();
			if(input.isKeyDown(Input.KEY_W)){
				if(!isBlocked(x, y - delta * 0.1f)){
					y-=0.1 * delta;
					timer(0,1,delta);
				}
			}else if(input.isKeyDown(Input.KEY_S) && y < screenBottomEdge){
				if (!isBlocked(x, y + playerHeight + delta * 0.1f)){
					y+=0.1 * delta;
					timer(0,0,delta);
				}
			}else if(input.isKeyDown(Input.KEY_A) && x > screenLeftEdge){
				if (!isBlocked(x - delta * 0.1f, y)){
					x-=0.1 * delta;
					timer(0,2,delta);
				}
			}else if(input.isKeyDown(Input.KEY_D) && x < screenRightEdge){
				if (!isBlocked(x + playerWidth + delta * 0.1f, y)){
					x+=0.1 * delta;
					timer(0,3,delta);
				}
			}
	}

	
	//Rendering
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		map.render(0, 0);
		
		player.draw(x,y);
	}
	
	//Custom timer
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
	
	//More collision
	private boolean isBlocked(float x, float y) {
        int xBlock = (int)x / 30;
        int yBlock = (int)y / 30;
        return blocked[xBlock][yBlock];
    }
	
	//Running the game.
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("RPG Game"));
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