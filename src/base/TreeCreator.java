package base;

import treeTraining.TreeTrainer;
import dataManagement.DataReader;
import dataManagement.IData;

public class TreeCreator
{
	public static void main(String[] args)
	{
		String filePath = "";
		
		String[] testFiles = new String[] { "glass_mm.txt", "heart_mm.txt", "pima_indians_diabetes.txt" };
		
		if(args.length != 0)
		{
			filePath = args[0];
		}
		else
		{
			filePath = testFiles[2];
		}
		
		IData expData = DataReader.getInstance().getData(filePath);
		
		final int initialMinRecords = 2;
		final int finalMinRecords = 5;
		
		final int initialKValue = 1;
		final int finalKValue = 4;
		
		for(int k = initialKValue; k <= finalKValue; k++)
		{
			for(int i = initialMinRecords; i <= finalMinRecords; i ++)
			{
				TreeTrainer.getInstance().trainTree(expData, i, k);
			}
		}
	}
}
