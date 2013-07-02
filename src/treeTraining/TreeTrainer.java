package treeTraining;

import java.util.List;

import dataManagement.IData;
import dataManagement.IPartialData;
import dataManagement.decisionTrees.IDecisionTree;

public class TreeTrainer
{
	private static int NUM_CROSS_VALIDATION = 10;
	private static TreeTrainer instance;
	
	private TreeTrainer()
	{	
	}
	
	public static TreeTrainer getInstance()
	{
		if(instance == null)
			instance = new TreeTrainer();
		
		return instance;
	}
	
	public void trainTree(IData data, int minLevel)
	{
		for(int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			data.setTestNumPart(i);
			IDecisionTree tree = this.getTree(data.getTrainingData(), minLevel);
			
			this.testTree(data.getTestData(), tree);
		}
	}
	
	private IDecisionTree getTree(IPartialData data, int minLevel)
	{
		
		//TODO hacer
		return null;
	}
	
	private void addNodes(IPartialData data, int minLevel, IDecisionTree tree, int nodeNum
			, List < Integer > expandedAttr)
	{
		//TODO hacer
	}
	
	private void testTree(IPartialData data, IDecisionTree tree)
	{
		//TODO hacer
	}
}
