import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AISearch 
{
	public static void main(String args[]) throws IOException
	{
		Map<Integer, int[]> actions = new HashMap<>();
		initialActions(actions);
		int[] size = new int[3];
		int[] startPoint = new int[3];
		int[] endPoint = new int[3];
		Map<String, int[]> pointActions = new HashMap<>();
		String searchName = readFile(size, startPoint, endPoint, pointActions);
		Result result = null;
		if (searchName.equals("BFS"))
		{
			result = BFS(size, startPoint, endPoint, pointActions, actions);
		}
		if (searchName.equals("UCS"))
		{
			result = UCS(size, startPoint, endPoint, pointActions, actions);
		}
		if (searchName.equals("A*"))
		{
			result = A(size, startPoint, endPoint, pointActions, actions);
		}
		outPutData(result);
    }
	private static Result BFS(int[] size, int[] startPoint, int[] endPoint, Map<String, int[]> pointActions, Map<Integer, int[]> actions)
	{
		if (!isBounded(size, startPoint))
		{
			return null;
		}
		if (!isBounded(size, endPoint))
		{
			return null;
		}
		String start = arrayToString(startPoint);
		String end = arrayToString(endPoint);
		Node startNode = new Node(0, " ");
		if (start.equals(end))
		{
			return new Result(0, 1, new ArrayList<String>());
		}
		Set<String> visited = new HashSet<>();
		Queue<int[]> q = new LinkedList<>();
		Map<String, Node> path = new HashMap<>();
		q.offer(startPoint);
		visited.add(start);
		path.put(start, startNode);
		while(!q.isEmpty())
		{
			int qSize = q.size();
			for(int i = 0; i < qSize; i++)
			{
				int[] curPoint = q.poll();
				String curStr = arrayToString(curPoint);
				int[] actionsCode = pointActions.get(curStr);
				if (actionsCode == null)
				{
					continue;
				}
				for(int j = 0; j < actionsCode.length; j++)
				{
					int newPoint[] = new int[3];
					int[] newAction = actions.get(actionsCode[j]);
					for(int k = 0; k < 3; k++)
					{
						newPoint[k] = curPoint[k] + newAction[k];
					}
					if (!isBounded(size, newPoint))
					{
						continue;
					}
					String newStr = arrayToString(newPoint);
					if (visited.contains(newStr))
					{
						continue;
					}
					Node node = new Node(1, curStr);
					path.put(newStr, node);
					if (newStr.equals(end))
					{
						return processResult(path, newStr);
					}
					q.offer(newPoint);
					visited.add(newStr);
				}
			}
		}
		return null;
	}
	private static Result UCS(int[] size, int[] startPoint, int[] endPoint, Map<String, int[]> pointActions, Map<Integer, int[]> actions)
	{
		if (!isBounded(size, startPoint))
		{
			return null;
		}
		if (!isBounded(size, endPoint))
		{
			return null;
		}
		String start = arrayToString(startPoint);
		String end = arrayToString(endPoint);
		Node startNode = new Node(0, " ");
		Map<String, Integer> map = new HashMap<>();
		PriorityQueue<String> pq = new PriorityQueue<>((s1, s2) -> map.get(s1) - map.get(s2));
		Set<String> visited = new HashSet<>();
		Map<String, Node> path = new HashMap<>();
		pq.offer(start);
		visited.add(start);
		map.put(start, 0);
		path.put(start, startNode);
		while(!pq.isEmpty())
		{
			String curStr = pq.poll();
			int cost = map.get(curStr);
			if (curStr.equals(end))
			{
				return processResult(path, curStr);
			}
			int[] point = StringToArray(curStr);
			int[] actionsCode = pointActions.get(curStr);
			if (actionsCode == null)
			{
				continue;
			}
			for(int j = 0; j < actionsCode.length; j++)
			{
				int newPoint[] = new int[3];
				int[] newAction = actions.get(actionsCode[j]);
				for(int k = 0; k < 3; k++)
				{
					newPoint[k] = point[k] + newAction[k];
				}
				if (!isBounded(size, newPoint))
				{
					continue;
				}
				String newStr = arrayToString(newPoint);
				if (visited.contains(newStr))
				{
					continue;
				}
				int newCost = 0;
				int curCost;
				if (actionsCode[j] > 6)
				{
					curCost = 14;
				}
				else
				{
					curCost = 10;
				}
				newCost = cost + curCost;
				Node node = new Node(curCost, curStr);
				if (map.get(newStr) != null && map.get(newStr) > newCost)
				{
					map.remove(newStr);
					pq.remove(newStr);
					map.put(newStr, newCost);
					pq.offer(newStr);
					path.put(newStr, node);
					continue;
				}
				if (map.get(newStr) != null && map.get(newStr) <= newCost)
				{
					continue;
				}
				map.put(newStr, newCost);
				pq.offer(newStr);
				path.put(newStr, node);
			}
			pq.remove(curStr);
			map.remove(curStr);
			visited.add(curStr);
		}
		return null;
	}
	private static Result A(int[] size, int[] startPoint, int[] endPoint, Map<String, int[]> pointActions, Map<Integer, int[]> actions)
	{
		if (!isBounded(size, startPoint))
		{
			return null;
		}
		if (!isBounded(size, endPoint))
		{
			return null;
		}
		String start = arrayToString(startPoint);
		String end = arrayToString(endPoint);
		Node startNode = new Node(0, " ");
		int futureCost = distance(startPoint, endPoint);
		Map<String, Integer> map = new HashMap<>();
		PriorityQueue<String> pq = new PriorityQueue<>((s1, s2) -> map.get(s1) - map.get(s2));
		Set<String> visited = new HashSet<>();
		Map<String, Node> path = new HashMap<>();
		pq.offer(start);
		visited.add(start);
		map.put(start, futureCost);
		path.put(start, startNode);
		while(!pq.isEmpty())
		{
			String curStr = pq.poll();
			int cost = map.get(curStr);
			if (curStr.equals(end))
			{
				return processResult(path, curStr);
			}
			int[] point = StringToArray(curStr);
			int[] actionsCode = pointActions.get(curStr);
			if (actionsCode == null)
			{
				continue;
			}
			for(int j = 0; j < actionsCode.length; j++)
			{
				int newPoint[] = new int[3];
				int[] newAction = actions.get(actionsCode[j]);
				for(int k = 0; k < 3; k++)
				{
					newPoint[k] = point[k] + newAction[k];
				}
				if (!isBounded(size, newPoint))
				{
					continue;
				}
				String newStr = arrayToString(newPoint);
				if (visited.contains(newStr))
				{
					continue;
				}
				int newCost = 0;
				int curCost;
				if (actionsCode[j] > 6)
				{
					curCost = 14;
				}
				else
				{
					curCost = 10;
				}
				futureCost = distance(newPoint, endPoint);
				newCost = cost + curCost + futureCost;
				Node node = new Node(curCost, curStr);
				if (map.get(newStr) != null && map.get(newStr) > newCost)
				{
					map.remove(newStr);
					pq.remove(newStr);
					map.put(newStr, newCost);
					pq.offer(newStr);
					path.put(newStr, node);
					continue;
				}
				if (map.get(newStr) != null && map.get(newStr) <= newCost)
				{
					continue;
				}
				map.put(newStr, newCost);
				pq.offer(newStr);
				path.put(newStr, node);
			}
			pq.remove(curStr);
			map.remove(curStr);
			visited.add(curStr);
		}
		return null;
	}
	private static String readFile(int[] size, int[] startPoint, int[] endPoint, Map<String, int[]> pointActions)
	{
		try (FileReader reader = new FileReader("input"
				+ ".txt");
	             BufferedReader br = new BufferedReader(reader)
	        ) 
	        {
	            String searchName = br.readLine();
	            processArray(br.readLine(), size);
	            processArray(br.readLine(), startPoint);
	            processArray(br.readLine(), endPoint);
	            br.readLine();
	            String line;
	            while ((line = br.readLine()) != null) 
	            {
	            	processMap(line, pointActions);
	            }   
	            return searchName;
	        } catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
		return null;
	}
	private static void outPutData(Result result) throws IOException
	{
		File file = new File("output.txt");
		if(!file.exists())
		{
			file.createNewFile();
		}
		FileWriter fw = new FileWriter("output.txt", true);
		try (PrintWriter pw = new PrintWriter(fw)) 
		{
			if (result == null)
			{
				pw.print("FAIL");
				pw.flush();
				return;
			}
			pw.println(result.cost);
			pw.flush();
			pw.println(result.step);
			pw.flush();
			ArrayList<String> arr = result.arr;
			int arrSize = arr.size();
			for(int i = arrSize - 1; i > 0; i--)
			{
				pw.println(arr.get(i));
				pw.flush();
			}
			pw.print(arr.get(0));
			pw.flush();
		}
	}
	private static Result processResult(Map<String, Node> path, String str)
	{
		int cost = 0;
		ArrayList<String> arr = new ArrayList<>();
		Node node = path.get(str);
		while(path.get(str) != null)
		{
			String newStr = str + node.cost;
			arr.add(newStr);
			cost = cost + node.cost;
			str = node.parent;
			node = path.get(str);
			
		}
		return new Result(cost, arr.size(), arr);
	}
	private static void processArray(String str, int[] arr)
	{
		String[] s = str.split(" ");
		for(int i = 0; i < arr.length; i++)
		{
			arr[i] = Integer.parseInt(s[i]);
		}
	}
	private static void processMap(String str, Map<String, int[]> pointActions)
	{
		String[] s = str.split(" ");
		StringBuilder ss = new StringBuilder();
		for(int i = 0; i < 3; i++)
		{
			ss.append(s[i]);
			ss.append(" ");
		}
		if (s.length == 3)
		{
			pointActions.put(ss.toString(), null);
		}
		int[] arr = new int[s.length - 3];
		for(int i = 3; i < s.length; i++)
		{
			arr[i - 3] = Integer.parseInt(s[i]);
		}
		pointActions.put(ss.toString(), arr);
	}
	private static String arrayToString(int[] arr)
	{
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < 3; i++)
		{
			s.append(arr[i]);
			s.append(" ");
		}
		return s.toString();
	}
	private static int[] StringToArray(String str)
	{
		String[] s = str.split(" ");
		int[] ans = new int[3];
		for(int i = 0; i < 3; i++)
		{
			ans[i] = Integer.parseInt(s[i]);
		}
		return ans;
 	}
	private static int distance(int[] startPoint, int[] endPoint)
	{
		int ans = 0;
		for(int i = 0; i < 3; i++)
		{
			int num = endPoint[i] - startPoint[i];
			ans = ans + num * num; 
		}
		return (int) Math.sqrt(ans);
	}
	private static boolean isBounded(int[] size, int[] point)
	{
		if (point[0] >= 0 && point[0] < size[0])
		{
			if (point[1] >= 0 && point[1] < size[1])
			{
				if (point[2] >= 0 && point[2] < size[2])
				{
					return true;
				}
			}
		}
		return false;
	}
 	private static void initialActions(Map<Integer, int[]> actions)
	{
		actions.put(1, new int[]{1, 0, 0});
		actions.put(2, new int[]{-1, 0, 0});
		actions.put(3, new int[]{0, 1, 0});
		actions.put(4, new int[]{0, -1, 0});
		actions.put(5, new int[]{0, 0, 1});
		actions.put(6, new int[]{0, 0, -1});
		actions.put(7, new int[]{1, 1, 0});
		actions.put(8, new int[]{1, -1, 0});
		actions.put(9, new int[]{-1, 1, 0});
		actions.put(10, new int[]{-1, -1, 0});
		actions.put(11, new int[]{1, 0, 1});
		actions.put(12, new int[]{1, 0, -1});
		actions.put(13, new int[]{-1, 0, 1});
		actions.put(14, new int[]{-1, 0, -1});
		actions.put(15, new int[]{0, 1, 1});
		actions.put(16, new int[]{0, 1, -1});
		actions.put(17, new int[]{0, -1, 1});
		actions.put(18, new int[]{0, -1, -1});
	}
}
class Result
{
	public int cost;
	public int step;
	public ArrayList<String> arr;
	public Result(int cost, int step, ArrayList<String> arr)
	{
		this.cost = cost;
		this.step = step;
		this.arr = arr;
	}
}
class Node
{
	public int cost;
	public String parent;
	public Node(int cost, String parent)
	{
		this.cost = cost;
		this.parent = parent;
	}
}
