import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;




//子弹
public class Missile {
	int x , y;
	Direction dir;
	public static final int SPEEDX = 10;
	public static final int SPEEDY = 10;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private boolean bLive = true;
	private boolean good;
	
	TankClient tc;
	
	public boolean isbLive() {
		return bLive;
	}

	

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this.x = x;
		this.y = y;
		this.good =good;
		this.dir = dir;
		this.tc = tc;
	}



	public void draw(Graphics g) {
		if(!this.bLive) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);//10大小
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch(dir) {
		case L:
			x -= SPEEDX;
			break;
		case LU:
			x -= SPEEDX/2;
			y -= SPEEDY/2;
			break;
		case U:
			y -= SPEEDY;
			break;
		case UR:
			x += SPEEDX/2;
			y -= SPEEDY/2;
			break;
		case R:
			x += SPEEDX;
			break;
		case RD:
			x += SPEEDX/2;
			y += SPEEDY/2;
			break;
		case D:
			y += SPEEDY;
			break;
		case DL:
			x -= SPEEDX/2;
			y += SPEEDY/2;
			break;
		}
		if(x<0 || y<0 || x > TankClient.GAME_WIDTH|| y > TankClient.GAME_HEIGHT ) {
			bLive =false;
		}
		
	}	
	
	public Rectangle getRect() {
		return new Rectangle(x,y,HEIGHT,WIDTH);
	}
	//子弹打坦克
	public boolean hitTank(Tank t) {
		if(this.bLive &&this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			if(t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife() <= 0) {
					t.setLive(false);
				}
			}else {
				t.setLive(false);
			}
			this.bLive = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i =0;i<tanks.size();i++) {
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.bLive && this.getRect().intersects(w.getRect())){
			this.bLive = false;
			return true;
		}
		return false;
	}
}
