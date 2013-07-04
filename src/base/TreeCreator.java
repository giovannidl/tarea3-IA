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
		
		final int initialMinRecords = 2;
		final int finalMinRecords = 5;
		
		int k = 2;
		
		final int restart_iteration = 2;
		
		if(args.length != 0)
		{
			filePath = args[0];
			k = Integer.parseInt(args[1]);
		}
		else
		{
			filePath = testFiles[2];
		}
		
		IData expData = DataReader.getInstance().getData(filePath);
		
		System.out.println("Data file: " + filePath);
		for(int i = initialMinRecords; i <= finalMinRecords; i ++)
		{
			System.out.println("\n** k = " + k + ", minRecords = " + i);
			TreeTrainer.getInstance().trainTree(expData, i, k, restart_iteration);
		}
	}
}
