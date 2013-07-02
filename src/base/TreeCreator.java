package base;

import treeTraining.TreeTrainer;
import dataManagement.DataReader;
import dataManagement.IData;

public class TreeCreator
{
	public static void main(String[] args)
	{
		String filePath = "";
		
		if(args.length != 0)
		{
			filePath = args[0];
		}
		else
		{
			filePath = "glass_mm.txt";
		}
		
		IData expData = DataReader.getInstance().getData(filePath);
		
		final int initialMinLevel = 2;
		final int finalMinLevel = 5;
		
		for(int i = initialMinLevel; i <= finalMinLevel; i ++)
			TreeTrainer.getInstance().trainTree(expData, i);
	}
}
