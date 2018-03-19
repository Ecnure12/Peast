package UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import Shape.Phenomenon;
import Shape.Rect;

public class ConstraintDialog extends JDialog implements ActionListener {

	String[] character = { "subClock", "alternate" , "boundedDrift_i_j"};
	JComboBox constraint=new JComboBox(character);
	
	JComboBox JiaohuFrom=new JComboBox();
	DefaultComboBoxModel modelFrom = new DefaultComboBoxModel();
	JComboBox JiaohuTo=new JComboBox();
	DefaultComboBoxModel modelTo = new DefaultComboBoxModel();
	LinkedList<InstantGraph> igs;
	
	JTextField number = new JTextField();
	JTextField number2 = new JTextField();
	
	JButton confirm = new JButton("OK");
	
	JLabel background = null;

	public ConstraintDialog(LinkedList<InstantGraph> igs) {
		super(Main.win, true);
		this.igs=igs;
		try {
			jbInit();
			}
		    catch (Exception e) {
		    	e.printStackTrace();
		    	}
	}
	
	private void jbInit() {
		// TODO Auto-generated method stub
		setTitle("ConstraintEditor");
		setSize(new Dimension(500, 240));
		this.setResizable(false);
		ImageIcon img = new ImageIcon(Main.class.getResource("/images/ee.jpg"));
		background = new JLabel(img);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 150);
		final JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);

		constraint.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int selectedIndex=constraint.getSelectedIndex();
				if(selectedIndex == 2){
					contentPane.add(number);
					contentPane.add(number2);
					repaint();
				}
				else if(number!=null){
					contentPane.remove(number);
					contentPane.remove(number2);
					repaint();
				}
			}
		});
		
		JiaohuFrom.setModel(this.modelFrom);
		if(igs!=null){
			for (int i = 0; i < this.igs.size(); i++) {
				LinkedList temp=igs.get(i).getInts();
				int intsSize=temp.size();
				for(int m=0;m<intsSize;m++){
					Phenomenon temp_p = (Phenomenon) temp.get(m);
					this.modelFrom.addElement("int"+temp_p.getBiaohao());
				}
			}
		}
		
		JiaohuTo.setModel(this.modelTo);
		if(igs!=null){
			for (int i = 0; i < this.igs.size(); i++) {
				LinkedList temp=igs.get(i).getInts();
				int intsSize=temp.size();
				for(int m=0;m<intsSize;m++){
					Phenomenon temp_p = (Phenomenon) temp.get(m);
					this.modelTo.addElement("int"+temp_p.getBiaohao());
				}
			}
		}

		JiaohuFrom.setBounds(50, 60, 100, 20);
		constraint.setBounds(160, 60, 200, 20);
		JiaohuTo.setBounds(370, 60, 100, 20);
		number.setBounds(210,100,30,20);
		number2.setBounds(245,100,30,20);
		
		confirm.setBounds(210, 140, 80, 25);
		confirm.addActionListener(this);

		contentPane.add(JiaohuFrom);
		contentPane.add(constraint);
		contentPane.add(JiaohuTo);
		contentPane.add(confirm);

		this.setContentPane(contentPane);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	//get the postfix number of an "int" string
	public static int getStringNum(String str){
		StringBuilder stringBuilder = new StringBuilder("");
		for(int i = 0;i < str.length();i++){
			if(str.charAt(i) <= '9' && str.charAt(i) >= '0') stringBuilder.append(str.substring(i, i+1));
		}
		return Integer.parseInt(stringBuilder.toString());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("OK")) {
			String from=JiaohuFrom.getSelectedItem().toString();
			String to=JiaohuTo.getSelectedItem().toString();
			String cons=constraint.getSelectedItem().toString();
			String num="";

			if(constraint.getSelectedIndex() == 0){
				Main.win.instantPane.ClockRelations.add(0);
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(from)){
							Main.win.instantPane.froms.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(to)){
							Main.win.instantPane.tos.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				Main.win.instantPane.repaint();
			}

			else if(constraint.getSelectedIndex() == 1){
				Main.win.instantPane.ClockRelations.add(1);
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(from)){
							Main.win.instantPane.froms.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(to)){
							Main.win.instantPane.tos.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				Main.win.instantPane.repaint();
			}

			else{
				if(number.getText().trim().equals("") || number2.getText().trim().equals("")){
					JOptionPane.showMessageDialog(null, "Please input i and j", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				num=" "+number.getText()+" "+number2.getText();
				Main.win.instantPane.ClockRelations.add(2);
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(from)){
							Main.win.instantPane.froms.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				for(int i = 0;i < igs.size();i++){
					for(int j = 0;j < igs.get(i).getJiaohu().size();j++){
						if(igs.get(i).getJiaohu().get(j).getNumber() == getStringNum(to)){
							Main.win.instantPane.tos.add(igs.get(i).getJiaohu().get(j));
						}
					}
				}
				Main.win.instantPane.repaint();
			}
			//Main.win.instantPane.south.addConstraint(from+cons+to+num);
			Main.win.instantPane.addConstraint(from,cons,to,num);
			dispose();
		}
	}
	
	public static void main(String[]arg0){
		new ConstraintDialog(null);
	}

}
