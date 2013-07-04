package dataManagement.decisionTrees;

import dataManagement.IPartialData;

public interface IDecisionTree
{
	void train(IPartialData data);
	
	double classify(double[] record);
	
	int getNumLeaves();
	
	int getMaxDepth();
}
