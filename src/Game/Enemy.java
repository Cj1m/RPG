package Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Enemy {
	int ID;
	float x;
	float y;
	
	int width;
	int height;

	int hitBox;
	
	float speed;
	
	boolean moveLeft = false;
	boolean moveRight = true;
	
	Rectangle enemyBoundingRect;
	Rectangle[] rects;
	
	public Enemy(int ID, float x, float y, Rectangle[] rects){
		this.ID = ID;
		this.x = x;
		this.y = y;
		
		this.width = 20;
		this.height = 20;
		this.hitBox = width / 2;
		
		speed = 1.5f;
		
		this.rects = rects;
		
		enemyBoundingRect = new Rectangle(this.x- width, this.y - height, width, hitBox);
	}
	
	public void render(Graphics g, Rectangle[] rects){
		this.rects = rects;
		g.draw(enemyBoundingRect);
	}
	
	public void move(int delta, float mapX, float mapY){
		if(moveLeft){
			x -= speed;
		}
		if(moveRight){
			x += speed;
		}
		
		if(isBlocked(x - mapX - speed, y - mapY) && !isBlocked(x - mapX + speed, y - mapY)){
			moveRight = true;
			moveLeft = false;
		}
		if(!isBlocked(x - mapX - speed, y - mapY) && isBlocked(x - mapX + speed, y - mapY)){
			moveLeft = true;
			moveRight = false;
		}
	}
	
	private boolean isBlocked(float x, float y){
		 boolean blocked = false;
	        
	        enemyBoundingRect.setLocation(x + (width - 5) / 2,y + hitBox - 2);
			for(int i = 0; i < rects.length; i++){
				if(enemyBoundingRect.intersects(rects[i])){
					blocked = true;
				}
			}
			return blocked;
			
	}
	
	public void death(){
		System.out.println("Killed Enemy" + ID);
	}
	
}
