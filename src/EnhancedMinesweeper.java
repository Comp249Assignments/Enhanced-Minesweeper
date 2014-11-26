import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

public class EnhancedMinesweeper extends JFrame{
	private Square squares[][];
	private DifficultySelector selector = new DifficultySelector();
	private ImageIcon smileyImage = new ImageIcon("smiley button.png"), winSmileyImage = new ImageIcon("win smiley button.png"), superSaiyan=new ImageIcon("superSaiyan.jpg");
	private JPanel p1 = new JPanel(), p2 = new JPanel(), p3=new JPanel(), p4 = new JPanel();
	private JLabel mineLabel = new JLabel("000"), timeLabel = new JLabel("000"), livesLabel = new JLabel("Lives: 3"), shieldsLabel=new JLabel("Shields: 0"), probesLabel=new JLabel("Probes: 0");
	private JButton smiley = new JButton(smileyImage);
	private JMenuBar menubar = new JMenuBar();
	private JMenu file = new JMenu("File"), game = new JMenu("Game");
	private JMenuItem save = new JMenuItem("Save"), load = new JMenuItem("Load"), exit = new JMenuItem("Exit"), newGame = new JMenuItem("New Game"), reset = new JMenuItem("Reset"), highscore= new JMenuItem("Highscore");
	private int mines, lives, height, width, time, shields, probes, score, powerups;
	private boolean playing, timeGoing, immortality;
	private Timer timer = new Timer(1000, new timerListener());
	private  LinkedList<String> highScorers=new LinkedList();
	private  LinkedList<Integer> highScores=new LinkedList();
	Random random= new Random();
	
	//TODO: Make the images for the powerups change back to normal squares after a few seconds
	
