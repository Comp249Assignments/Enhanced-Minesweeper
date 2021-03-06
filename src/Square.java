import javax.swing.*;

public class Square  extends JButton{
	private int mines, number, flags;
	private boolean clicked;
	private int powerup, powerUpDelay;
	private ImageIcon squareImage = new ImageIcon("square.png");
	private ImageIcon clickedSquareImage = new ImageIcon("square clicked.png");
	private ImageIcon shieldImage=new ImageIcon("shield.png"), probeImage=new ImageIcon("probe.png"),bonus=new ImageIcon("bonus.png") , superSquare=new ImageIcon("superSquare.png");
	//array of images for if there's 1, 2, or 3 mines
	private ImageIcon[] mineImages = {new ImageIcon("mine.png"), new ImageIcon("mine2.png"), new ImageIcon("mine3.png")};
	//similar array for number of flags
	private ImageIcon[] flagImages = {new ImageIcon("flag.png"), new ImageIcon("flag2.png"), new ImageIcon("flag3.png")};
	//array of all the images for the different numbers.
	private ImageIcon[] numberImages = {new ImageIcon("1.png"), new ImageIcon("2.png"), new ImageIcon("3.png"), new ImageIcon("4.png"), new ImageIcon("5.png"), new ImageIcon("6.png"), new ImageIcon("7.png"), new ImageIcon("8.png"), new ImageIcon("9.png"), new ImageIcon("10.png"), new ImageIcon("11.png"), new ImageIcon("12.png"), new ImageIcon("13.png"), new ImageIcon("14.png"), new ImageIcon("15.png"), new ImageIcon("16.png"), new ImageIcon("17.png"), new ImageIcon("18.png"), new ImageIcon("19.png"), new ImageIcon("20.png"), new ImageIcon("21.png"), new ImageIcon("22.png"), new ImageIcon("23.png"), new ImageIcon("24.png")};
	
	public Square(){
		mines=0;
		powerup=0;
		clicked=false;
		flags=0;
		powerUpDelay=0;
		setIcon(squareImage);
	}
	
	public void addMine(){
		mines++;
	}
	
	//The type of powerup is set based on a number that other methods translate into specific powerups
	public void addPowerUp(int type){
		powerup=type;
	}
	
	public void setNumber(int number){
		this.number = number;
	}
	
	public void setClicked(boolean clicked){
		this.clicked = clicked;
		if(!clicked){
			setIcon(squareImage);
		}
	}
	
	public int getMines(){
		return mines;
	}
	 
	public int hasPowerUp(){
		return powerup;
	}
	
	public int getNumber(){
		return number;
	}
	
	public boolean isClicked(){
		return clicked;
	}
	public int getFlags(){
		return flags;
	}
	
	//Adds a flag to the space
	public void flag(){
		if(flags==3){
			setIcon(squareImage);
			flags=0;
		}
		else{
			flags++;
			setIcon(flagImages[flags-1]);
		}
	}
	
	//Method that will be run when a space has been clicked. It will return an integer based on what type of space it is (1 for mine, 2 for space with adjacent mines,
	//3 for empty space, and 4 or higher for powerup and -1 for a space that's already been clicked)
	public int clicked(){
		if(!clicked)
		{
			clicked=true;
			if(mines>0){
				setIcon(mineImages[mines-1]);
				return 1;
			}
			else if(number>0){
				setIcon(numberImages[number-1]);
				return 2;
			}
			else if(powerup>0){
				if(powerup==9){
					setIcon(superSquare);
					powerUpDelay++;
				}
				else{
					powerUpDelay++;
					switch (powerup%3){
						case 0:
							setIcon(shieldImage);
							break;
						case 1:
							setIcon(probeImage);
							break;
						case 2:
							setIcon(bonus);
							break;
					}
				}
				return (powerup+4);
			}
			else{
				setIcon(clickedSquareImage);
				return 3;
			}
		}
		return -1;
	}
	
	//If an adjacent space that is blank is clicked, then this method will be run. If there are no mines here it will click the space
	public int adjacentClicked(){
		if(mines==0){
			return clicked();
		}
		return 2;
	}
	
	//method to reveal this space if it is a mine or powerup
	public void reveal(){
		if(mines>0){
			setIcon(mineImages[mines-1]);
		}
		else if(powerup>0){
			if(powerup==9){
				setIcon(superSquare);
			}
			else{
				switch (powerup%3){
					case 0:
						setIcon(shieldImage);
						break;
					case 1:
						setIcon(probeImage);
						break;
					case 2:
						setIcon(bonus);
						break;
				}
			}
		}
	}
	
	//If a space has a powerup on it, we want to reveal that it was a powerup (without any annoying popups) but still want to show the number if it has a number
	//So this method runs every second. If the powerup is revealed it'll update powerUpDelay. After 3 seconds it'll go back to the normal image instead of the powerup image
	public void updatePowerUpImage(){
		if(powerUpDelay==4){
			if(number>0)
				setIcon(numberImages[number-1]);
			else
				setIcon(clickedSquareImage);
			powerUpDelay=0;
		}
		else if(powerUpDelay>0){
			powerUpDelay++;
		}
	}
	
	//Method to reset a square
	public void reset(){
		mines=0;
		powerup=0;
		clicked=false;
		flags=0;
		powerUpDelay=0;
		setIcon(squareImage);
	}
}
