import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Robot {
	//行数
	public static int colomnNum;
	
	//列数
	public static int rowNum;
	
	//障碍物数量
	public static int obstacleNum;
	
	//用于存放state的优先级队列
	public static Queue<State> priorityQueue;
	
	//地图
	public static String[][] map;
	
	//灰尘坐标列表
	public static List<Point> dirtList;
	
	//closeList，用于存放已经存在的state
	public static List<State> closeList;
	
	//遍历总耗费
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
	    		//统计障碍物数量
	    		if(line.charAt(j) == '#')
	    		{	    			
	    			obstacleNum++;
	    		}
	    		
	    		//将灰尘格子坐标存入list中
	    		if(line.charAt(j) == '*')
	    		{
	    			dirtList.add(new Point(i, j));
	    		}
	    		
	    		//设置机器人初始坐标
	    		if(line.charAt(j) == '@')
	    		{
	    			initialState.setRobotLocation(new Point(i, j));
	    		}
	    		
	    		//初始化地图
	    		map[i][j] = line.charAt(j) + "";
	    	}
	    }
	    sc.close();
	    initialState.setDirtList(dirtList);
	    initialState.setCost(0);
	    initialState.setFvalue(0 + dirtList.size());
	    
	    //优先级队列的自定义Comparator,比较规则是Fvalue较小的state排在队列前面
	    Comparator<State> cmp = new Comparator<State>() {
	      public int compare(State s1, State s2) {
	        return s1.getFvalue() - s2.getFvalue();
	      }
	    };
	    
	    //初始化优先级队列
	    priorityQueue = new PriorityQueue<State>(5, cmp);
	    
	    closeList.add(initialState);
	    priorityQueue.add(initialState);
	    cost++;
	    
	    //遍历开始
	    while(!priorityQueue.isEmpty()){
	    	//State state = stack.pop();
	    	
	    	//取出队列中第一个state
	    	State state = priorityQueue.poll();
	    	
	    	//如果达到目标,输出结果并退出
	    	if(isgoal(state)){
	    		output(state);
	    		return;
	    	}
	    	calculate(state);
	    }
	}
	
	public static void calculate(State state){
		//获取当前机器人的坐标
		int x = state.getRobotLocation().getX();
		int y = state.getRobotLocation().getY();
		
		//如果当前的点是灰尘并且没有被清理
		if(map[x][y].equals("*") && !isCleared(new Point(x, y), state.getDirtList())){
			State newState = new State();
			List<Point> newdirtList = new ArrayList<Point>();
			//在新的state中,将灰尘列表更新,即去掉当前点的坐标
			for(Point point : state.getDirtList())
			{
				if(point.getX() == x && point.getY() == y)
					continue;
				else
					newdirtList.add(new Point(point.getX(), point.getY()));
			}
			newState.setDirtList(newdirtList);
			//Fvalue为gvalue和hvalue的和
			newState.setFvalue(state.getCost() + newdirtList.size());
			newState.setRobotLocation(new Point(x, y));
			//C代表Clean操作
			newState.setOperation("C");
			newState.setPreviousState(state);
			
			//若新产生的状态与任意一个遍历过的状态都不同,则进入队列
			if(!isDuplicated(newState)){
				priorityQueue.add(newState);
				closeList.add(newState);
				cost++;
			}
		}
		
		//若当前机器人坐标下方有格子并且不是障碍物
		if(x + 1 < rowNum)
		{
			if(!map[x+1][y].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x + 1, y));
				//S代表South,即向下方移动一个格子
				newState.setOperation("S");
				newState.setFvalue(state.getCost() + state.getDirtList().size());
				newState.setPreviousState(state);
				if(!isDuplicated(newState)){
					priorityQueue.add(newState);
					//加入到closeList中
					closeList.add(newState);
					cost++;
				}
			}
		}
		
		//若当前机器人坐标上方有格子并且不是障碍物
		if(x - 1 >= 0)
		{
			if(!map[x-1][y].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x - 1, y));
				//N代表North,即向上方移动一个格子
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
		
		//若当前机器人坐标左侧有格子并且不是障碍物
		if(y - 1 >= 0)
		{
			if(!map[x][y-1].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x, y - 1));
				//W代表West,即向左侧移动一个格子
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
		
		//若当前机器人坐标右侧有格子并且不是障碍物
		if(y + 1 < colomnNum)
		{
			if(!map[x][y+1].equals("#"))
			{
				State newState = new State();
				newState.setDirtList(state.getDirtList());
				newState.setRobotLocation(new Point(x, y + 1));
				//E代表East,即向右侧移动一个格子
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
	
	//判断是否已经达到目标,即当前遍历到的state中手否已经没有灰尘需要清理
	public static boolean isgoal(State state){
		if(state.getDirtList().isEmpty())
			return true;
		return false;
	}
	
	//输出,由最后一个state一步一步回溯到起始state
	public static void output(State state){
		String output = "";
		//回溯期间把每一个state的操作(由于直接输出的话是倒序)加入到output字符串之前,再输出output
		while(state != null){
			if(state.getOperation() != null)
				output = state.getOperation() + "\r\n" + output;
			state = state.getPreviousState();
		}
		System.out.println(output);
		//最后输出遍历过的节点(state)数量
		System.out.println(cost);
	}
	
	//判断节点是否存在,即将state与closeList中的state相比较,若都不相同则为全新节点
	public static boolean isDuplicated(State state){
		for(State state2 : closeList){
			if(State.isSameState(state, state2))
				return true;
		}
		return false;
	}
	
	//判断地图中当前位置的灰尘在这个state中是否已经被除去。
	public static boolean isCleared(Point point, List<Point> list){
		for(Point p : list){
			if(Point.isSamePoint(p, point))
				return false;
		}
		return true;
	}

}

