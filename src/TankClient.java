import java.awt.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

//��ܼ�  ���й���  
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
	Image offScreenImage = null;//�������Ļ  �Ȼ����ڴ���  ����ͼƬ��
	
	//��̹��   paint  
	@Override
	public void paint(Graphics g) {
		g.drawString("Missile count:"+ missiles.size(), 10, 50);
		g.drawString("explode count:"+ explodes.size(), 10, 70);
		g.drawString("Tank count:"+ tanks.size(), 10, 90);
		g.drawString("MyTank life:"+ myTank.getLife(), 10, 110);
		
		//�����������¼������
		if(tanks.size()<=0) {
			for(int i =0;i<5;i++) {
				tanks.add(new Tank(50 + 40*(i+1),50,false,Direction.D, this));
			}
		}
		
		for(int i =0; i<missiles.size();i++) {
			Missile m = missiles.get(i);
			//1����
			//��Ϊ�����еĶ���һ��
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
		//2��ײ 
		//��Ϊײ���е���̹��
		//����bug  �ж��Լ���̹��Ҳ�ǻ��   
		myTank.tankHitAllTank(tanks);
		//tank
		//3����  ��Ϊ������
		for(int i =0; i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);//�����Ҫ�ǽ������ڲ���̹����ײ
			t.draw(g);
		}
		
		
		myTank.draw(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		b.draw(g);
	}
	
	
	//paint ��֧����
	//��ִ��  update  ��ִ�� paint
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		//����ͼƬ��
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
		gOffScreen.setColor(c);
		//������Ĳ�����̹��
		paint(gOffScreen);
		//һ���ԵĽ������ϻ���
		g.drawImage(offScreenImage,0,0,null);
	}



	//���ش���
	private void lauchFrame() {
		for(int i =0;i<10;i++) {
			tanks.add(new Tank(50 + 40*(i+1),50,false,Direction.D, this));
		}
		this.setLocation(400,300);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		//�����ڲ���   ������������ϵ���� �������ǿ   
		//�����¼�   �ر��¼�
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//���ñ���ɫ
		this.setBackground(Color.GREEN);
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		setVisible(true);//����paint
		new Thread(new PaintThread()).run();//�ֿ���һ���߳�
	}
		
	public static void main(String[] args) {//һ���߳�
		TankClient tc = new TankClient();
		tc.lauchFrame();//��������һ���߳�
		
	}
	//�ڲ���  ӦΪֻΪ̹�˶�ʹ�ò���Ҫд���ⲿ��
	private class PaintThread implements Runnable{
		
		@Override
		public void run() {
			while(true) {
				repaint();  //����paint
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} 
	}
	
	//���̼�����  ӦΪֻΪ̹�˶�ʹ�ò���Ҫд���ⲿ��
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
