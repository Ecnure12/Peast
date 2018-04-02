package UI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import Shape.*;

public class InstantGraph implements Serializable {

	private static final long serialVersionUID = -1776292504828741371L;

	public static int TYPE_NORMAL = 0;
	public static int TYPE_CONSTRUCTION = 1;

	private int type = 0;

	private String name = "";

	private LinkedList<Phenomenon> phenomenons = new LinkedList<Phenomenon>();// 存放与该问题域有关的现象
	private LinkedList<String> constraint = new LinkedList<String>();// 存放此时钟图的约束语句
	private LinkedList<Jiaohu> jiaohu = new LinkedList<Jiaohu>(); // 保存交互
	private Hashtable<Integer, String> weight = new Hashtable<Integer, String>();

	private Rect domain;// 要绘制时钟图的问题域
	private Clock clock = new Clock();
	private Diagram problemDiagram;// 问题图

	int originX;// 坐标轴原点X坐标
	int originY;// 坐标轴原点Y坐标

	int length;// 坐标轴长度

	public InstantGraph(Rect domain, Clock clock) {

		originX = 20;
		originY = 60;
		length = 800;

		this.clock = clock;
		this.domain = domain;
		this.problemDiagram = Main.win.myProblemDiagram;

		if (problemDiagram == null)
			return;

		LinkedList temp = problemDiagram.getPhenomenon();
		int state;

		this.setName(domain.getShortName());

		// 筛选出所有与该问题域相关的现象
		for (int i = 0; i <= temp.size() - 1; i++) {
			state = 0;
			Phenomenon temp_p = (Phenomenon) temp.get(i);
			if (temp_p.getFrom().getShortName().equals(domain.getShortName())
					|| temp_p.getTo().getShortName()
							.equals(domain.getShortName())) {
				phenomenons.add(temp_p);
				/*
				 * if (temp_p.getRequirement() != null) state = 1;
				 * 
				 * Jiaohu tempJh = new TriangleJiaohu(originX + jiaohu.size() *
				 * 50 + 30, originY, temp_p.getBiaohao(), state);
				 * 
				 * jiaohu.add(tempJh);
				 */
			}
		}
		setOrder();
		fixIteractions(this.phenomenons);

		int interactions_size = phenomenons.size();

		int interval = length / (interactions_size + 1);

		for (int m = 0; m < interactions_size; m++) {

			state = 0;
			Phenomenon temp_p2 = phenomenons.get(m);

			if (temp_p2.getRequirement() != null)
				state = 1;

			Jiaohu tempJh = new TriangleJiaohu(originX + (jiaohu.size() + 1)
					* interval, originY, temp_p2.getBiaohao(), state);

			jiaohu.add(tempJh);
		}
	}

	public InstantGraph(int intCount, String m_name, Clock clock) {

		originX = 20;
		originY = 60;
		length = 800;

		this.clock = clock;
		this.problemDiagram = Main.win.myProblemDiagram;
		this.name = m_name.trim();

		if (problemDiagram == null)
			return;

		int interval = length / (intCount + 1);

		for (int i = 0; i < intCount; i++) {
			Jiaohu tempJh = new TriangleJiaohu(originX + (jiaohu.size() + 1)
					* interval, originY, i, 0);
			jiaohu.add(tempJh);
		}

	}

	public void setType(int m_type) {
		this.type = m_type;
		for (int i = 0; i < this.jiaohu.size(); i++) {
			String jName = this.getName().length() < 3 ? this.getName() : this
					.getName().substring(0, 3);
			this.jiaohu.get(i).setName(jName);
		}
	}

	public int getType() {
		return this.type;
	}

	public void setName(String m_name) {
		this.name = m_name.trim();
	}

	public String getName() {
		return this.name;
	}

