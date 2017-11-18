package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class ConstraintPane extends JPanel {
	/*
	 * ʾ���������ڵ�Pane����ΪBorderLayout this.setLayout(new BorderLayout());
	 * ConstraintPane south=new ConstraintPane(); Ȼ�����Pane�ӵ������·�
	 * this.add(south,BorderLayout.SOUTH); ��ӵ��ú���
	 * south.addConstraint("x1>x2");��������String
	 */
	private ArrayList<JLabel> labellist = new ArrayList<JLabel>();
	private ArrayList<JButton> buttonlist = new ArrayList<JButton>();
	private final int PANEWIDTH = 210;
	private final int PANEHEIGHT = 140;
	JPanel relation = null;

	ConstraintPane() {
		this.setLayout(new BorderLayout());
		relation = new JPanel();
		relation.setPreferredSize(new Dimension(PANEWIDTH, PANEHEIGHT));
		relation.setBackground(Color.white);
		relation.revalidate();

		relation.setLayout(null);
		JScrollPane constraint = new JScrollPane(relation);
		constraint.setPreferredSize(new Dimension(250, 200));
		constraint.setViewportView(relation);
		constraint.setBorder(new TitledBorder("Լ�� :"));

		this.setLayout(new BorderLayout());

		this.add(constraint, BorderLayout.EAST);
	}

	public void addConstraint(String constraint) {
		/*
		 * ���Ӵ����ĺ�����Ҫ�Ӵ����������
		 */

		final JLabel label = new JLabel(constraint);
		int num = labellist.size();
		label.setBounds(25, num * 25 + 10, 150, 20);
		final JButton button = new JButton("DEL");
		// ImageIcon icon = new
		// ImageIcon(Main.class.getResource("/image/delete.png"));
		// button.setIcon(icon);
		button.setBounds(190, num * 25 + 10, 50, 21);
		button.setContentAreaFilled(false); // ����JButton͸��
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				labellist.remove(label);
				buttonlist.remove(button);
				int num = labellist.size();
				if (num > 4) {
					relation.setPreferredSize(new Dimension(PANEWIDTH,
							PANEHEIGHT + (num - 4) * 25));
					relation.revalidate();
				}
				conrepaint();
			}

		});
		if (num > 4) {
			relation.setPreferredSize(new Dimension(PANEWIDTH, PANEHEIGHT
					+ (num - 4) * 25));
			relation.revalidate();
		}
		labellist.add(label);
		buttonlist.add(button);
		relation.add(button);
		relation.add(label);
		this.repaint();
	}

	public void conrepaint() {
		/*
		 * ɾ�������ĺ�����Ҫ�Ӵ����������
		 */

		relation.removeAll();
		for (int i = 0; i < labellist.size(); i++) {
			labellist.get(i).setBounds(25, i * 25 + 10, 150, 20);
			buttonlist.get(i).setBounds(190, i * 25 + 10, 50, 21);
			relation.add(labellist.get(i));
			relation.add(buttonlist.get(i));
		}
		repaint();
	}

}
