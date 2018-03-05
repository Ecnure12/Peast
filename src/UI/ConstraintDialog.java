package UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import Shape.Phenomenon;
import Shape.Rect;

public class ConstraintDialog extends JDialog implements ActionListener {


	String[] character = { "£¼", "¡Ü" , "£½" , "£­"};
	JComboBox constraint=new JComboBox(character);
	
	JComboBox JiaohuFrom=new JComboBox();
	DefaultComboBoxModel modelFrom = new DefaultComboBoxModel();
	JComboBox JiaohuTo=new JComboBox();
	DefaultComboBoxModel modelTo = new DefaultComboBoxModel();
	LinkedList<InstantGraph> igs;
	
	JTextField number = new JTextField();
	
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
		setSize(new Dimension(300, 180));
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
				if(selectedIndex==3){
					contentPane.add(number);
					repaint();
				}
				else if(number!=null){
					contentPane.remove(number);
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

		JiaohuFrom.setBounds(10, 20, 100, 20);
		constraint.setBounds(120, 20, 60, 20);
		JiaohuTo.setBounds(190, 20, 100, 20);
		number.setBounds(300,20,60,20);
		
		confirm.setBounds(90, 80, 80, 25);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("OK")) {
			dispose();
			String from=JiaohuFrom.getSelectedItem().toString();
			String to=JiaohuTo.getSelectedItem().toString();
			String cons=constraint.getSelectedItem().toString();
			String num="";
			if(constraint.getSelectedIndex()==3){
				num="="+number.getText();
			}
			//Main.win.instantPane.south.addConstraint(from+cons+to+num);
			Main.win.instantPane.addConstraint(from,cons,to,num);
		}
	}
	
	public static void main(String[]arg0){
		new ConstraintDialog(null);
	}

}
