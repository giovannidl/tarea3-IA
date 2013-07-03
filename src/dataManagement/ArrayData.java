package dataManagement;

import java.util.ArrayList;
import java.util.List;

public class ArrayData implements IData
{
	private static int NUM_CROSS_VALIDATION = 10;
	
	private double[][] data;
	
	private int testPart;
	private IPartialData testData;
	private IPartialData trainingData;
	
	final private int testDataLength;
	final private int trainingDataLength;
	final private int attributeCount;
	
	private double entropy;
	
	private AttributeType[] attrTypes;
	private int classesCount;
	private List<Double> classesNames;
	
	private String name;
	
	public ArrayData(List < List < Double > > dataList)
	{
		this.attributeCount = dataList.get(0).size();
		this.data = new double[dataList.size()][this.attributeCount];
		
		for(int i = 0; i < dataList.size(); i++)
		{
			for(int e = 0; e < this.attributeCount; e++)
			{
				this.data[i][e] = dataList.get(i).get(e);
			}
		}
		
		this.attrTypes = new AttributeType[this.attributeCount];
		this.setAttributeType();
		
		this.testPart = 0;
		this.testDataLength = this.data.length / NUM_CROSS_VALIDATION;
		this.trainingDataLength = this.data.length - this.testDataLength;
		this.testData = null;
		this.trainingData = null;
		
		this.setClassesNames();
		
		this.setEntropy();
	}
	
	public AttributeType getAttributeType(int attrPos)
	{
		return this.attrTypes[attrPos];
	}
	
	public double getEntropy()
	{
		return this.entropy;
	}
	
	public int getLength()
	{
		return this.data.length;
	}
	
	public IPartialData getTestData()
	{
		return this.testData;
	}
	
	public IPartialData getTrainingData()
	{
		return this.trainingData;
	}
	
	public int getTestNumPart()
	{
		return this.testPart;
	}
	
	public void setTestNumPart(int num)
	{
		this.testPart = num;
		
		int dataPos = 0;
		int trainingPos = 0;
		double[][] trainingData = new double[this.trainingDataLength][this.attributeCount];
		double[][] testData = new double[this.testDataLength][this.attributeCount];
		
		final int initialTestPos = (this.data.length / NUM_CROSS_VALIDATION) * this.testPart;
		//TODO ver si lo toma con cero
		//Copiamos la parte inicial al set de entrenamiento
		for(; dataPos < initialTestPos; dataPos++, trainingPos++)
		{
			for(int attrPos = 0; attrPos < this.data[dataPos].length; attrPos++)
			{
				trainingData[trainingPos][attrPos] = this.data[dataPos][attrPos];
			}
		}
		
		//Agregamos la parte al set de test
		for(int testPos = 0; testPos < this.testDataLength; dataPos++, testPos++)
		{
			for(int attrPos = 0; attrPos < this.data[dataPos].length; attrPos++)
			{
				testData[testPos][attrPos] = this.data[dataPos][attrPos];
			}
		}
		
		//Terminamos completando el set de entrenamiento
		for(; dataPos < this.data.length; dataPos++, trainingPos++)
		{
			for(int attrPos = 0; attrPos < this.data[dataPos].length; attrPos++)
			{ 
				trainingData[trainingPos][attrPos] = this.data[dataPos][attrPos];
			}
		}
		
		this.trainingData = new PartialArrayData(trainingData, this.attrTypes);
		this.testData = new PartialArrayData(testData, this.attrTypes);
	}
	
	private void setAttributeType()
	{
		final int numOfValuesToBeDiscret = 15;
		
		List < Double > attributeValues = new ArrayList < Double >();
		
		for(int attrPos = 0; attrPos < this.attributeCount; attrPos++)
		{
			this.attrTypes[attrPos] = AttributeType.DISCRETE;
			
			int recordPos = 0;
			for(; recordPos < this.data.length 
					&& attributeValues.size() < numOfValuesToBeDiscret; recordPos++)
			{
				if(!attributeValues.contains(data[recordPos][attrPos]))
					attributeValues.add(this.data[recordPos][attrPos]);
			}
			
			if(recordPos == numOfValuesToBeDiscret)
				this.attrTypes[attrPos] = AttributeType.CONTINUOUS;
		}
	}
	
	private int countDiscreteValues(int attrNum)
	{
		List < Double > attributeValues = new ArrayList < Double >();
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(!attributeValues.contains(this.data[recordPos][attrNum]))
			{
				attributeValues.add(this.data[recordPos][attrNum]);
			}
		}
		
		return attributeValues.size();
	}
	
	/**
	 * Calcula this.classesNames y this.classesCount.
	 * (Nombre y cantidad de clases en el set de datos)
	 */
	private void setClassesNames()
	{
		int attrNum = this.data[0].length - 1;
		List < Double > attributeValues = new ArrayList < Double >();
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(!attributeValues.contains(this.data[recordPos][attrNum]))
			{
				attributeValues.add(this.data[recordPos][attrNum]);
			}
		}
		
		this.classesNames = attributeValues;
		this.classesCount = attributeValues.size();
	}
	
	private void setEntropy()
	{
		int attrNum = this.data[0].length - 1;
		
		List < Double > attributeValues = new ArrayList < Double >();
		int[] discreteData = new int[this.classesCount];
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(!attributeValues.contains(this.data[recordPos][attrNum]))
			{
				attributeValues.add(this.data[recordPos][attrNum]);
				discreteData[attributeValues.indexOf(this.data[recordPos][attrNum])] = 0;
			}
			
			//Se aumenta el contador
			discreteData[attributeValues.indexOf(this.data[recordPos][attrNum])]++;
		}
		
		this.entropy = 0;
		for(int i = 0; i < discreteData.length; i++)
		{
			double partial = discreteData[i] / this.data.length;
			this.entropy -= (partial * Math.log(partial) / Math.log(2));
		}
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public List<Double> getClassesNames()
	{
		return classesNames;
	}
}
