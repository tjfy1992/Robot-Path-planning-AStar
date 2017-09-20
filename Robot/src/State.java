import java.util.ArrayList;
import java.util.List;

public class State {
	//机器人位置
	private Point robotLocation;
	
	//操作,分为N(向上移动一格), S(向下移动一格), W(向左移动一格), E(向右移动一格)以及C(清理灰尘)
	private String operation;
	
	//当前节点的父节点, 用于达到目标后进行回溯
	private State previousState;
	
	//灰尘所在坐标的list
	private List<Point> dirtList;
	
	//fvalue为gvalue和hvalue的和
	private int fvalue;
	
	//gvalue
	private int cost;

	public Point getRobotLocation() {
		return robotLocation;
	}

	public void setRobotLocation(Point robotLocation) {
		this.robotLocation = robotLocation;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public State getPreviousState() {
		return previousState;
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public List<Point> getDirtList() {
		return dirtList;
	}

	public void setDirtList(List<Point> dirtList) {
		this.dirtList = new ArrayList<Point>();
		for(Point point : dirtList){
			this.dirtList.add(point);
		}
	}
	
	public int getFvalue() {
		return fvalue;
	}

	public void setFvalue(int fvalue) {
		this.fvalue = fvalue;
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	//用于判断两个节点是否相同
	public static boolean isSameState(State state1, State state2){
		//若机器人位置不同,则节点不同
		if(!Point.isSamePoint(state1.getRobotLocation(), state2.getRobotLocation()))
			return false;
		//若灰尘列表长度不同, 则节点不同
		else if(state1.getDirtList().size() != state2.getDirtList().size())
			return false;
		//若前两者都相同, 则判断两个state中的灰尘列表中的灰尘坐标是否完全相同
		else{
			for(Point point : state1.getDirtList())
			{
				boolean same = false;
				for(Point point2 : state2.getDirtList())
				{
					if(Point.isSamePoint(point, point2))
						same = true;
				}
				if(!same)
					return false;
			}
		}
		//若满足上述所有条件, 则两节点相同。
		return true;
	}
	
}
