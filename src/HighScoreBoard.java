import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class HighScoreBoard extends JFrame {
	private JButton okButton=new JButton("OK"), submitButton=new JButton("Submit");
	private JPanel p1=new JPanel();
	
	
	public HighScoreBoard(){
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("High Scores");
		this.setSize(200,300);
		//this.setLayout(new GridLayout(3,10));
		p1.add(okButton);
		add(BorderLayout.SOUTH,p1);
	}
	public HighScoreBoard(int score){
		this.setVisible(true);
		this.setTitle("Congradulations");
		this.setLayout(new GridLayout(1,2));
		this.setLocation(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
								

	public class buttonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
		setVisible(false);
		}
	}

}