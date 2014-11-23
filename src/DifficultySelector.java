import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class DifficultySelector extends JFrame{
	private boolean visible;
	private int difficulty, height, width, mines, powerups;
	private JRadioButton r1 = new JRadioButton("Easy"), r2 = new JRadioButton("Medium"), r3 = new JRadioButton("Hard"), r4  = new JRadioButton("Custom");
	private JTextField heightField = new JTextField(), widthField = new JTextField(10), minesField = new JTextField(10);
	private JLabel heightLabel = new JLabel("Height", SwingConstants.RIGHT), widthLabel = new JLabel("Width", SwingConstants.RIGHT), minesLabel = new JLabel("Mines", SwingConstants.RIGHT);
	private JButton okButton = new JButton("Ok");
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel p1 = new JPanel(), p2 = new JPanel();
	
	
	public DifficultySelector(){
		setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(4, 1));
		p2.setLayout(new GridLayout(1, 7));
		
		buttonGroup.add(r1);
		buttonGroup.add(r2);
		buttonGroup.add(r3);
		buttonGroup.add(r4);
		
		p2.add(r4);
		p2.add(heightLabel);
		p2.add(heightField);
		p2.add(widthLabel);
		p2.add(widthField);
		p2.add(minesLabel);
		p2.add(minesField);
		p1.add(r1);
		p1.add(r2);
		p1.add(r3);
		p1.add(p2);
		
		okButton.addActionListener(new buttonListener());
		r1.setSelected(true);
		
		add(BorderLayout.CENTER, p1);
		add(BorderLayout.SOUTH, okButton);
		
		setTitle("Enhanced Minesweeper Difficulty Selection");
		setLocationRelativeTo(null); // Center the frame   
		setSize(500, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		visible=true;
	}
	
	public boolean isVisible(){
		return visible;
	}
	public int getDifficulty(){
		return difficulty;
	}
	public int getGridHeight(){
		return height;
	}
	public int getGridWidth(){
		return width;
	}
	public int getMines(){
		return mines;
	}
	public int getPowerups(){
		return powerups;
	}
	
	
	
	class buttonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//I put the dimensions and number of mines based on the difficulty setting of the original minesweeper
			setVisible(false);
			if(r1.isSelected()){
				difficulty=1;
				height=10;
				width=10;
				mines=10;
				powerups=3;
				visible=false;
			}
			else if(r2.isSelected()){
				difficulty=2;
				height=16;
				width=16;
				mines=40;
				powerups=4;
				visible=false;
			}
			else if(r3.isSelected()){
				difficulty=3;
				height=16;
				width=30;
				mines=99;
				powerups=5;
				visible=false;
			}
			else{
				//TO DO: Proper error handling for this stuff
				difficulty=4;
				//System.out.println("Here");
				boolean error=false;
				if(!(heightLabel.getText()==null||widthLabel.getText()==null||minesLabel.getText()==null)){
					//System.out.println("Here2");
					try{
						//System.out.println("Here3");
						//System.out.println("height " + heightLabel.getText() + " width " + widthLabel.getText() + " mines " + minesLabel.getText());
						height=Integer.parseInt(heightField.getText());
						width=Integer.parseInt(widthField.getText());
						mines=Integer.parseInt(minesField.getText());
						//System.out.println("height " + height + " width " + width + " mines " + mines);
						if(mines>=(width*height*3)||mines<=0){
							error=true;
							//System.out.println("Here4");
						}
					}
					catch(NumberFormatException f){
						error=true;
						//System.out.println("Here5");
					}
				}
				visible=error;
			}
			setVisible(visible);
		}
	}
}
