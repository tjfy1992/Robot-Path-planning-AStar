import java.util.ArrayList;
import java.util.List;

public class State {
	//������λ��
	private Point robotLocation;
	
	//����,��ΪN(�����ƶ�һ��), S(�����ƶ�һ��), W(�����ƶ�һ��), E(�����ƶ�һ��)�Լ�C(����ҳ�)
	private String operation;
	
	//��ǰ�ڵ�ĸ��ڵ�, ���ڴﵽĿ�����л���
	private State previousState;
	
	//�ҳ����������list
	private List<Point> dirtList;
	
	//fvalueΪgvalue��hvalue�ĺ�
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

	//�����ж������ڵ��Ƿ���ͬ
	public static boolean isSameState(State state1, State state2){
		//��������λ�ò�ͬ,��ڵ㲻ͬ
		if(!Point.isSamePoint(state1.getRobotLocation(), state2.getRobotLocation()))
			return false;
		//���ҳ��б��Ȳ�ͬ, ��ڵ㲻ͬ
		else if(state1.getDirtList().size() != state2.getDirtList().size())
			return false;
		//��ǰ���߶���ͬ, ���ж�����state�еĻҳ��б��еĻҳ������Ƿ���ȫ��ͬ
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
		//������������������, �����ڵ���ͬ��
		return true;
	}
	
}
