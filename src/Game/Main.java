package Game;

import java.util.ArrayList;
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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame{
	 //General
	static int FPS = 60;
	
	//Player
	SpriteSheet playerSprites;
	Rectangle playerBoundingRect;
	Image player;
	Input input;
	int walkSpeed = 250;
	int playerWidth;
	int playerHeight;
	int hitBox = 24;
	int cameraX;
	int cameraY;
	
	//Map
	TiledMap map;
	int mapX;
	int mapY;
	
	//For the timer
	int counter;
	
	//Temporary boundary control
	int screenRightEdge = 600;
	int screenLeftEdge = 0;
	int screenTopEdge = 0;
	int screenBottomEdge = 430;
	
	//Collision
	ArrayList<Rectangle> rectList;
	Rectangle[] rects;
	int blockSize = 30;
	
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
		playerBoundingRect = new Rectangle(x, y - playerHeight, playerWidth, hitBox);
		
		x = gc.getWidth() / 3;
		y = gc.getHeight() / 3;
		
		cameraX = (int) ((gc.getWidth() / 2) - (x / 2));
		cameraY = (int) ((gc.getHeight() / 2) - (y / 2));
		
		//Map
		map = new TiledMap("gfx/map.tmx");
		
		//Collision code from thejavablog.wordpress.com	
		rectList = new ArrayList<Rectangle>();
		
		for (int xAxis=0;xAxis<map.getWidth(); xAxis++){
             for (int yAxis=0;yAxis<map.getHeight(); yAxis++){
                 int tileID = map.getTileId(xAxis, yAxis, 0);
                 String value = map.getTileProperty(tileID, "blocked", "false");
                 if (value.equals("true")){
                     rectList.add(new Rectangle(xAxis * blockSize, yAxis * blockSize,blockSize,blockSize));
                 }
             }
         }
		rects = new Rectangle[rectList.size()];
		rects = rectList.toArray(rects);
		
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
				if (!isBlocked(x, y + delta * 0.1f)){
					y+=0.1 * delta;
					timer(0,0,delta);
				}
			}else if(input.isKeyDown(Input.KEY_A) && x > screenLeftEdge){
				if (!isBlocked(x - delta * 0.1f, y)){
					x-=0.1 * delta;
					timer(0,2,delta);
				}
			}else if(input.isKeyDown(Input.KEY_D) && x < screenRightEdge){
				if (!isBlocked(x + delta * 0.1f, y)){
					x+=0.1 * delta;
					timer(0,3,delta);
				}
			}
	}

	
	//Rendering
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		//Drawing sprites
		map.render(0,0);
		player.draw(x, y);
		
		//Drawing rectangles
		for(int i = 0;i < rects.length;i++){
			g.draw(rects[i]);
		}
		g.draw(playerBoundingRect);
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
        boolean blocked = false;
        
        playerBoundingRect.setLocation(x,y + hitBox);
		for(int i = 0; i < rects.length; i++){
			if(playerBoundingRect.intersects(rects[i])){
				blocked = true;
			}
		}
		return blocked;
    }
	
	//Running the game.
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Main("RPG Game"));
			appgc.setDisplayMode(640, 480, false);
			appgc.setTargetFrameRate(FPS); 
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}