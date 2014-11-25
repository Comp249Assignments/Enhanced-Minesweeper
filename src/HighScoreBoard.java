import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HighScoreBoard extends JFrame implements ActionListener {
	private JButton okButton=new JButton("OK"), submitButton=new JButton("Submit");
	private JPanel p1=new JPanel();
	private JTextField nameInput=new JTextField();
	private JLabel messageLabel, enterLabel;
	private JLabel numbering;
	private JLabel highScores;
	private JLabel highScorers;
	private String userName;
	
	
	
	
	
	public HighScoreBoard(LinkedList<String> names, LinkedList<Integer> scores){
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("High Scores");
		this.setLayout(new BorderLayout());
		//_------------------------------------------------------------------------------------------
		p1.setLayout(new GridLayout(10,2));		
		for(int i=0;i<10;i++){
			p1.add(highScorers=new JLabel(i+1+". "+names.get(i)));
			p1.add(highScores=new JLabel("        "+scores.get(i)));
		}
		
		add(BorderLayout.WEST,p1);
		//add(BorderLayout.WEST,p2);
		
		add(BorderLayout.SOUTH,okButton);
		this.pack();

		this.setVisible(true);
	}
	
	public HighScoreBoard(int score){
		this.setTitle("Congradulations");
		this.setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(2,1));
		this.setLocationRelativeTo(null);
		this.setSize(300,150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//messageLabel=new JLabel("Congradulations you have a high score of:\n " +score,SwingConstants.LEFT);
		enterLabel=new JLabel("Please enter your name",SwingConstants.LEFT);
		//p1.add(messageLabel);
		p1.add(enterLabel);
		p1.add(nameInput);
		add(BorderLayout.NORTH,p1);
		add(BorderLayout.SOUTH,submitButton);
		submitButton.addActionListener(this);
		this.setVisible(true);
		while(this.isVisible()){}
		userName=nameInput.getText();
	}
								
/*
	public int getScore(int location){
		return scores[location];
	}
	
	public void setNewScore(int location, int newScore, String userName){
		if(location==-1)
			return;
		for(int i=9;i>=location;i--){
			scores[i]=scores[i-1];
			scorers[i]=scorers[i-1];
		}
		scores[location]=newScore;
		scorers[location]=userName;
	}
	
	*/
	/*
	public int checkIfHigh(int newScore){
		for(int i=0;i<9;i++){
			if(newScore>scores[i]&&newScore<=scores[i+1]){
				return i;
			}
			if(newScore>scores[9])
				return 9;
		}
		return -1;
	}
	
	public String submit(){
		return userName.toString();
	}
	*/
	public String getUserName(){
		return userName;
	}
	
	public void actionPerformed(ActionEvent e){
		//while(!scorers.equals(userName.getText())){}
		setVisible(false);		
	}
	


}
