package dataManagement;

import java.util.ArrayList;
import java.util.List;
import treeTraining.TreeTrainer;

public class ArrayData implements IData
{
	private double[][] data;
	
	private int currentFold;
	private IPartialData testData;
	private IPartialData trainingData;
	
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
		
		this.randomizeData();
		
		this.attrTypes = new AttributeType[this.attributeCount];
		this.setAttributeType();
		
		this.currentFold = 0;
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
	
	public int getCurrentFold()
	{
		return this.currentFold;
	}
	
	public void setCurrentFold(int num)
	{
		this.currentFold = num;
		
		int dataPos = 0;
		int trainingPos = 0;
		
		int testDataLength = this.data.length / TreeTrainer.NUM_CROSS_VALIDATION;
		// en ultimo fold incluir toda la ultima data
		if (num + 1 == TreeTrainer.NUM_CROSS_VALIDATION)
			testDataLength += this.data.length % TreeTrainer.NUM_CROSS_VALIDATION;
		int trainingDataLength = this.data.length - testDataLength;
		
		double[][] trainingData = new double[trainingDataLength][this.attributeCount];
		double[][] testData = new double[testDataLength][this.attributeCount];
		
		final int initialTestPos = (this.data.length / TreeTrainer.NUM_CROSS_VALIDATION) * this.currentFold;
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
		for(int testPos = 0; testPos < testDataLength; dataPos++, testPos++)
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
	
	private void randomizeData()
	{
		double[] auxRecord;
		for(int i = 0; i < data.length; i++)
		{
			int randomPos = ((Double)(Math.random() * data.length)).intValue();
			auxRecord = data[randomPos];
			data[randomPos] = data[i];
			data[i] = auxRecord;
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
