import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

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
	private JMenuItem save = new JMenuItem("Save"), load = new JMenuItem("Load"), exit = new JMenuItem("Exit"), newGame = new JMenuItem("New Game"), reset = new JMenuItem("Reset"), highscore= new JMenuItem("Highscore"), instructions = new JMenuItem("Instructions");
	private int mines, lives, height, width, time, shields, probes, score, powerups;
	private boolean playing, timeGoing, immortality, startNewGame;
	private Timer timer = new Timer(1000, new timerListener());
	private  LinkedList<String> highScorers=new LinkedList();
	private  LinkedList<Integer> highScores=new LinkedList();
	Random random= new Random();
	
	public EnhancedMinesweeper(){
		timeGoing=false;
		startNewGame=false;
		
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
		game.add(instructions);
		menubar.add(file);
		menubar.add(game);
		save.addActionListener(new menuListener());
		load.addActionListener(new menuListener());
		exit.addActionListener(new menuListener());
		newGame.addActionListener(new menuListener());
		highscore.addActionListener(new menuListener());
		reset.addActionListener(new menuListener());
		instructions.addActionListener(new menuListener());
		
		p4.setLayout(new BorderLayout());
		p1.add(mineLabel);
		p1.add(smiley);
		p1.add(timeLabel);
		p4.add(menubar, BorderLayout.PAGE_START);
		p4.add(p1, BorderLayout.CENTER);
		p3.add(livesLabel);
		p3.add(shieldsLabel);
		p3.add(probesLabel);
		
		newGame();
	}
	
	public static void main(String[] args) {
		
		EnhancedMinesweeper frame = new EnhancedMinesweeper();
		frame.setTitle("Enhanced Minesweeper");
		frame.setLocationRelativeTo(null); // Center the frame   
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void setMines(int mines){
		updateMineLabel();
		while (mines>0){
			int y=random.nextInt(squares.length);
			int x=random.nextInt(squares[y].length);
			
			//There's a max of 3 mines in any one spot. We felt that more than that would be a bit much
			if(squares[y][x].getMines()!=3)
			{
				squares[y][x].addMine();
				mines--;
			}
		}
	}
	
	//Randomly places all the powerups. Receives number of powerups and picks randomly which type it'll be
	public void setPowerUps(int powerups){
		while (powerups>0){
			int y=random.nextInt(squares.length);
			int x=random.nextInt(squares[y].length);
			
			//Will only add a powerup if there is no mine and no powerup there
			if(squares[y][x].getMines()==0 && squares[y][x].hasPowerUp()==0){
				squares[y][x].addPowerUp(random.nextInt(100));
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
	}
	
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
	
	//This method runs when you click on a mine
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
			askNewGame();
		}
	}
	
	//This runs when a powerup is activated
	public void implementPowerUp(int type, int x, int y){
		//Type 9 means invincibility
		if(type==9){
			immortality=true;
			lives=999;
			livesLabel.setText("Lives: "+ lives);
			smiley.setIcon(superSaiyan);
			return;
		}
		//Otherwise there are three other types. Numbers congruent to 0 mod 3 are shields, 1 mod 3 are probes, and 2 mod 3 are score boosters
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
	//TODO: add instructions
	
	//This method properly formats the mine label based on how many mines there are
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
			JOptionPane.showMessageDialog(null, "You won!\nyour score is: "+ (score+20));
			// activates highScore(). and is needed since you cant pause a single thread in the middle of an ActionEvent
			Thread t=new Thread(new buttonListener());
			t.start();
		}
	}
	
	//Displays the highscore board
	public  void highScore(){
		if(highScoreLoad()==0){
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
		highScoreSave();
		while(highScoreBoard.isVisible());
		askNewGame();
	}
	
	//Resets the game
	public void reset(){
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x].reset();
			}
		}
		mines=selector.getMines();
		powerups=selector.getPowerups();
		time=0;
		lives=3;
		shields=0;
		probes=0;
		score=0;
		time=0;
		immortality=false;
		timer.stop();
		timeGoing=false;
		smiley.setIcon(smileyImage);
		updateMineLabel();
		timeLabel.setText("000");
		shieldsLabel.setText("Shields: 0");
		probesLabel.setText("Probes: 0");
		livesLabel.setText("Lives: 3");
		playing=true;
		setMines(mines);
		setPowerUps(powerups);
		setNumber();
	}
	
	//loads the highScore list and returns 0 if there is no list to load
	public int highScoreLoad(){
		try{
		ObjectInputStream input2 = new ObjectInputStream(new FileInputStream("highScore saves.txt"));
		highScores=(LinkedList<Integer>)input2.readObject();
		highScorers=(LinkedList<String>)input2.readObject();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(FileNotFoundException e){
			return 0;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return 1;
	}
	
	//records the highScore list
	public void highScoreSave(){
		try{
		ObjectOutputStream output2 = new ObjectOutputStream(new FileOutputStream("highScore saves.txt"));
		output2.writeObject(highScores);
		output2.writeObject(highScorers);
		output2.close();
		} 
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	//saves the game
	public void save(){
		try{
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("saves.txt"));
			output.writeObject(squares);
			output.writeObject(highScores);
			output.writeObject(highScorers);
			output.writeObject(selector);
			output.writeInt(lives);
			output.writeInt(shields);
			output.writeInt(probes);
			output.writeInt(score);
			output.writeInt(time);
			output.writeInt(mines);
			output.writeBoolean(immortality);
			
			output.flush();
			output.close();
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
	}
	
	//loads the previously saved game
	public void load(){
		try{
			int difficulty = selector.getDifficulty();
			
			//Remove everything so it can be rebuilt
			if(squares!=null){
				for(int y=0; y<squares.length; y++){
					for(int x=0; x<squares[y].length; x++){
						p2.remove(squares[y][x]);
					}
				}
				this.remove(p4);
				this.remove(p2);
				this.remove(p3);
			}
			ObjectInputStream input=new ObjectInputStream(new FileInputStream("saves.txt"));
			squares=(Square[][])input.readObject();
			highScores=(LinkedList<Integer>)input.readObject();
			highScorers=(LinkedList<String>)input.readObject();
			selector=(DifficultySelector)input.readObject();

			lives=input.readInt();
			livesLabel.setText("Lives: "+ lives);

			shields=input.readInt();
			shieldsLabel.setText("Shields: "+ shields);

			probes=input.readInt();
			probesLabel.setText("Probes: "+ probes);
			
			score=input.readInt();

			time=input.readInt();
			
			mines=input.readInt();
			updateMineLabel();
			immortality=input.readBoolean();
			playing = true;
			
			height = selector.getGridHeight();
			width = selector.getGridWidth();
			p2.setLayout(new GridLayout(height, width));
			height = 28*height;
			width = 25*width;
			for(int y=0; y<squares.length; y++){
				for(int x=0; x<squares[y].length; x++){
					squares[y][x].addActionListener(new buttonListener());
					squares[y][x].addMouseListener(new handleRight());
					p2.add(squares[y][x]);
				}
			}
			add(BorderLayout.NORTH, p4);
			add(BorderLayout.CENTER, p2);
			add(BorderLayout.SOUTH, p3);
			setSize(width, height+75);
			input.close();
			smiley.setIcon(smileyImage);
			setVisible(false);

			setVisible(true);
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	//Exits the game
	public void exit(){
		System.exit(0);
	}
	
	//Asks the user if they want to play a new game. Runs after a victory or loss
	public void askNewGame(){
		int yes = JOptionPane.showConfirmDialog(null, "New Game?", "Do you wish to play a new game", JOptionPane.YES_NO_OPTION);
		if(yes==0){
			startNewGame=true;
			Thread t = new Thread(new buttonListener());
			t.start();
		}
		else{
			exit();
		}
	}
	
	//Starts a new game (with the option to change difficulty and everything)
	public void newGame(){
		setVisible(false);
		selector.setVisible(true);
		while(selector.isVisible()){
			
		}
		setVisible(true);
		int difficulty = selector.getDifficulty();
		
		//Remove everything so it can be rebuilt
		if(squares!=null){
			for(int y=0; y<squares.length; y++){
				for(int x=0; x<squares[y].length; x++){
					p2.remove(squares[y][x]);
				}
			}
			this.remove(p4);
			this.remove(p2);
			this.remove(p3);
		}
		
		//Start rebuilding everything
		lives=3;
		shields=0;
		probes=0;
		immortality=false;
		score=0;
		time=0;
		playing = true;
		
		height = selector.getGridHeight();
		width = selector.getGridWidth();
		mines = selector.getMines();
		powerups = selector.getPowerups();
		squares = new Square[height][width];
		p2.setLayout(new GridLayout(height, width));
		height = 28*height;
		width = 25*width;
		
		//Initialize all the square objects and add them to the frame
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x] = new Square();
				squares[y][x].addActionListener(new buttonListener());
				squares[y][x].addMouseListener(new handleRight());
				p2.add(squares[y][x]);
			}
		}
		smiley.setIcon(smileyImage);
		add(BorderLayout.NORTH, p4);
		add(BorderLayout.CENTER, p2);
		add(BorderLayout.SOUTH, p3);
		setMines(mines);
		setPowerUps(powerups);
		setNumber();
		setSize(width, height+75);
		startNewGame=false;
		
		//The frame wasn't displaying properly until it was resized, so now the code resizes it to fix that problem
		setSize(100, 100);
		setSize(width, height+75);
	}
	
	//Reveals the location of all mines and powerups when you lose or win
	public void revealAll(){
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x].reveal();
			}
		}
	}
	
	//We put all the listeners as nested classes so that they would have direct access to the methods and variables
	//This is for  right clicking for flags and such
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
	//TODO: Reset button
	//This handles when you click on any button or when you start a new game or open the highscore board
	class buttonListener implements ActionListener, Runnable{
		@Override
		
		public void run(){
			if(startNewGame)
				newGame();
			else
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
								if(squares[y][x].getFlags()>0){
									mines+=squares[y][x].getFlags();
									updateMineLabel();
									if(probes>0){
										probes--;
										probesLabel.setText("Probes: " + probes);
										if(type==1){
											shields++;
										}	
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
	
	//This handles all the buttons on the menu at the top
	class menuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==save){
				save();
				JOptionPane.showMessageDialog(null,"saved");
			}
			else if(e.getSource()==load){
				load();
			}
			else if(e.getSource()==exit){
				exit();
			}
			else if(e.getSource()==newGame){
				startNewGame=true;
				Thread t = new Thread(new buttonListener());
				t.start();
			}
			else if(e.getSource()==reset){
				reset();
			}
			else if(e.getSource()==highscore){
				if(highScoreLoad()==0){
					for(int i=0;i<10;i++){
						highScorers.add("empty");
						highScores.add(0);
					}
				}
				HighScoreBoard highScoreBoard=new HighScoreBoard(highScorers, highScores);
			}
			else if(e.getSource()==instructions){
				JOptionPane.showMessageDialog(null, "Welcome to Enhanced Minesweeper!\n"
						+ "This is much like regular minesweeper except for a few major differences.\n"
						+ "1) A space can have up to 3 mines in it\n"
						+ "2) There are 4 possible powerups\n"
						+ "3) There are lives\n"
						+ "\n"
						+ "Powerups:\n"
						+ "Shields: A shield will protect you from clicking on mines once\n"
						+ "Probes: A probe lets you check a spot to see if it is a mine. To use a probe, flag a space and then click on it\n"
						+ "Score: These give you extra points\n"
						+ "Invincibility: These are very rare and make you invincible for the rest of the game\n"
						+ "\n"
						+ "You have 3 lives and clicking on mines (even if there are multiple in one spot) removes 1 life.\n"
						+ "When you get to 0 lives, you lose.\n"
						+ "Highscore is calculated based on the number of times you clicked and the time it took (along with score powerups)\n"
						+ "Good luck and have fun!", "Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	//This is for the timer. It runs this every second that the timer is running
	class timerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(int y=0; y<squares.length; y++){
				for(int x=0; x<squares[y].length; x++){
					squares[y][x].updatePowerUpImage();
				}
			}
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