	private void fixIteractions(LinkedList<Phenomenon> interactions2) {

		int first_unsorted;
		int position;
		Phenomenon current;
		int interactions2_size = interactions2.size();

		for (first_unsorted = 1; first_unsorted < interactions2_size; first_unsorted++) {
			if (this.getValue(interactions2.get(first_unsorted).getBiaohao()) < this
					.getValue(interactions2.get(first_unsorted - 1)
							.getBiaohao())) {
				position = first_unsorted;
				current = interactions2.get(first_unsorted);
				do {
					position--;
				} while (position > 0
						&& this.getValue(interactions2.get(position)
								.getBiaohao()) > this.getValue(current
								.getBiaohao()));
				interactions2.add(position, current);
				interactions2.remove(first_unsorted + 1);
			}
		}
	}

	private double getValue(int key) {

		String weight_str = this.weight.get(key);

		double m_weight = Double.valueOf(weight_str.substring(weight_str
				.indexOf(',') + 1));

		return m_weight;
	}

	public void setPosition(int x, int y) {
		this.originX = x;// 坐标轴原点X坐标
		this.originY = y;// 坐标轴原点Y坐标
		for (int i = 0; i < jiaohu.size(); i++) {
			Jiaohu temp = jiaohu.get(i);
			jiaohu.get(i).moveTo(0, originY - temp.getMiddleY());
		}
	}

	public Point getOrigin() {
		return new Point(originX, originY);
	}

	public LinkedList<Phenomenon> getInts() {
		return phenomenons;
	}

	public LinkedList<Jiaohu> getJiaohu() {
		return this.jiaohu;
	}

	public void addConstraint(String con) {
		constraint.add(con);
	}

	public Rect getDomain() {
		return this.domain;
	}

	public Clock getClock() {
		return this.clock;
	}

	public void setClock(Clock mClock) {
		this.clock = mClock;
	}

	public Hashtable<Integer, String> getOrder() {
		return this.weight;
	}

	public boolean contains(Jiaohu p_jiaohu) {

		boolean isHave = false;
		int jiaohu_size = this.jiaohu.size();

		for (int i = 0; i < jiaohu_size; i++) {

			// if (p_jiaohu.getNumber() == jiaohu.get(i).getNumber()) {
			if (p_jiaohu.equals(jiaohu.get(i))) {

				isHave = true;

				break;
			}
		}

		return isHave;
	}

	public void refresh() {
		Collections.sort(this.getJiaohu());
	}

	public void draw(Graphics g) {
		// 画图的标识
		g.drawString("C", originX - 5, originY - 10);
		Font font1 = new Font("SansSerif", 0, 9);
		Font tmp = g.getFont();
		g.setFont(font1);
		g.drawString(this.getName(), originX + 3, originY - 8);
		g.setFont(tmp);
		g.drawLine(originX + 20, originY - 10, originX + length, originY - 10);
		g.fillPolygon(new int[] { originX + length, originX + length - 10,
				originX + length - 10 }, new int[] { originY - 10,
				originY - 15, originY - 5 }, 3);
		// 画交互
		for (int i = 0; i < jiaohu.size(); i++) {
			Jiaohu jh = (Jiaohu) this.jiaohu.get(i);
			jh.draw(g);
		}
		// 画单位
		g.drawString(clock.getUnit(), originX + length, originY + 5);
	}

	public Jiaohu whichSelected(int x, int y) {
		for (int i = 0; i <= this.jiaohu.size() - 1; i++) {
			Jiaohu tmp_jh = (Jiaohu) this.jiaohu.get(i);
			if (tmp_jh.isIn(x, y)) {
				return tmp_jh;
			}
		}
		return null;
	}

	/**
	 * 得到该交互图中所有state为0或者1的场景 调用improve函数对这些场景进行
	 * */
	private void setOrder() {
		IntDiagram myIntDiagram = whichDiagram();

		if (myIntDiagram == null)
			return;
		LinkedList m_changjing = myIntDiagram.getChangjing();
		int m_changjing_size = m_changjing.size();

		LinkedList<Changjing> new_changjing = new LinkedList<Changjing>();
		for (int i = 0; i < m_changjing_size; i++) {

			Changjing temp_changjing = (Changjing) m_changjing.get(i);

			if (temp_changjing.getState() == 0
					|| temp_changjing.getState() == 1) {

				new_changjing.add(temp_changjing);
			}
		}

		improve(new_changjing);

	}

