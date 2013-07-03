package dataManagement.decisionTrees;

import dataManagement.IPartialData;

public class ObliqueTree implements IDecisionTree
{
	private final double EPSILON = 0.0000000001; 
	private Node root;
	private int minRecords;
	private int k;
	
	public ObliqueTree(int minRecords, int k)
	{
		root = null;
		this.minRecords = minRecords;
		this.k = k;
	}
	
	public void train(IPartialData data)
	{
		double[] coefs = this.calculateCoefficients(data);
		
		this.root = new Node(coefs);
		IPartialData[] dividedData = data.divideData(coefs);
		this.root.setLeftChild(new Node());
		this.root.setRightChild(new Node());
		
		this.trainNode(this.root.getLeftChild(), dividedData[0]);
		this.trainNode(this.root.getRightChild(), dividedData[1]);
	}
	
	public void trainNode(Node node, IPartialData data)
	{
		if(data.getLength() < minRecords || data.isSingleClass())
		{
			node.setLeaf(true);
			node.setLeafClass(data.getMayorityClass());
		}
		else
		{
			double[] coefs = this.calculateCoefficients(data);
			node.setCoefficients(coefs);
			IPartialData[] dividedData = data.divideData(coefs);
			node.setLeftChild(new Node());
			node.setRightChild(new Node());
			
			this.trainNode(node.getLeftChild(), dividedData[0]);
			this.trainNode(node.getRightChild(), dividedData[1]);
		}
	}
	
	public double classify(double[] record)
	{
		Node auxNode = this.root;
		while(!auxNode.isLeaf())//TODO no implementado
		{
			double result = 0;
			//El último atributo es la clase a la que corresponde
			for(int attrPos = 0; attrPos < record.length - 1; attrPos++)
			{
				result += auxNode.getCoefs()[attrPos] * record[attrPos];
			}
						
			//El último coeficiente corresponde al coeficiente libre
			result += auxNode.getCoefs()[auxNode.getCoefs().length - 1];
			
			//Bias a la derecha cuando hay errores de redondeo
			result += EPSILON;
			
			if(result < 0)
				auxNode = auxNode.getLeftChild();
			else
				auxNode = auxNode.getRightChild();
		}
		return auxNode.getLeafClass();
	}
	
	private double[] calculateCoefficients(IPartialData data)
	{
		double maxGain = 0;
		double bestContinuousValue = 0;
		int attrSelected = 0;
		
		//No ocupamos el último atributo porque es la clase del registro.
		for(int attrNum = 0; attrNum < data.getAttributes().length - 1; attrNum++)
		{
			double continuousValue = 0;
			IPartialData[] dividedData;
			
			continuousValue = data.getBestPivotAttribute(attrNum);
			dividedData = data.divideData(attrNum, continuousValue);

			double gain = data.getEntropy();
			for(IPartialData partialData : dividedData)
			{
				gain -= ((double)partialData.getLength() / data.getLength()) * partialData.getEntropy();
			}
			
			if(gain > maxGain)
			{
				maxGain = gain;
				bestContinuousValue = continuousValue;
				attrSelected = attrNum;
			}
		}
		
		double[] coefs = new double[data.getAttributes().length];
		coefs[attrSelected] = (double) 1 / bestContinuousValue;
		
		//Como el último atributo es la clase, ocuparemos el coeficiente como el
		//coeficiente libre
		coefs[data.getAttributes().length - 1] = -1;
		
		return coefs;
	}
}
