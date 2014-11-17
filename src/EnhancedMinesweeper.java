import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class EnhancedMinesweeper extends JFrame{
	private Square squares[][];
	private int mines, lives, height, width;
	private boolean playing;
	Random random= new Random();
	
	//TO DO: fix up the GUI to have a score (will that be based on time? That's how classic minesweeper does it, so I say we should do that. And the bonus score thing will just
	//subtract some of the time) as well as the number of mines. Maybe also have a button to restart (and then we would also need a restart method). Also a counter for the
	//number of probes along with a button to use the probe, I guess? (or probed could be used by pushing a button and then clicking. Let me know if that's how you want to
	//implement it and I'll do it. It's really easy to do). Also need save and load buttons (or they could be in a tool bar at the top. That would make it look nicer for sure)
	
	//TO DO: Add save and load stuff
	
	//TO DO: Implement powerups
	
	public EnhancedMinesweeper(){
		int difficulty = 1;
		lives=3;
		playing = true;
		//TO DO: We should add in code to ask the user which difficulty they want and set the size of the grid accordingly. For now I'll just have it at 1, which is easy.
		//2 will be medium and 3 will be hard. If we have time we can also add in custom settings. Ideally this should also be a frame that we design, not just a JOptionPane
		//Alternatively difficulty could be managed through a toolbar at the top. Saving and loading could also work through the toolbar
		//I put the dimensions and number of mines based on the difficulty setting of the original minesweeper
		
		//Btw, in case you get mixed up (I always do) 2D arrays go array[row][column]
		if(difficulty==1){
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
		else{
			squares = new Square[16][30];
			mines = 99;
			height = 21*16;
			width = 18*30;
			setLayout(new GridLayout(16, 30));
		}
		//Initialize all the square objects and add them to the frame
		for(int y=0; y<squares.length; y++){
			for(int x=0; x<squares[y].length; x++){
				squares[y][x] = new Square();
				add(squares[y][x]);
			}
		}
		addMouseListener(new mouse());
		setMines(mines);
		setNumber();
		setSize(width, height);
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
	
	//for the gift
	public void setPowerUps(int powerups, int type){
		while (powerups>0){
			int y=random.nextInt(squares.length);
			int x=random.nextInt(squares[y].length);
			/*if(squares[x][y]!=0){
				continue;
			}*/
			//Will only add a powerup if there is no mine and no powerup there
			if(squares[y][x].getMines()==0 && !squares[y][x].hasPowerUp()){
				squares[y][x].addPowerUp(type);
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
	
	//If you click an empty space this will clear out all adjacent empty spaces
	public void clickAdjacent(int x, int y){
		//Checks spaces above current space
		if(y>0){
			if(squares[y-1][x].adjacentClicked()==3)
				clickAdjacent(x, y-1);
			if(x>0){
				if(squares[y-1][x-1].adjacentClicked()==3)
					clickAdjacent(x-1, y-1);
			}
			if(x<squares[y].length-1){
				if(squares[y-1][x+1].adjacentClicked()==3)
					clickAdjacent(x+1, y-1);
			}
		}
		//Checks spaces below
		if(y<squares.length-1){
			if(squares[y+1][x].adjacentClicked()==3)
				clickAdjacent(x, y+1);
			if(x>0){
				if(squares[y+1][x-1].adjacentClicked()==3)
					clickAdjacent(x-1, y+1);
			}
			if(x<squares[y].length-1){
				if(squares[y+1][x+1].adjacentClicked()==3)
					clickAdjacent(x+1, y+1);
			}
		}
		//Checks space to the left
		if(x>0){
			if(squares[y][x-1].adjacentClicked()==3)
				clickAdjacent(x-1, y);
		}
		//Checks space to the right
		if(x<squares[y].length-1){
			if(squares[y][x+1].adjacentClicked()==3)
				clickAdjacent(x+1, y);
		}
	}
	
	//TO DO: Allow the user to start a new game after this and the victory method as well (including the option to change the difficulty setting)
	//TO DO: When they lose reveal all the mines and rewards. So we also need 16 px by 16 px images for the powerups
	public void hitMine(){
		lives--;
		if(lives==0){
			JOptionPane.showMessageDialog(null, "GAME OVER\nYou have run out of lives");
			playing=false;
		}
	}
	
	//TO DO: Implement this method that will check if every space that is not a mine has been clicked and if so pops up a victory message and sets playing to false
	public void checkWin(){
		
	}
	
	//TO DO: Implement right clicking to flag something as having a mine (maybe also to flag something as having 2 or 3 mines. Idk how to check a right click. Probably isn't too complicated
	class mouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent m) {
			if(playing){
				//There's some empty space at the top and bottom, so the numbers I subtract fix that
				//TO DO: Test on other difficulties
				int x = (m.getX()-9)/16;
				int y = (m.getY()-35)/16;
				/*System.out.println(x + " " + m.getX());
				System.out.println(y + " " + m.getY());*/
				int type = squares[y][x].clicked();
				if(type==1){
					hitMine();
				}
				else if(type==3)
					clickAdjacent(x, y);
				checkWin();
			}
		}

		@Override
		public void mouseEntered(MouseEvent m) {
			
		}

		@Override
		public void mouseExited(MouseEvent m) {
			
		}

		@Override
		public void mousePressed(MouseEvent m) {
			
		}

		@Override
		public void mouseReleased(MouseEvent m) {
			
		}
	}
}
