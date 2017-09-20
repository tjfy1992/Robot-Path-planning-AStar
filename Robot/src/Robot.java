import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Robot {
	//����
	public static int colomnNum;
	
	//����
	public static int rowNum;
	
	//�ϰ�������
	public static int obstacleNum;
	
	//���ڴ��state�����ȼ�����
	public static Queue<State> priorityQueue;
	
	//��ͼ
	public static String[][] map;
	
	//�ҳ������б�
	public static List<Point> dirtList;
	
	//closeList�����ڴ���Ѿ����ڵ�state
	public static List<State> closeList;
	
	//�����ܺķ�
	public static int cost = 0;
	
	public static void main(String[] args) {
		State initialState = new State();
		Scanner sc = new Scanner(System.in);   
		System.out.println("Please Enter Row Number:");
	    rowNum = sc.nextInt();
	    System.out.println("Please Enter Colomn Number:"); 
	    colomnNum = sc.nextInt();
	    map = new String[rowNum][colomnNum];
	    dirtList = new ArrayList<Point>();
	    closeList = new ArrayList<State>();
	    sc.nextLine();
	    for(int i=0; i<rowNum; i++)
	    {
	    	System.out.println("Please Enter the Elements in row " + (i + 1) + ":"); 
	    	String line = sc.nextLine();
	    	for(int j=0; j<colomnNum; j++)
	    	{
	    		//ͳ���ϰ�������
	    		if(line.charAt(j) == '#')
	    		{	    			
	    			obstacleNum++;
	    		}
	    		
	    		//���ҳ������������list��
	    		if(line.charAt(j) == '*')
	    		{
	    			dirtList.add(new Point(i, j));
	    		}
	    		
	    		//���û����˳�ʼ����
	    		if(line.charAt(j) == '@')
	    		{
	    			initialState.setRobotLocation(new Point(i, j));
	    		}
	    		
	    		//��ʼ����ͼ
	    		map[i][j] = line.charAt(j) + "";
	    	}
	    }
	    sc.close();
	    initialState.setDirtList(dirtList);
	    initialState.setCost(0);
	    initialState.setFvalue(0 + dirtList.size());
	    
	    //���ȼ����е��Զ���Comparator,�ȽϹ�����Fvalue��С��state���ڶ���ǰ��
	    Comparator<State> cmp = new Comparator<State>() {
	      public int compare(State s1, State s2) {
	        return s1.getFvalue() - s2.getFvalue();
	      }
	    };
	    
	    //��ʼ�����ȼ�����
	    priorityQueue = new PriorityQueue<State>(5, cmp);
	    
	    closeList.add(initialState);
	    priorityQueue.add(initialState);
	    cost++;
	    
	    //������ʼ
	    while(!priorityQueue.isEmpty()){
	    	//State state = stack.pop();
	    	
	    	//ȡ�������е�һ��state
	    	State state = priorityQueue.poll();
	    	
	    	//����ﵽĿ��,���������˳�
	    	if(isgoal(state)){
	    		output(state);
	    		return;
	    	}
	    	calculate(state);
	    }
	}
	
	public static void calculate(State state){
		//��ȡ��ǰ�����˵�����
		int x = state.getRobotLocation().getX();
		int y = state.getRobotLocation().getY();
		
		//�����ǰ�ĵ��ǻҳ�����û�б�����
		if(map[x][y].equals("*") && !isCleared(new Point(x, y), state.getDirtList())){
			State newState = new State();
			List<Point> newdirtList = new ArrayList<Point>();
			//���µ�state��,���ҳ��б����,��ȥ����ǰ�������
			for(Point point : state.getDirtList())
			{
				if(point.getX() == x && point.getY() == y)
					continue;
				else
					newdirtList.add(new Point(point.getX(), point.getY()));
			}
			newState.setDirtList(newdirtList);
			//FvalueΪgvalue��hvalue�ĺ�
			newState.setFvalue(state.getCost() + newdirtList.size());
			newState.setRobotLocation(new Point(x, y));
			//C����Clean����
			newState.setOperation("C");
			newState.setPreviousState(state);
			
			//���²�����״̬������һ����������״̬����ͬ,��������
			if(!isDuplicated(newState)){
				priorityQueue.add(newState);
				closeList.add(newState);
				cost++;
			}
		}
		
		//����ǰ�����������·��и��Ӳ��Ҳ����ϰ���
		if(x + 1 < rowNum)
		{
			if(!map[x+1][y].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x + 1, y));
				//S����South,�����·��ƶ�һ������
				newState.setOperation("S");
				newState.setFvalue(state.getCost() + state.getDirtList().size());
				newState.setPreviousState(state);
				if(!isDuplicated(newState)){
					priorityQueue.add(newState);
					//���뵽closeList��
					closeList.add(newState);
					cost++;
				}
			}
		}
		
		//����ǰ�����������Ϸ��и��Ӳ��Ҳ����ϰ���
		if(x - 1 >= 0)
		{
			if(!map[x-1][y].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x - 1, y));
				//N����North,�����Ϸ��ƶ�һ������
				newState.setOperation("N");
				newState.setFvalue(state.getCost() + state.getDirtList().size());
				newState.setPreviousState(state);
				if(!isDuplicated(newState)){
					priorityQueue.add(newState);
					closeList.add(newState);
					cost++;
				}
			}
		}
		
		//����ǰ��������������и��Ӳ��Ҳ����ϰ���
		if(y - 1 >= 0)
		{
			if(!map[x][y-1].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x, y - 1));
				//W����West,��������ƶ�һ������
				newState.setOperation("W");
				newState.setFvalue(state.getCost() + state.getDirtList().size());
				newState.setPreviousState(state);
				if(!isDuplicated(newState)){
					priorityQueue.add(newState);
					closeList.add(newState);
					cost++;
				}
			}
		}
		
		//����ǰ�����������Ҳ��и��Ӳ��Ҳ����ϰ���
		if(y + 1 < colomnNum)
		{
			if(!map[x][y+1].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x, y + 1));
				//E����East,�����Ҳ��ƶ�һ������
				newState.setOperation("E");
				newState.setFvalue(state.getCost() + state.getDirtList().size());
				newState.setPreviousState(state);
				if(!isDuplicated(newState)){
					priorityQueue.add(newState);
					closeList.add(newState);
					cost++;
				}
			}	
		}
		
		
	}
	
	//�ж��Ƿ��Ѿ��ﵽĿ��,����ǰ��������state���ַ��Ѿ�û�лҳ���Ҫ����
	public static boolean isgoal(State state){
		if(state.getDirtList().isEmpty())
			return true;
		return false;
	}
	
	//���,�����һ��stateһ��һ�����ݵ���ʼstate
	public static void output(State state){
		String output = "";
		//�����ڼ��ÿһ��state�Ĳ���(����ֱ������Ļ��ǵ���)���뵽output�ַ���֮ǰ,�����output
		while(state != null){
			if(state.getOperation() != null)
				output = state.getOperation() + "\r\n" + output;
			state = state.getPreviousState();
		}
		System.out.println(output);
		//�������������Ľڵ�(state)����
		System.out.println(cost);
	}
	
	//�жϽڵ��Ƿ����,����state��closeList�е�state��Ƚ�,��������ͬ��Ϊȫ�½ڵ�
	public static boolean isDuplicated(State state){
		for(State state2 : closeList){
			if(State.isSameState(state, state2))
				return true;
		}
		return false;
	}
	
	//�жϵ�ͼ�е�ǰλ�õĻҳ������state���Ƿ��Ѿ�����ȥ��
	public static boolean isCleared(Point point, List<Point> list){
		for(Point p : list){
			if(Point.isSamePoint(p, point))
				return false;
		}
		return true;
	}

}

