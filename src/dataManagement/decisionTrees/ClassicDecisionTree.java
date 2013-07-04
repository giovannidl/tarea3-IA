package dataManagement.decisionTrees;

import java.util.ArrayList;
import java.util.List;

import dataManagement.IPartialData;

public class ClassicDecisionTree implements IDecisionTree
{
	List < Node > nodes;
	
	public ClassicDecisionTree()
	{
		this.nodes = new ArrayList < Node >();
	}
	
	public int addContinuousNode(int parentNodeNum, double separatorValue, int attrNum)
	{
		//TODO unimplemented
		return 0;
	}
	
	public int addDiscreteNode(int parentNodeNum, double[] attrValues, int attrNum)
	{
		//TODO unimplemented
		return 0;
	}
	
	public int setContinuousRootNode(double separatorValue, int attrNum)
	{
		//TODO unimplemented
		return 0;
	}
	
	public int setDiscreteRootNode(double[] attrValues, int attrNum)
	{
		//TODO unimplemented
		return 0;
	}

	@Override
	public void train(IPartialData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double classify(double[] record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumLeaves()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxDepth()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
