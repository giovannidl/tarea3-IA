package treeTraining;

import java.util.List;

import utils.MathUtils;
import dataManagement.AttributeType;
import dataManagement.IData;
import dataManagement.IPartialData;
import dataManagement.decisionTrees.ClassicDecisionTree;
import dataManagement.decisionTrees.IDecisionTree;
import dataManagement.decisionTrees.ObliqueTree;

public class TreeTrainer
{
	public static final int NUM_CROSS_VALIDATION = 10;
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
	
	public void trainTree(IData data, int minRecords, int k, int restart_iteration)
	{
		List<Double> classesNames = data.getClassesNames();
		// matriz de confusion
		int[][] confMatrixMean = new int[classesNames.size()][classesNames.size()];
		
		for(int i = 0; i < NUM_CROSS_VALIDATION; i++)
		{
			// train
			data.setCurrentFold(i);
			IDecisionTree tree = new ObliqueTree(minRecords, k, restart_iteration);
			tree.train(data.getTrainingData());
			
			// test
			IPartialData testData = data.getTestData();
			int[][] confMatrixFold = this.testTree(testData, tree, classesNames);
			
			System.out.println(String.format("Fold %d. Accuracy: %.3f. #leaves: %d. max depth: %d", i+1, 
											(double)MathUtils.MatrixTrace(confMatrixFold) / testData.getLength(),
											tree.getNumLeaves(),
											tree.getMaxDepth()));
			
			MathUtils.MatrixAdd(confMatrixMean, confMatrixFold);
		}
		
		System.out.println(String.format("+Accuracy (avg.): %.3f", 
										(double)MathUtils.MatrixTrace(confMatrixMean) / data.getLength()));
		
		for(int i = 0; i < classesNames.size(); i++)
		{
			int TP = confMatrixMean[i][i];	// true positive
			int GT = MathUtils.MatrixSumColumn(confMatrixMean, i); // ground truth (TP + FN)
			double recall = (double)TP / GT; 
			
			System.out.println(String.format("+Recall Class %.0f: %.3f (%d/%d)", classesNames.get(i), recall, TP, GT));
			
			// puedo calcular accuracy/precision, sensitivity/specificity pero no quiero spammear la consola aun
		}
	}
	
	/**
	 * Retorna la matriz de confusion del arbol usando la data de testing.
	 * confMatrix[#real][#predicted]
	 */
	private int[][] testTree(IPartialData data, IDecisionTree tree, List<Double> classesNames)
	{
		int[][] confMatrix = new int[classesNames.size()][classesNames.size()];
		
		for(int recordPos = 0; recordPos < data.getLength(); recordPos++)
		{
			double[] record = data.getRecord(recordPos);
			double predictedClass = tree.classify(record);
			
			confMatrix[classesNames.indexOf(record[record.length - 1])][classesNames.indexOf(predictedClass)] += 1;
		}
		return confMatrix;
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
