package Shape;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Hashtable;
import java.util.LinkedList;

import UI.Clock;
import UI.Main;

public class ClockDiagram extends Diagram {

	String title;
	public Hashtable<Rect, String> members = new Hashtable<Rect, String>();
	private LinkedList<Clock> clocks=new LinkedList<Clock>();

	public ClockDiagram(Diagram t) {
		super("ClockDiagram");

		this.title = "ClockDiagram";

		this.components = new LinkedList();

		if (t.getTitle().equals("ContextDiagram")) {

			for (int i = 0; i <= t.components.size() - 1; i++) {
				Shape tmp_s = (Shape) t.components.get(i);
				this.components.add(tmp_s);
			}
		}
		
		//this.clocks.add(new Clock());
	}

	public void draw(Graphics g) {
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);

			if (tmp.shape == 2) {
				Line tmpLine = (Line) tmp;

				if (tmpLine.selected) {
					g.setColor(Color.red);
				}

				String biao = tmpLine.name;
				g.drawString(biao, (tmpLine.x1 + tmpLine.x2) / 2,
						(tmpLine.y1 + tmpLine.y2) / 2);

				g.setColor(Color.black);
				tmp.draw(g);
			}
			if (tmp.shape == 0) {
				Rect tmpRect = (Rect) tmp;

				tmp.draw(g);

				if (tmpRect.selected) {
					g.setColor(Color.red);
				}

				Clock tempClock = this.getClock(tmpRect);
				if (tempClock != null) {
					String clockName="CLK:"+tempClock.getName();
					g.drawString(clockName, tmpRect.x2 + tmpRect.x1-clockName.length()*2,
							tmpRect.y1 - 15);
				}
				drawClock(g, tmpRect.x2 + tmpRect.x1, tmpRect.y1);
			}
		}
	}

	// ��ʱ�ӱ���
	// middleX:��������λ�õ�x����
	// middleY:��������λ�õ�y����
	public void drawClock(Graphics g, int middleX, int middleY) {
		int radius = 10;// ���̵İ뾶
		Color temp = g.getColor();// ���浱ǰ���ʵ���ɫ

		g.setColor(Color.white);
		g.fillOval(middleX - radius, middleY - radius, 20, 20);// ʱ����

		g.setColor(temp);
		g.drawOval(middleX - radius, middleY - radius, 20, 20);// ʱ����
		g.drawLine(middleX, middleY - radius, middleX, middleY - radius + 2);// 12��̶�
		g.drawLine(middleX + radius, middleY, middleX + radius - 2, middleY);// 3��̶�
		g.drawLine(middleX, middleY + radius, middleX, middleY + radius - 2);// 6��̶�
		g.drawLine(middleX - radius, middleY, middleX - radius + 2, middleY);// 9��̶�
		g.drawLine(middleX, middleY, middleX - 5, middleY - 5);// ����
		g.drawLine(middleX, middleY, middleX + 3, middleY - 3);// ʱ��

		g.setColor(Color.black);
		
	}

	public void addClock(Clock clock){
		//System.out.println("clock.getName()="+clock.getName());
		if(clock.getName().equals("Default"))
			return;
		
		int size=this.clocks.size();
		int temp=size;
		
		for(int i=0;i<size;i++){
			if(this.clocks.get(i).getName().equals(clock.getName())){
				temp=i;
			}
		}
		
		if(temp!=size){
			this.clocks.remove(temp);
		}
		
		this.clocks.add(clock);
	}
	
	public LinkedList<Clock> getClocks(){
		return this.clocks;
	}
	
	public Clock getClock(String name){
		int size=this.clocks.size();
		
		if(name.equals("Default")){
			return new Clock();
		}
		for(int i=0;i<size;i++){
			if(this.clocks.get(i).getName().equals(name)){
				return this.clocks.get(i);
			}
		}
		
		return null;
	}
	
	public void setClock(Rect rect, Clock clock) {
		if (rect != null && clock != null) {
			this.members.put(rect, clock.getName());
			this.addClock(clock);
		}
	}

	public Clock getClock(Rect rect) {
		String clockName=this.members.get(rect);
		
		if(clockName!=null&&clockName.equals("Default")){
			return new Clock();
		}
		
		for(int i=0;i<this.clocks.size();i++){
			Clock tempClock=clocks.get(i);
			if(tempClock.getName().equals(clockName)){
				return tempClock;
			}
		}
		return null;
	}

	@Override
	public Shape whichSelected(int x, int y) {
		// TODO Auto-generated method stub
		//return super.whichSelected(x, y);
		for (int i = 0; i < this.components.size(); i++) {
			Shape tmp = (Shape) this.components.get(i);
			if (tmp.isIn(x, y)) {
				return tmp;
			}
			else if(tmp.shape==0)
			{
				Rect tmpRect = (Rect) tmp;
				if(Math.pow(x-tmpRect.x2 - tmpRect.x1,2)+Math.pow(y-tmpRect.y1,2)<=100)
					return tmp;
			}
		}
		return null;
	}
	
	
}
