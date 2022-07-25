
class FieldEvent 
{ 
    public final int x; 
    public final int y; 
    public final int orderNumber; 
    public final boolean isMinePresent;

    FieldEvent(Field field) 
    {
	this.x = field.x;	 this.y = field.y;
	isMinePresent = field.hasMine;
	this.orderNumber = field.orderNumber;
    } 
    public int getY() {
	return y;
    } 
    public int getX() {
	return x;
    }
    public String toString(){
	return (Integer.toString(x) + ", " + Integer.toString(y)); 
    }
}
