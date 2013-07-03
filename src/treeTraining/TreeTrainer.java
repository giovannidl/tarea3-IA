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
	
	public void trainTree(IData data, int minRecords, int k)
	{
		for(int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			data.setTestNumPart(i);
			IDecisionTree tree = new ObliqueTree(minRecords, k);
			tree.train(data.getTrainingData());
			
			this.testTree(data.getTestData(), tree);
		}
	}
	
	private void testTree(IPartialData data, IDecisionTree tree)
	{
		int correctClassified = 0;
		for(int recordPos = 0; recordPos < data.getLength(); recordPos++)
		{
			double[] record = data.getRecord(recordPos);
			double recordClass = tree.classify(record);
			if(recordClass == record[record.length - 1])
				correctClassified++;
		}
		
		double percentage = ((double) correctClassified / data.getLength()) * 100;
		
		System.out.println("El porcentaje de clasificaciones correctas fue " + percentage + "%");
		//TODO hacer
	}
	
	//Unfinished
	private IDecisionTree getClassicTree(IPartialData data, int minRecords)
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
				continuousValue = data.getBestPivotAttribute(attrNum);
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
