package dataManagement.decisionTrees;

import dataManagement.IPartialData;

public class ObliqueTree implements IDecisionTree
{
	private Node root;
	private int minLevel;
	private int k;
	
	public ObliqueTree(int minLevel, int k)
	{
		root = null;
		this.minLevel = minLevel;
		this.k = k;
	}
	
	public void train(IPartialData data)
	{
		double[] coefs = this.calculateCoefficients(data);
		
		System.out.println(coefs[0]);
	}
	
	public double classify(double[] record)
	{
		//TODO no implementado
		return -5;
	}
	
	private double[] calculateCoefficients(IPartialData data)
	{
		double maxGain = 0;
		double bestContinuousValue = 0;
		int attrSelected = 0;
		
		for(int attrNum = 0; attrNum < data.getAttributes().length - 1; attrNum++)
		{
			double continuousValue = 0;
			IPartialData[] dividedData;
			
			continuousValue = data.getTopDividerAttribute(attrNum);
			dividedData = data.divideData(attrNum, continuousValue);

			double gain = data.getEntropy();
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
		
		double[] coefs = new double[data.getAttributes().length];
		coefs[attrSelected] = bestContinuousValue;
		
		return coefs;
	}
}
