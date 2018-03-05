package UI;

import java.awt.Dimension;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import Shape.Rect;

public class ClockDialog extends JDialog implements ActionListener {

	Rect editor;

	JComboBox referCombox = new JComboBox();
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	String[] clockType = { "physical", "logical" };
	JComboBox typeCombox = new JComboBox(clockType);

	JTextField defText = new JTextField();
	JTextField unitText = new JTextField();
	JTextField resolutionText = new JTextField();
	JTextField maxText = new JTextField();
	JTextField offsetText = new JTextField();
	JButton confirm = new JButton("confirm");
	JButton cancel = new JButton("cancel");
	JLabel background = null;

	int selectedIndex = 0;// 引用选择框选择的下标

	Clock myClock;

	public void actionPerformed(ActionEvent e) {
		LinkedList<Rect> tempRects = new LinkedList<Rect>();
		for(int i = 0;i < Main.win.myProblemDiagram.components.size();i++){
			if(Main.win.myProblemDiagram.components.get(i) instanceof Rect && Main.win.myProblemDiagram.getMachine() != Main.win.myProblemDiagram.components.get(i)){
				tempRects.add((Rect)Main.win.myProblemDiagram.components.get(i));
			}
		}
		LinkedList<Clock> tempClocks = new LinkedList<Clock>();
		for(int i = 0;i < Main.win.cd.getClocks().size();i++){
			tempClocks.add(Main.win.cd.getClocks().get(i));
		}
		if (e.getActionCommand().equals("confirm")) {
			dispose();
			if (selectedIndex !=0) {
				myClock.updateName(defText.getText());
				myClock.updateType(typeCombox.getSelectedIndex());
				myClock.updateUnit(unitText.getText());
				myClock.setMax(maxText.getText());
				myClock.setOffset(offsetText.getText());
				myClock.setResolution(resolutionText.getText());
			}
			Main.win.cd.setClock(editor, myClock);
			InstantGraph tempIg = new InstantGraph(editor, myClock);
			if (Main.win.instantPane == null) {
				//InstantPane ip = new InstantPane(tempIg);
				InstantPane ip = new InstantPane(editor, myClock);
				Main.win.instantPane = ip;
				Main.win.myDisplayPane.addPane(Main.win.instantPane,
						"InstantGraph");
				for(int i = 0;i < tempClocks.size();i++){
					if(Main.win.cd.getClocks().get(i) != myClock){
						Clock tempClock = tempClocks.get(i);
						Rect tempRect = tempRects.get(i);
						InstantGraph temp = new InstantGraph(tempRect,tempClock);
						Main.win.instantPane.addGraph(temp);
					}
				}
			}
			else {
				Main.win.instantPane.addGraph(editor, myClock);
			}
			FatherPane kk = Main.win.myDisplayPane.getMyPane("InstantGraph");
			Main.win.myDisplayPane.setSelected(kk);
		} else {
			dispose();
		}
	}

	public ClockDialog(Rect editor) {
		super(Main.win, true);
		this.editor = editor;
		myClock = Main.win.cd.getClock(editor);
		if (myClock == null) {
			myClock = new Clock();
		}
		try {
			Init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Init() throws Exception {
		setTitle("ClockEditor");
		setSize(new Dimension(360, 376));
		this.setResizable(false);
		ImageIcon img = new ImageIcon(Main.class.getResource("/images/ee.jpg"));
		background = new JLabel(img);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double width = d.getWidth();
		double height = d.getHeight();
		setLocation((int) width / 2 - 200, (int) height / 2 - 150);
		JPanel contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setLayout(null);

		JLabel referLabel = new JLabel("Reference :");
		JLabel defLabel = new JLabel("Name :");
		JLabel typeLabel = new JLabel("Clocktype :");
		JLabel unitLabel = new JLabel("Unit :");
		JLabel resolutionLabel = new JLabel("Resolution :");
		JLabel maxLabel = new JLabel("Maximal :");
		JLabel offsetLabel = new JLabel("Offset :");

		typeCombox.setEnabled(false);
		defText.setEnabled(false);
		unitText.setEnabled(false);
		resolutionText.setEnabled(false);
		maxText.setEnabled(false);
		offsetText.setEnabled(false);

		int anIndex=-2;
		
		referLabel.setBounds(60, 20, 100, 20);
		referCombox.setModel(this.model);
		this.model.addElement("Default");
		this.model.addElement("NEW");
		for (int i = 0; i < Main.win.cd.getClocks().size(); i++) {
			this.model.addElement(Main.win.cd.getClocks().get(i).getName());
			if(Main.win.cd.getClocks().get(i).getName().equals(this.myClock.getName())){
				anIndex=i;
			}
		}
		
		typeCombox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (typeCombox.getSelectedIndex() == 0) {
					unitText.setText("s");
					unitText.setEnabled(false);
				} else {
					unitText.setText("");
					unitText.setEnabled(true);
				}
			}

		});
		
