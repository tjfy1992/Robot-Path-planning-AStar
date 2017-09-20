
public class Point {
	private int X;
	private int Y;
	
	public Point(int x, int y){
		this.X = x;
		this.Y = y;
	}
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	
	//判断两个点是否坐标相同
	public static boolean isSamePoint(Point point1, Point point2){
		if(point1.getX() == point2.getX() && point1.getY() == point2.getY())
			return true;
		return false;
	}
	
}
