import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HighScoreBoard extends JFrame {
	private JButton okButton=new JButton("OK"), submitButton=new JButton("Submit");
	private JPanel p1=new JPanel();
	private JTextField usersScore=new JTextField();
	private JLabel messageLabel, enterLabel;
	private JLabel[] highScores=new JLabel[10];
	private JLabel[] highScorers=new JLabel[10];
	
	
	public HighScoreBoard(){
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("High Scores");
		this.setSize(200,300);
		this.setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(10,1));
		for(int i=0; i<10;i++){
			p1.add(highScores[i]=new JLabel((1+i)+"."));
		add(BorderLayout.WEST,p1);
		}
		add(BorderLayout.SOUTH,okButton);
	}
	public HighScoreBoard(int score){
		this.setVisible(true);
		this.setTitle("Congradulations");
		this.setLayout(new BorderLayout());
		p1.setLayout(new GridLayout(3,1));
		this.setLocationRelativeTo(null);
		this.setSize(300,150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		messageLabel=new JLabel("Congradulations you have a high score of:\n " +score,SwingConstants.LEFT);
		enterLabel=new JLabel("Please enter your name",SwingConstants.LEFT);
		p1.add(messageLabel);
		p1.add(enterLabel);
		p1.add(usersScore);
		add(BorderLayout.NORTH,p1);
		add(BorderLayout.SOUTH,submitButton);
		submitButton.addActionListener(new buttonListener());
		
	}
								

	public class buttonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==submitButton)
			setVisible(false);
		}
	}

}