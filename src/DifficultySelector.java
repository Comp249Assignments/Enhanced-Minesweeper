import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class DifficultySelector extends JFrame{
	private int difficulty, height, width, mines, powerups;
	private JRadioButton r1 = new JRadioButton("Easy"), r2 = new JRadioButton("Medium"), r3 = new JRadioButton("Hard"), r4  = new JRadioButton("Custom");
	private JTextField heightField = new JTextField(7), widthField = new JTextField(7), minesField = new JTextField(7), powerUpField = new JTextField(7);
	private JLabel heightLabel = new JLabel("Height", SwingConstants.RIGHT), widthLabel = new JLabel("Width", SwingConstants.RIGHT), minesLabel = new JLabel("Mines", SwingConstants.RIGHT), powerUpLabel = new JLabel("Powerups", SwingConstants.RIGHT);
	private JButton okButton = new JButton("Ok");
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel p1 = new JPanel(), p2 = new JPanel();
	
	public DifficultySelector(){
		buildEverything();
	}
	
	//method to build the frame
	public void buildEverything(){
		setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(4, 1));
		p2.setLayout(new GridLayout(1, 9));
		
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
		p2.add(powerUpLabel);
		p2.add(powerUpField);
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
		setSize(650, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		p1.setVisible(true);
		p2.setVisible(true);
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
	
	//For the ok button. Sets the dimensions, number of mines, and number of powerups based on the difficulty selected.
	class buttonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//I put the dimensions and number of mines based on the difficulty settings of the original minesweeper
			boolean visible=true;
			boolean error=true;
			//easy
			if(r1.isSelected()){
				difficulty=1;
				height=10;
				width=10;
				mines=10;
				powerups=10;
				error=false;
			}
			//medium
			else if(r2.isSelected()){
				difficulty=2;
				height=16;
				width=16;
				mines=40;
				powerups=4;
				error=false;
			}
			//hard
			else if(r3.isSelected()){
				difficulty=3;
				height=16;
				width=30;
				mines=99;
				powerups=5;
				error=false;
			}
			//custom with error handling in case the user enters impossible setups
			else{
				difficulty=4;
				//If any of the spaces were left blank, then there will be an error
				if(!(heightLabel.getText()==null||widthLabel.getText()==null||minesLabel.getText()==null||powerUpLabel.getText()==null)){
					try{
						height=Integer.parseInt(heightField.getText());
						width=Integer.parseInt(widthField.getText());
						mines=Integer.parseInt(minesField.getText());
						powerups=Integer.parseInt(powerUpField.getText());
						error=false;
						//if there are so many mines that there couldn't possibly be an open space, then there will be an error
						if(mines>=(width*height*3-3)){
							error=true;
							JOptionPane.showMessageDialog(null, "Error: Too many mines for the space defined", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
						//if there are enough mines so that the code might not be able to fit all the powerups, then there will be an error
						if(powerups!=0&&(width*height)-mines<=powerups){
							error=true;
							JOptionPane.showMessageDialog(null, "Error: Too many mines and powerups for the space defined", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
						//If the user tries to start a game with 0 mines or negative mines, then there will be an error
						if(mines<=0){
							error=true;
							JOptionPane.showMessageDialog(null, "Error: There must be at least one mine", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
						//If the user tries to start a game with negative powerups, then there will be an error
						if(powerups<0){
							error=true;
							JOptionPane.showMessageDialog(null, "Error: There can't be less than 0 powerups", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
					catch(NumberFormatException f){
						JOptionPane.showMessageDialog(null, "Error: One or more of the entries was not a valid number", "ERROR", JOptionPane.ERROR_MESSAGE);
						error=true;
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "You must enter a value for all the spaces", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
			visible=error;
			//if no errors are detected, the code can continue, so the frame will be hidden
			setVisible(visible);
		}
	}
}
