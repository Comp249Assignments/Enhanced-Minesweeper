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
	
	//Creates a highscore board where high scores are displayed with names and scores.
	public HighScoreBoard(LinkedList<String> names, LinkedList<Integer> scores){
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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
		okButton.addActionListener(this);
		add(BorderLayout.SOUTH,okButton);
		this.pack();

		this.setVisible(true);
	}
	
	//requests the user to enter a name to go along with the score in the highscore board
	public HighScoreBoard(int score){
		this.setTitle("Congratulations");
		this.setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(3,1));
		this.setLocationRelativeTo(null);
		this.setSize(300,150);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		messageLabel=new JLabel("Congratulations, you have a high score of:\n " +score,SwingConstants.LEFT);
		enterLabel=new JLabel("Please enter your name",SwingConstants.LEFT);
		p1.add(messageLabel);
		p1.add(enterLabel);
		p1.add(nameInput);
		add(BorderLayout.NORTH,p1);
		add(BorderLayout.SOUTH,submitButton);
		submitButton.addActionListener(this);
		this.setVisible(true);
		while(this.isVisible()){}
		userName=nameInput.getText();
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void actionPerformed(ActionEvent e){
		setVisible(false);		
	}
}
