package treeTraining;

import java.util.List;

import dataManagement.AttributeType;
import dataManagement.IData;
import dataManagement.IPartialData;
import dataManagement.decisionTrees.ClassicDecisionTree;
import dataManagement.decisionTrees.IDecisionTree;
import dataManagement.decisionTrees.ObliqueTree;

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
	
	public void trainTree(IData data, int minLevel, int k)
	{
		for(int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			data.setTestNumPart(i);
			IDecisionTree tree = new ObliqueTree(minLevel, k);
			tree.train(data.getTrainingData());
			
			this.testTree(data.getTestData(), tree);
		}
	}
	
	private IDecisionTree getObliqueTree(IPartialData data, int minLevel, int k)
	{
		
		return null;
	}
	
	private void testTree(IPartialData data, IDecisionTree tree)
	{
		//TODO hacer
	}
	
	//Unfinished
	private IDecisionTree getClassicTree(IPartialData data, int minLevel)
	{
		IDecisionTree tree = new ClassicDecisionTree();
		double maxGain = 0;
		double bestContinuousValue = 0;
		int attrSelected = 0;
		
		for(int attrNum = 0; attrNum < data.getAttributes().length - 1; attrNum++)
		{
			double gain = data.getEntropy();
			double continuousValue = 0;
			IPartialData[] dividedData;
			if(data.getAttributeType(attrNum) == AttributeType.DISCRETE)
			{
				dividedData = data.divideByDiscreteAttribute(attrNum);
			}
			else
			{
				continuousValue = data.getTopDividerAttribute(attrNum);
				dividedData = data.divideData(attrNum, continuousValue);
			}
			
			for(IPartialData partialData : dividedData)
			{
				gain -= (partialData.getLength() / data.getLength()) * partialData.getEntropy();
			}
			
			if(gain > maxGain)
			{
				maxGain = gain;
				bestContinuousValue = continuousValue;
				attrSelected = attrNum;
			}
		}
		
		if(data.getAttributeType(attrSelected) == AttributeType.DISCRETE)
		{
			Double[] attrValues = data.getDiscreteValues(attrSelected);
/*			int[] nodeNumbers = tree.addDiscreteNode(0, attrValues, attrSelected);
 * 			for(int i = 0; i < nodeNumbers.length; i++)
 * 			{
 * 				this.addNodes(dividedData[i], minLevel - 1, tree, nodeNumbers[i], new int[] { attrSelected });
 * 			}
*/			
		}
		else
		{
/*			int nodeNumbers = tree.addContinuousNode(0, bestContinuousValue, attrSelected);
 * 			for(int i = 0; i < nodeNumbers.length; i++)
 * 			{
 * 				this.addNodes(dividedData[i], minLevel - 1, tree, nodeNumbers[i], new int[] {attrSelected });
 * 			}
 */
		}
		//TODO hacer
		return null;
	}
	
	private void addNodes(IPartialData data, int minLevel, IDecisionTree tree, int nodeNum
			, List < Integer > expandedAttr)
	{
		//TODO hacer
	}
}