package dataManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartialArrayData implements IPartialData
{
	private double[][] data;
	private double[][] attrArray;
	
	final private int attributeCount;
	private double entropy;
	private double purity;
	
	private AttributeType[] attrTypes;
	private int classesCount;
	
	public PartialArrayData(double[][] data, AttributeType[] attrTypes)
	{
		this.attributeCount = attrTypes.length;
		this.data = new double[data.length][this.attributeCount];
		this.attrArray = new double[this.attributeCount][data.length];
		
		for(int recordPos = 0; recordPos < data.length; recordPos++)
		{
			for(int attrPos = 0; attrPos < data[recordPos].length; attrPos++)
			{
				this.data[recordPos][attrPos] = data[recordPos][attrPos];
				this.attrArray[attrPos][recordPos] = data[recordPos][attrPos];
			}
		}
		
		this.attrTypes = Arrays.copyOf(attrTypes, attrTypes.length);
		this.classesCount = this.getDiscreteValues(this.data[0].length - 1).length;
		this.setEntropyAndPurity();
	}
	
	public AttributeType getAttributeType(int attrPos)
	{
		return this.attrTypes[attrPos];
	}
	
	public IPartialData getEqualsRecords(int attrNum, double value)
	{
		List < double[] > records = new ArrayList < double[] >();
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(this.data[recordPos][attrNum] == value)
				records.add(data[recordPos]);
		}
		
		return new PartialArrayData(records.toArray(new double[0][]), this.attrTypes);
	}
	
	public IPartialData getNotEqualsRecords(int attrNum, double value)
	{
		List < double[] > records = new ArrayList < double[] >();
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(this.data[recordPos][attrNum] != value)
				records.add(data[recordPos]);
		}
		
		return new PartialArrayData(records.toArray(new double[0][]), this.attrTypes);
	}
	
	public IPartialData getMoreThanRecords(int attrNum, double value)
	{
		List < double[] > records = new ArrayList < double[] >();
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(this.data[recordPos][attrNum] > value)
				records.add(data[recordPos]);
		}
		
		return new PartialArrayData(records.toArray(new double[0][]), this.attrTypes);
	}
	
	public IPartialData getLessThanRecords(int attrNum, double value)
	{
		List < double[] > records = new ArrayList < double[] >();
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(this.data[recordPos][attrNum] < value)
				records.add(data[recordPos]);
		}
		
		return new PartialArrayData(records.toArray(new double[0][]), this.attrTypes);
	}
	
	public IPartialData[] divideData(int attrNum, double value)
	{
		IPartialData[] dividedData = new IPartialData[2];
		List < double[] > moreThanRecords = new ArrayList < double[] >();
		List < double[] > lessThanRecords = new ArrayList < double[] >();
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(this.data[recordPos][attrNum] >= value)
				moreThanRecords.add(data[recordPos]);
			else
				lessThanRecords.add(data[recordPos]);
		}
		
		dividedData[0] = new PartialArrayData(lessThanRecords.toArray(new double[0][]), this.attrTypes);
		dividedData[1] = new PartialArrayData(moreThanRecords.toArray(new double[0][]), this.attrTypes);
		
		return dividedData;
	}
	
	//Divide la data según un hiperplano definido por los coeficientes de cada atributo
	public IPartialData[] divideData(double[] coefs)
	{
		List < double[] > lessThanZeroRecords = new ArrayList < double[] >();
		List < double[] > moreThanZeroRecords = new ArrayList < double[] >();
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			double result = 0;
			for(int attrPos = 0; attrPos < this.attributeCount - 1; attrPos++)
			{
				result += coefs[attrPos] * this.data[recordPos][attrPos];
			}
			if(result < 0)
				lessThanZeroRecords.add(this.data[recordPos]);
			else
				moreThanZeroRecords.add(this.data[recordPos]);
		}
		
		IPartialData[] dividedData = new IPartialData[2];
		dividedData[0] = new PartialArrayData(lessThanZeroRecords.toArray(new double[0][]), this.attrTypes);
		dividedData[1] = new PartialArrayData(moreThanZeroRecords.toArray(new double[0][]), this.attrTypes);
		
		return dividedData;
	}
	
	public IPartialData[] divideByDiscreteAttribute(int attrNum)
	{
		List < Double > attributeValues = new ArrayList < Double >();
		List < List < double[] > > discreteData = new ArrayList < List < double[] > >();
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(!attributeValues.contains(this.data[recordPos][attrNum]))
			{
				attributeValues.add(this.data[recordPos][attrNum]);
				discreteData.add(new ArrayList < double[] >());
			}
			
			discreteData.get(attributeValues.indexOf(this.data[recordPos][attrNum])).add(this.data[recordPos]);
		}
		
		IPartialData[] dividedData = new IPartialData[discreteData.size()];
		for(int i = 0; i < discreteData.size(); i++)
		{
			dividedData[i] = new PartialArrayData(discreteData.get(i).toArray(new double[0][]), this.attrTypes);
		}
		
		return dividedData;
	}
	
	public double[][] getAttributes()
	{
		return this.attrArray;
	}
	
	public int getLength()
	{
		return this.data.length;
	}
	
	public double getEntropy()
	{
		return this.entropy;
	}
	
	public double getPurity()
	{
		return this.purity;
	}
	
	public double getTopDividerAttribute(int attrNum)
	{
		final int iterationNumber = 100;
		int finalPos = 0;
		double bestPurity = 0;
		for(int i = 0; i < iterationNumber && i < this.data.length; i++)
		{
			int pos = ((Double)(Math.random() * this.data.length)).intValue();
			IPartialData[] dividedData = this.divideData(attrNum, this.data[pos][attrNum]);
			
			double purity = (dividedData[0].getPurity() + dividedData[1].getPurity()) / 2;
			if(purity > bestPurity)
			{
				finalPos = pos;
				bestPurity = purity;
			}
		}
		
		return this.data[finalPos][attrNum];
	}
	
	public Double[] getDiscreteValues(int attrNum)
	{
		List < Double > attributeValues = new ArrayList < Double >();
		
		for(int recordPos = 0; recordPos < this.data.length; recordPos++)
		{
			if(!attributeValues.contains(this.data[recordPos][attrNum]))
			{
				attributeValues.add(this.data[recordPos][attrNum]);
			}
		}
		
		return attributeValues.toArray(new Double[0]);
	}
	
	private void setEntropyAndPurity()
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
		
		//Para obtener la pureza
		int topRep = 0;
		
		//Obtenemos la entropía
		this.entropy = 0;
		for(int i = 0; i < discreteData.length; i++)
		{
			double partial = discreteData[i] / this.data.length;
			this.entropy -= (partial * Math.log(partial) / Math.log(2));
			
			if(discreteData[i] > topRep)
				topRep = discreteData[i];
		}
		
		this.purity = topRep / this.data.length;
	}
}
