import java.awt.*;

public class Explode {

	int x ,y;
	private boolean live =true;
	private TankClient tc ;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	int [] diameter = {4, 5,6,7,8,9,10,11, 12,13,14,15,16,17, 18,19,20,21,22,23,24,25, 26,27,28,29,30,28,26,24,20,10,9,8,7, 6};
	int step =0;
	public void draw(Graphics g) {
		if(!live) {
			tc.explodes.remove(this);
			return;}
		if(step ==diameter.length) {
			live =false;
			step = 0;
			return;
		}
		Color c =g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		step++;
	}
}