		referCombox.setBounds(180, 20, 100, 20);
		referCombox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectedIndex = referCombox.getSelectedIndex();
				if (selectedIndex == 0) {
					myClock=new Clock();
					defText.setText("Default");
					resolutionText.setText(null);
					maxText.setText(null);
					offsetText.setText(null);
					typeCombox.setEnabled(false);
					defText.setEnabled(false);
					typeCombox.setSelectedIndex(0);
					resolutionText.setEnabled(false);
					maxText.setEnabled(false);
					offsetText.setEnabled(false);
				}
				else if (selectedIndex == 1) {
					myClock=new Clock();
					defText.setText(null);
					resolutionText.setText(null);
					maxText.setText(null);
					offsetText.setText(null);
					typeCombox.setEnabled(true);
					defText.setEnabled(true);
					if (typeCombox.getSelectedIndex() == 0)
						unitText.setEnabled(false);
					resolutionText.setEnabled(true);
					maxText.setEnabled(true);
					offsetText.setEnabled(true);
				} else {
					if (selectedIndex >= 2)
						myClock = Main.win.cd.getClocks().get(selectedIndex - 2);
					defText.setText(myClock.getName());
					resolutionText.setText(myClock.getResolution());
					maxText.setText(myClock.getMax());
					offsetText.setText(myClock.getOffset());
					typeCombox.setEnabled(true);
					typeCombox.setSelectedIndex(myClock.getClockType());
					defText.setEnabled(false);
					unitText.setText(myClock.getUnit());
					resolutionText.setEnabled(true);
					maxText.setEnabled(true);
					offsetText.setEnabled(true);
				}
			}

		});

		referCombox.setSelectedIndex(anIndex+2);
		
		defLabel.setBounds(60, 55, 100, 20);
		defText.setBounds(180, 55, 100, 20);

		typeLabel.setBounds(60, 90, 100, 20);
		typeCombox.setBounds(180, 90, 100, 20);

		unitLabel.setBounds(60, 125, 100, 20);
		unitText.setBounds(180, 125, 100, 20);

		confirm.setBounds(100, 310, 80, 25);
		confirm.addActionListener(this);
		cancel.setBounds(200, 310, 80, 25);
		cancel.addActionListener(this);

		resolutionLabel.setBounds(20, 25, 100, 20);
		resolutionText.setBounds(130, 25, 100, 20);
		maxLabel.setBounds(20, 60, 100, 20);
		maxText.setBounds(130, 60, 100, 20);
		offsetLabel.setBounds(20, 95, 100, 20);
		offsetText.setBounds(130, 95, 100, 20);

		JPanel properPane = new JPanel();
		properPane.setBorder(new TitledBorder("Optional properties :"));
		properPane.setLayout(null);
		properPane.setBounds(50, 160, 240, 130);
		properPane.setOpaque(false);

		properPane.add(resolutionLabel);
		properPane.add(resolutionText);
		properPane.add(maxLabel);
		properPane.add(maxText);
		properPane.add(offsetLabel);
		properPane.add(offsetText);

		contentPane.add(referLabel);
		contentPane.add(referCombox);
		contentPane.add(defLabel);
		contentPane.add(defText);
		contentPane.add(typeLabel);
		contentPane.add(typeCombox);
		contentPane.add(unitLabel);
		contentPane.add(unitText);
		contentPane.add(properPane);
		contentPane.add(confirm);
		contentPane.add(cancel);

		this.setContentPane(contentPane);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
		this.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			// TODO exception
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException ex) {
		} catch (IllegalAccessException ex) {
		} catch (InstantiationException ex) {
		} catch (ClassNotFoundException ex) {
		}
		// ClockDialog dd = new ClockDialog();
	}

}
