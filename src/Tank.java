import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
/**
 * ��������
 * �����Ƿ�Ҫ���ʲô����
 * ���ǹ��췽��
 * @author ����
 *
 */
public class Tank {
	public static final int SPEEDX = 5;
	public static final int SPEEDY = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private boolean live =true;
	private int life =100;
	private BloodBar bb = new BloodBar();
	private int x , y;
	private int oldX,oldY;
	private  boolean good;
	TankClient tc;
	private static Random r = new Random();
	//���ĸ�������
	private boolean bL = false,bU = false ,bD = false, bR = false;

	private Direction dir =Direction.STOP;
	private Direction ptDir = Direction.D;
	
	
	private int step = r.nextInt(12) + 3;
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public boolean isGood() {
		return good;
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.oldX =x;
		this.oldY = y;
		this.good =good;
	}
	public Tank(int x, int y,boolean good,Direction dir,TankClient tc) {
		this(x,y,good);
		this.tc =tc;
		this.dir = dir;
	}
	//̹�������Լ�
	public void draw(Graphics g) {
		if(!live) {
			if(!live) {
				if(!good) {
					tc.tanks.remove(this);
				}
				return;
			}
		}
		Color c =g.getColor();  //��ȡ���ʵ���ɫ
		if(good) g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fillOval(x, y, WIDTH, HEIGHT);  
		g.setColor(c);//���û��ʱ�������ɫ
		if(good) {
			bb.draw(g);
		}
		move();
		switch(ptDir) {
		case L:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y +Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+Tank.WIDTH/2, y );
			break;
		case UR:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+Tank.WIDTH, y );
			break;
		case R:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+Tank.WIDTH, y +Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+Tank.WIDTH, y +Tank.HEIGHT);
			break;
		case D:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x+Tank.WIDTH/2, y +Tank.HEIGHT);
			break;
		case DL:
			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y +Tank.HEIGHT);
			break;
		}
	}
	void move() {
		this.oldX =x;
		this.oldY = y;
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
		case STOP:
			break;
		default:
			break;
		}
		if(this.dir != Direction.STOP) {
			this.ptDir =this.dir;
		}
		if(x<0) x = 0;
		if(y<30) y = 30;
		if(x+ Tank.WIDTH >TankClient.GAME_WIDTH)  x = TankClient.GAME_WIDTH -Tank.WIDTH;
		if(y+ Tank.HEIGHT >TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT -Tank.HEIGHT;
		if(!good) {
			Direction[] dirs =Direction.values();
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn =r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(40)>38)
			this.fire();
			
		}
		
	}
	//̹�˱��������µķ�Ӧ
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			
			case KeyEvent.VK_LEFT:
				bL = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break;
			default:
				break;
		}
		locateDirection();
	}
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_CONTROL:
				fire();
				break;
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;
			case KeyEvent.VK_A:
				superFire();
				break;
			case KeyEvent.VK_F2:
				addMyTank();
				break;
			default:
				break;
		}
		locateDirection();
	}
	void locateDirection() {
		if(bL&& !bU && !bR && !bD)  dir =Direction.L;
		else if(bL&& bU && !bR && !bD)  dir =Direction.LU;
		else if(!bL&& bU && !bR && !bD)  dir =Direction.U;
		else if(!bL&& bU && bR && !bD)  dir =Direction.UR;
		else if(!bL&& !bU && bR && !bD)  dir =Direction.R;
		else if(!bL&& !bU && bR && bD)  dir =Direction.RD;
		else if(!bL&& !bU && !bR && bD)  dir =Direction.D;
		else if(bL&& !bU && !bR && bD)  dir =Direction.DL;
		else if(!bL&& !bU && !bR && !bD)  dir =Direction.STOP;
	}
	
	public void fire() {
		if(!live) return;
		int x = this.x + Tank.WIDTH/2 -Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 -Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ptDir,this.tc);
		tc.missiles.add(m);
	}
	public void fire(Direction dir) {
		if(!live) return;
		int x = this.x + Tank.WIDTH/2 -Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 -Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,HEIGHT,WIDTH);
	}
	//ֻ���Լ���̹�˵����������
	public boolean tankHitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()) && t.isLive()&& this.live) {
			t.setLive(false);
			this.live = false;
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	//ֻ���Լ���̹�˵����������
	public boolean tankHitAllTank(List<Tank> tanks) {
		
		for(int i =0;i<tanks.size();i++) {
			if(tankHitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	public boolean collidesWithWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i =0;i<tanks.size();i++) {
			Tank t =tanks.get(i);
			if(this != t) {
				if(this.live && t.isLive()&& this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	private void superFire() {
		Direction[] dirs =Direction.values();
		for(int i = 0; i<8; i++) {
			fire(dirs[i]);
		}
	}
	private void stay() {
		x = oldX;
		y = oldY;
	}
	private class BloodBar{
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x,y-10,WIDTH,10);//������
			int w = WIDTH * life/100;
			g.fillRect(x, y-10, w, 10);//���
			g.setColor(c);
		}
	}
	
	public boolean eat(Blood b) {
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			this.life = 100;
			b.setLive(false);
			return true;
		}
		return false;
	}
	public void addMyTank() {
		if(this.isLive())return;
		this.setLive(true);
		this.setLife(100);
		this.x = 700;
		this.y = 500;
	
	}
}
