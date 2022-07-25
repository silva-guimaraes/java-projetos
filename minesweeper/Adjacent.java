public class Adjacent {
    public int top, bottom, back, front;

    Adjacent(Field field){
	generateAdj(field.x, field.y);
    }
    Adjacent(int  x, int y){
	generateAdj(x, y);
    }

    private void generateAdj(int x, int y){
	final int lengthX = Settings.BOARD_SIZE_X - 1;
	final int lengthY = Settings.BOARD_SIZE_Y - 1;

	this.back = ( x > 0) ? x - 1 : x;	 this.front = ( x < lengthX) ? x + 1 : x;

	this.top = ( y > 0) ? y - 1 : y;	 this.bottom = ( y < lengthY) ? y + 1 : y; 
    }
} 
