package Game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Main extends BasicGame{
	 //General
	static int FPS = 60;
	int SCREENCENTERX;
	int SCREENCENTERY;
	
	//Player
	SpriteSheet playerSprites;
	Rectangle playerBoundingRect;
	Image player;
	Input input;
	int walkSpeed = 250;
	int playerWidth;
	int playerHeight;
	int hitBox = 24;
	float health;
	
	//Enemy
	Enemy bob;
	
	//Map
	TiledMap map;
	float mapX,mapY;
	
	
	//For the timer 
	int counter;
	
	//Collision
	ArrayList<Rectangle> rectList;
	Rectangle[] rects;
	int blockSize = 32;
	
	
	public Main(String gamename)
	{
		super(gamename);
	}

	//On startup method
	@Override
	public void init(GameContainer gc) throws SlickException {
		//General
		SCREENCENTERX = gc.getWidth() / 2;
		SCREENCENTERY = gc.getHeight() / 2;
		
		//Player
		playerWidth = 20;
		playerHeight = 40;
		
		playerSprites = new SpriteSheet("gfx/Sprites.png", 32, 48);
		player = playerSprites.getSprite(0,0);
		playerBoundingRect = new Rectangle(SCREENCENTERX - playerWidth, SCREENCENTERY - playerHeight, playerWidth, hitBox);
		
		health = 100;
		
		mapX = -150;
		mapY = -50;
		
		
		
		//Map
		getBlocks();
		
		//Enemy
		bob = new Enemy(00, 200, 70, rects);
	}

	//Updating
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		input = gc.getInput();
			if(input.isKeyDown(Input.KEY_W)){
				if(!isBlocked(SCREENCENTERX, SCREENCENTERY - delta * 0.1f)){
					mapY-=0.1 * delta;
					timer(0,1,delta);
				}
			}else if(input.isKeyDown(Input.KEY_S)){
				if (!isBlocked(SCREENCENTERX, SCREENCENTERY + delta * 0.1f)){
					mapY+=0.1 * delta;
					timer(0,0,delta);
				}
			}else if(input.isKeyDown(Input.KEY_A)){
				if (!isBlocked(SCREENCENTERX - delta * 0.1f, SCREENCENTERY)){
					mapX-=0.1 * delta;
					timer(0,2,delta);
				}
			}else if(input.isKeyDown(Input.KEY_D)){
				if (!isBlocked(SCREENCENTERX + delta * 0.1f, SCREENCENTERY)){
					mapX+=0.1 * delta;
					timer(0,3,delta);
				}
			}
			bob.move(mapX, mapY);
			
			if(playerBoundingRect.intersects(bob.enemyBoundingRect)){
				health -= 0.1f;
			}
			getBlocks();
	}

	
	//Rendering
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException{
		//Map
		map.render((int)-mapX,(int)-mapY);
		
		//Enemy(s)
		bob.render(g, rects);
		
		//Player
		player.draw(SCREENCENTERX, SCREENCENTERY);
		g.drawString("Health: "+ Math.round(health), 10, 30);
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
	
	
	public void getBlocks(){
		try {
			map = new TiledMap("gfx/map.tmx");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		rectList = new ArrayList<Rectangle>();
		
		for (int xAxis=0;xAxis<map.getWidth(); xAxis++){
             for (int yAxis=0;yAxis<map.getHeight(); yAxis++){
                 int tileID = map.getTileId(xAxis, yAxis, 0);
                 String value = map.getTileProperty(tileID, "blocked", "false");
                 if (value.equals("true")){
                     rectList.add(new Rectangle(xAxis * blockSize - mapX, yAxis * blockSize - mapY,blockSize,blockSize));
                 }
             }
         }
		rects = new Rectangle[rectList.size()];
		rects = rectList.toArray(rects);
	}
	
	//More collision
	private boolean isBlocked(float x, float y) {
        boolean blocked = false;
        
        playerBoundingRect.setLocation(x + (playerWidth - 8) / 2,y + hitBox - 2);
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
			appgc.setDisplayMode(800, 600, false);
			appgc.setTargetFrameRate(FPS); 
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}