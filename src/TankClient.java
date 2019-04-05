import java.awt.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

//大管家  进行管理  
public class TankClient extends Frame {
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	Tank myTank = new Tank(700,500,true,Direction.STOP,this);

	Wall w1 =new Wall(100,200,20,150,this), w2 =new Wall(300,100,300,20,this);
	List<Explode> explodes = new ArrayList<Explode>();
	List<Missile> missiles = new ArrayList<Missile>();
	List<Tank> tanks = new ArrayList<Tank>();
	Missile m = null;
	Blood b = new Blood();
	Image offScreenImage = null;//背面的屏幕  先画到内存里  虚拟图片里
	
	//画坦克   paint  
	@Override
	public void paint(Graphics g) {
		g.drawString("Missile count:"+ missiles.size(), 10, 50);
		g.drawString("explode count:"+ explodes.size(), 10, 70);
		g.drawString("Tank count:"+ tanks.size(), 10, 90);
		g.drawString("MyTank life:"+ myTank.getLife(), 10, 110);
		
		//敌人死后重新加入敌人
		if(tanks.size()<=0) {
			for(int i =0;i<5;i++) {
				tanks.add(new Tank(50 + 40*(i+1),50,false,Direction.D, this));
			}
		}
		
		for(int i =0; i<missiles.size();i++) {
			Missile m = missiles.get(i);
			//1不打
			//改为把所有的都打一遍
			m.hitTank(myTank);
			m.hitTanks(tanks);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
	
		for(int i =0; i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		//2不撞 
		//改为撞所有敌人坦克
		//修正bug  判断自己的坦克也是活的   
		myTank.tankHitAllTank(tanks);
		//tank
		//3不画  改为画所有
		for(int i =0; i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);//这个主要是将敌人内部的坦克碰撞
			t.draw(g);
		}
		
		
		myTank.draw(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}
	
	
	//paint 那支画笔
	//先执行  update  再执行 paint
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		//画到图片上
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreen.setColor(c);
		//给后面的布画上坦克
		paint(gOffScreen);
		//一次性的将布画上画上
		g.drawImage(offScreenImage,0,0,null);
	}



	//加载窗体
	private void lauchFrame() {
		for(int i =0;i<10;i++) {
			tanks.add(new Tank(50 + 40*(i+1),50,false,Direction.D, this));
		}
		this.setLocation(400,300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		//匿名内部类   与其他东西关系不大 ，耦合性强   
		//增加事件   关闭事件
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//设置背景色
		this.setBackground(Color.GREEN);
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);//调用paint
		new Thread(new PaintThread()).run();//又开了一个线程
	}
		
	public static void main(String[] args) {//一个线程
		TankClient tc = new TankClient();
		tc.lauchFrame();//单独开了一个线程
		
	}
	//内部类  应为只为坦克动使用不需要写成外部类
	private class PaintThread implements Runnable{
		
		@Override
		public void run() {
			while(true) {
				repaint();  //调用paint
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
	//键盘监听类  应为只为坦克动使用不需要写成外部类
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
	}
}