	private void improve(LinkedList<Changjing> newChangjing) {

		LinkedList<Changjing> changjing = newChangjing;

		int count = getCount(newChangjing);

		int N = 0;

		while (this.weight.size() != count) {

			LinkedList<Changjing> hasWeight = new LinkedList<Changjing>();
			int changjing_size = changjing.size();
			for (int i = 0; i < changjing_size; i++) {

				Changjing current = changjing.get(i);

				Jiaohu from = current.getFrom();
				Jiaohu to = current.getTo();

				String value = String.valueOf(N) + ",";

				if (i == 0) {
					weight.put(from.getNumber(), value + String.valueOf(0.0));

					if (current.getState() == 1) {
						weight.put(to.getNumber(), value + String.valueOf(1.0));
					} else if (current.getState() == 1) {
						weight.put(to.getNumber(), value + String.valueOf(0.5));
					}

					hasWeight.add(current);

					continue;
				}

				if ((weight.get(from.getNumber()) == null && weight.get(to
						.getNumber()) == null)
						|| (weight.get(from.getNumber()) != null && weight
								.get(to.getNumber()) != null)) {

					continue;

				} else if (weight.get(from.getNumber()) != null
						&& weight.get(to.getNumber()) == null) {

					double w = this.getValue(from.getNumber());

					if (current.getState() == 1) {
						weight.put(to.getNumber(),
								value + String.valueOf(w + 1));
					} else if (current.getState() == 0) {
						weight.put(to.getNumber(),
								value + String.valueOf(w + 0.5));
					}

					hasWeight.add(current);

				} else if (weight.get(from.getNumber()) == null
						&& weight.get(to.getNumber()) != null) {

					double w = Double.valueOf(to.getNumber());

					if (current.getState() == 1) {
						weight.put(from.getNumber(),
								value + String.valueOf(w - 1));
					} else if (current.getState() == 0) {
						weight.put(from.getNumber(),
								value + String.valueOf(w - 0.5));
					}

					hasWeight.add(current);
				}
			}

			int hasWeight_size = hasWeight.size();

			for (int k = 0; k < hasWeight_size; k++) {
				changjing.remove(hasWeight.get(k));
			}

			N++;
		}
	}

	/**
	 * 参数：场景链表 返回值：共包含的不重复的交互
	 * */
	private int getCount(LinkedList<Changjing> newChangjing) {

		LinkedList<Jiaohu> jiaohu = new LinkedList<Jiaohu>();

		/*
		 * 这个函数可以改为集合 Set<Jiaohu> jiaohus=new HashSet();
		 */

		int newChangjing_size = newChangjing.size();

		for (int i = 0; i < newChangjing_size; i++) {

			Jiaohu from = newChangjing.get(i).getFrom();
			Jiaohu to = newChangjing.get(i).getTo();

			if (!jiaohu.contains(from)) {
				jiaohu.add(from);
			}
			if (!jiaohu.contains(to)) {
				jiaohu.add(to);
			}
		}

		return jiaohu.size();
	}

	/**
	 * 得到与该问题域相关的情境图
	 * */
	private IntDiagram whichDiagram() {

		LinkedList<IntDiagram> temp = Main.win.myIntDiagram;
		int temp_size = temp.size();

		for (int i = 0; i < temp_size; i++) {

			@SuppressWarnings("unchecked")
			LinkedList<Jiaohu> jiaohu = temp.get(i).getJiaohu();
			int jiaohu_size = jiaohu.size();

			for (int m = 0; m < jiaohu_size; m++) {

				Jiaohu temp_jiaohu = (Jiaohu) jiaohu.get(m);

				if (temp_jiaohu.getNumber() == this.phenomenons.get(0)
						.getBiaohao()) {
					return temp.get(i);
				}
			}
		}
		return null;
	}
}