	public EnhancedMinesweeper(){
		timeGoing=false;
		//makes sure a difficulty has been selected before moving on
		while(selector.isVisible()){
			
		}
		int difficulty = selector.getDifficulty();
		lives=3;
		shields=0;
		probes=0;
		immortality=false;
		score=0;
		time=0;
		playing = true;
		score=0;
		
		setLayout(new BorderLayout());
		
		//TODO: Make the mine count decrease when putting a flag. Done, but needs to be fixed
		//TODO: Make the top panel look better
		smiley.setPreferredSize(new Dimension(27, 27));
		smiley.addActionListener(new buttonListener());
		
		file.add(save);
		file.add(load);
		file.add(exit);
		game.add(newGame);
		game.add(highscore);
		game.add(reset);
		menubar.add(file);
		menubar.add(game);
		save.addActionListener(new menuListener());
		load.addActionListener(new menuListener());
		exit.addActionListener(new menuListener());
		newGame.addActionListener(new menuListener());
		highscore.addActionListener(new menuListener());
		//add(menubar/*, BorderLayout.PAGE_START*/);
		
		//p1.setLayout(new GridLayout(1, 3));
		//p1.setLayout(new BorderLayout());
		p4.setLayout(new BorderLayout());
		p1.add(/*BorderLayout.WEST, */mineLabel);
		p1.add(/*BorderLayout.CENTER, */smiley);
		p1.add(/*BorderLayout.EAST, */timeLabel);
		p4.add(menubar, BorderLayout.PAGE_START);
		p4.add(p1, BorderLayout.CENTER);
		p3.add(livesLabel);
		p3.add(shieldsLabel);
		p3.add(probesLabel);
		
		//Btw, in case you get mixed up (I always do) 2D arrays go array[row][column]
		height = selector.getGridHeight();
		width = selector.getGridWidth();
		mines = selector.getMines();
		powerups = selector.getPowerups();
		squares = new Square[height][width];
		p2.setLayout(new GridLayout(height, width));
		height = 28*height;
		width = 25*width;
		
		/*if(difficulty==1){
			squares = new Square[9][9];
			mines = 10;
			//Dimensions of the image are 16 by 16, but I made it a bit bigger so they're not all squished together
			height = 21*9;
			width = 18*9;
			setLayout(new GridLayout(9, 9));
		}
		else if(difficulty==2){
			squares = new Square[16][16];
			mines = 40;
			height = 21*16;
			width = 18*16;
			setLayout(new GridLayout(16, 16));
		}
		else if(difficulty==3){
			squares = new Square[16][30];
			mines = 99;
			height = 21*16;
			width = 18*30;
			setLayout(new GridLayout(16, 30));
		}
		else{
			height = selector.getGridHeight();
			width = selector.getGridWidth();
			mines = selector.getMines();
			squares = new Square[height][width];
			setLayout(new GridLayout(height, width));
			height = 21*height;
			width = 18*width;
		}*/
		
		//Initialize all the square objects and add them to the frame
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x] = new Square();
				squares[y][x].addActionListener(new buttonListener());
				squares[y][x].addMouseListener(new handleRight());
				p2.add(squares[y][x]);
			}
		}
		//addMouseListener(new mouse());
		add(BorderLayout.NORTH, p4);
		add(BorderLayout.CENTER, p2);
		add(BorderLayout.SOUTH, p3);
		setMines(mines);
		setPowerUps(powerups);
		setNumber();
		setSize(width, height+75);
	}
	
	public static void main(String[] args) {
		
		EnhancedMinesweeper frame = new EnhancedMinesweeper();
		frame.setTitle("Enhanced Minesweeper");
		//I like these dimensions for easy mode, but feel free to play around with them if you can find better ones
		//frame.setSize(width, height);
		frame.setLocationRelativeTo(null); // Center the frame   
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void setMines(int mines){
		updateMineLabel();
		while (mines>0){
			int y=random.nextInt(squares.length);
			int x=random.nextInt(squares[y].length);
			//I'm not sure why you put this here. This would make it so that spaces could only hold one mine at a time
			/*if(squares[y][x].getMines()!=0){
				continue;
			}*/
			//There's a max of 3 mines in any one spot. Anything more than that would be ridiculous
			if(squares[y][x].getMines()!=3)
			{
				squares[y][x].addMine();
				mines--;
			}
		}
		/*for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				System.out.print(squares[y][x].getMines()+ " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();*/
	}
	
	//Randomly places all the powerups. Receives number of powerups and picks randomly which type it'll be
	public void setPowerUps(int powerups){
		while (powerups>0){
			int y=random.nextInt(squares.length);
			int x=random.nextInt(squares[y].length);
			/*if(squares[x][y]!=0){
				continue;
			}*/
			//Will only add a powerup if there is no mine and no powerup there
			if(squares[y][x].getMines()==0 && squares[y][x].hasPowerUp()==0){
				squares[y][x].addPowerUp(random.nextInt(10));
				powerups--;
			}
		}
	}
	
	
	//Method that sets the number of adjacent mines for each space
	public void setNumber(){
		int number=0;
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				//Checks how many mines are above the current space
				if(y>0){
					number+=squares[y-1][x].getMines();
					if(x>0){
						number+=squares[y-1][x-1].getMines();
					}
					if(x<squares[y].length-1){
						number+=squares[y-1][x+1].getMines();
					}
				}
				//Checks how many mines are below the space
				if(y<squares.length-1){
					number+=squares[y+1][x].getMines();
					if(x>0){
						number+=squares[y+1][x-1].getMines();
					}
					if(x<squares[y].length-1){
						number+=squares[y+1][x+1].getMines();
					}
				}
				//Checks how many mines are in the space to the left
				if(x>0){
					number+=squares[y][x-1].getMines();
				}
				//Checks how many mines are in the space to the right
				if(x<squares[y].length-1){
					number+=squares[y][x+1].getMines();
				}
				squares[y][x].setNumber(number);
				number=0;
			}
		}
		/*for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				System.out.print(squares[y][x].getNumber()+ " ");
			}
			System.out.println();
		}*/
	}
	
	//TODO: Add custom powerups thing
	
	//If you click an empty space this will clear out all adjacent empty spaces
	public void clickAdjacent(int x, int y){
		int click=0;
		//Checks spaces above current space
		if(y>0){
			click=squares[y-1][x].adjacentClicked();
			if(click>=4)
				implementPowerUp(click-4, x, y-1);
			if(click!=2&&click!=-1)
				clickAdjacent(x, y-1);
			if(x>0){
				click=squares[y-1][x-1].adjacentClicked();
				if(click>=4)
					implementPowerUp(click-4, x-1, y-1);
				if(click!=2&&click!=-1)
					clickAdjacent(x-1, y-1);
			}
			if(x<squares[y].length-1){
				click=squares[y-1][x+1].adjacentClicked();
				if(click>=4)
					implementPowerUp(click-4, x+1, y-1);
				if(click!=2&&click!=-1)
					clickAdjacent(x+1, y-1);
			}
		}
		//Checks spaces below
		if(y<squares.length-1){
			click=squares[y+1][x].adjacentClicked();
			if(click>=4)
				implementPowerUp(click-4, x, y+1);
			if(click!=2&&click!=-1)
				clickAdjacent(x, y+1);
			if(x>0){
				click=squares[y+1][x-1].adjacentClicked();
				if(click>=4)
					implementPowerUp(click-4, x-1, y+1);
				if(click!=2&&click!=-1)
					clickAdjacent(x-1, y+1);
			}
			if(x<squares[y].length-1){
				click=squares[y+1][x+1].adjacentClicked();
				if(click>=4)
					implementPowerUp(click-4, x+1, y+1);
				if(click!=2&&click!=-1)
					clickAdjacent(x+1, y+1);
			}
		}
		//Checks space to the left
		if(x>0){
			click=squares[y][x-1].adjacentClicked();
			if(click>=4)
				implementPowerUp(click-4, x-1, y);
			if(click!=2&&click!=-1)
				clickAdjacent(x-1, y);
		}
		//Checks space to the right
		if(x<squares[y].length-1){
			click=squares[y][x+1].adjacentClicked();
			if(click>=4)
				implementPowerUp(click-4, x+1, y);
			if(click!=2&&click!=-1)
				clickAdjacent(x+1, y);
		}
	}
	
	//TODO: Allow the user to start a new game after this and the victory method as well (including the option to change the difficulty setting)
	public void hitMine(){
		score-=120;
		if(!immortality){
			if (shields>0){
				shields--;
				shieldsLabel.setText("Shields: " + shields);
			}
			else{
				lives--;
				livesLabel.setText("Lives: " + lives);
			}
		}
		mines--;
		updateMineLabel();
		
		if(lives==0){
			revealAll();
			JOptionPane.showMessageDialog(null, "GAME OVER\nYou have run out of lives");
			playing=false;
			timer.stop();
		}
	}
	
	public void implementPowerUp(int type, int x, int y){
		if(type==9){
			immortality=true;
			lives=999;
			livesLabel.setText("Lives: "+ lives);
			smiley.setIcon(superSaiyan);
			return;
		}
		else{
			switch (type%3){
				case 0:
					shields+=3;
					shieldsLabel.setText("Shields: " + shields);
					break;
				case 1:
					probes++;
					probesLabel.setText("Probes: " + probes);
					break;
				case 2:
					score+=200;
					break;
			}
		}
	}
	public void updateMineLabel(){
		if(mines<10){
			mineLabel.setText("00"+mines);
		}
		else if(mines<100){
			mineLabel.setText("0"+mines);
		}
		else if(mines<1000){
			mineLabel.setText(""+mines);
		}
		else{
			mineLabel.setText(""+999);
		}
	}
	
	//TODO: Let the user start a new game after this
	//Method that checks if the game has been won (everything that is not a mine has been clicked) and if so, displays a victory message and records the highscore
	public void checkWin(){
		boolean won=true;
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				if(!squares[y][x].isClicked()&&squares[y][x].getMines()==0)
					won=false;
			}
		}
		if(won){
			revealAll();
			playing=false;
			score=score*selector.getDifficulty()-time;
			smiley.setIcon(winSmileyImage);
			JOptionPane.showMessageDialog(null, "You won!\nyour score is: "+ score);
			//TODO: Find out what this is
			Thread t=new Thread(new buttonListener());
			t.start();
		}
	}
	
	//TODO: Implement high scores
	public  void highScore(){
		if(load()==0){
			for(int i=0;i<10;i++){
				highScorers.add("empty");
				highScores.add(0);
			}
		}
		
		for(int i=9;i>0;i--){
			if(score>highScores.get(i)&&score<=highScores.get(i-1)){
				HighScoreBoard highScoreInput=new HighScoreBoard(score);
				
				highScores.add(i,score);
				highScorers.add(i,highScoreInput.getUserName());
				highScorers.remove(10);
				highScores.remove(10);
			}
		}
		if(score>highScores.get(0)){
			HighScoreBoard highScoreInput=new HighScoreBoard(score);
			highScores.add(0,score);
			highScorers.add(0,highScoreInput.getUserName());
			highScorers.remove(10);
			highScores.remove(10);
		}
		
		HighScoreBoard highScoreBoard=new HighScoreBoard(highScorers, highScores);
	}
	
	//TODO: reset method should actually change the layout of everything
	public void reset(){
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x].setClicked(false);
			}
		}
		mines=selector.getMines();
		time=0;
		lives=3;
		shields=0;
		probes=0;
		timer.stop();
		timeGoing=false;
		smiley.setIcon(smileyImage);
		updateMineLabel();
		timeLabel.setText("000");
		shieldsLabel.setText("Shields: 0");
		probesLabel.setText("Probes: 0");
		livesLabel.setText("Lives: 3");
		playing=true;
	}
	
	//TODO: implement this method
	public void save(){
		
	}
	
	//TODO: implement this method
	public int load(){
		return 0;
	}
	
	public void exit(){
		System.exit(0);
	}
	
	//TODO: implement this method
	public void newGame(){
		
	}
	
	public void revealAll(){
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x].reveal();
			}
		}
	}
	
	class handleRight implements MouseListener{
		public void mousePressed(MouseEvent e){}
		
		public void mouseReleased(MouseEvent e){}
		
		public void mouseEntered(MouseEvent e){}
		
		public void mouseExited(MouseEvent e){}
		
		public void mouseClicked(MouseEvent e){
			if((SwingUtilities.isRightMouseButton(e)|| e.isControlDown())&&playing){
				((Square)e.getSource()).flag();
				if( ((Square)e.getSource()).getFlags()==0)
					 mines+=4;
				mines--;
				
				updateMineLabel();
			}
		}
	}
	
	class buttonListener implements ActionListener, Runnable{
		@Override
		
		public void run(){
			highScore();
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==smiley){
				reset();
			}
			else{
				if(playing)
				{
					for(int y=0; y<squares.length; y++){
						for(int x=0; x<squares[y].length; x++){
							if(e.getSource()==squares[y][x]){
								if(!timeGoing){
									timeGoing=true;
									timer.start();
								}
								
								int type = squares[y][x].clicked();
								if(squares[y][x].getFlags()>0&&probes>0){
									probes--;
									probesLabel.setText("Probes: " + probes);
									if(type==1){
										shields++;
									}	
								}
								if(type==1){
									
									hitMine();
								}
								else if(type==3)
									clickAdjacent(x, y);
								else if(type>4)
									implementPowerUp(type-4,x,y);
							
								checkWin();
							}
							
						}
					}
					score+=20;
				}
			}
		}
	}
	
	class menuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==save){
				save();
			}
			else if(e.getSource()==load){
				load();
			}
			else if(e.getSource()==exit){
				exit();
			}
			else if(e.getSource()==newGame){
				newGame();
			}
			else if(e.getSource()==reset){
				reset();
			}
			else if(e.getSource()==highscore){
				
			}
		}
	}
	
	class timerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			time++;
			if(time<10){
				timeLabel.setText("00"+time);
			}
			else if(time<100){
				timeLabel.setText("0"+time);
			}
			else if(time<1000){
				timeLabel.setText(""+time);
			}
			else{
				timeLabel.setText(""+999);
			}
		}
	}
}
