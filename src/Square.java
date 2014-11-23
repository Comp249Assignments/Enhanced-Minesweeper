import javax.swing.*;

public class Square  extends JButton{
	private int mines, number, flags;
	private boolean clicked,flagged;
	private int powerup;
	private ImageIcon squareImage = new ImageIcon("square.png");
	private ImageIcon clickedSquareImage = new ImageIcon("square clicked.png");
	private ImageIcon[] mineImages = {new ImageIcon("mine.png"), new ImageIcon("mine2.png"), new ImageIcon("mine3.png")};
	private ImageIcon[] flagImages = {new ImageIcon("flag.png"), new ImageIcon("flag2.png"), new ImageIcon("flag3.png")};
	
	//array of all the images for the different numbers. TO DO: Someone please make all these images and have the right dimensions for them (16 px by 16 px)
	private ImageIcon[] numberImages = {new ImageIcon("1.png"), new ImageIcon("2.png"), new ImageIcon("3.png"), new ImageIcon("4.png"), new ImageIcon("5.png"), new ImageIcon("6.png"), new ImageIcon("7.png"), new ImageIcon("8.png"), new ImageIcon("9.png"), new ImageIcon("10.png"), new ImageIcon("11.png"), new ImageIcon("12.png"), new ImageIcon("13.png"), new ImageIcon("14.png"), new ImageIcon("15.png"), new ImageIcon("16.png"), new ImageIcon("17.png"), new ImageIcon("18.png"), new ImageIcon("19.png"), new ImageIcon("20.png"), new ImageIcon("21.png"), new ImageIcon("22.png"), new ImageIcon("23.png"), new ImageIcon("24.png")};
	
	public Square(){
		mines=0;
		powerup=0;
		clicked=false;
		flagged=false;
		flags=0;
		//setFont(new Font("Arial", Font.PLAIN, 5));
		setIcon(squareImage);
	}
	
	public void addMine(){
		mines++;
	}
	
	//TO DO: Implement this method. The integer received will correspond to a type of powerup and one of that powerup will be added
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
			//setText(null);
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
	
	public boolean getFlagged(){
		return flagged;
	}
	
	public void flag(){
		if(flags==3){
			setIcon(squareImage);
			flagged=false;
			flags=0;
		}
		else{
			flags++;
			flagged=true;
			setIcon(flagImages[flags-1]);
		}
		/*flagged=!flagged;
		if(flagged)
			setIcon(flagImage);
		else
			setIcon(squareImage);*/
	}
	
	//Method that will be run when a space has been clicked. It will return an integer based on what type of space it is (1 for mine, 2 for space with adjacent mines,
	//3 for empty space, and 4 for a space that's already been clicked)
	//TO DO: Might need to add more cases for powerups and such
	public int clicked(){
		if(!clicked)
		{
			clicked=true;
			if(mines>0){
				setIcon(mineImages[mines-1]);
				return 1;
			}
			else if(powerup>0){
				
				return (powerup+4);
			}
			else if(number>0){
				setIcon(numberImages[number-1]);
				//For now I just set text showing the number, but it doesn't look as good as if we put images
				//setIcon(null);
				//setText(number+"");
				return 2;
			}
			else{
				setIcon(clickedSquareImage);
				//setBorderPainted(false);
				return 3;
			}
		}
		return -1;
	}
	
	
	public int adjacentClicked(){
		if(mines==0 && powerup==0){
			return clicked();
		}
		return 4;
	}
}
