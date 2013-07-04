package dataManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartialArrayData implements IPartialData
{
	private final double EPSILON = 0.0000000001; 
	
	private double[][] data;
	private double[][] attrArray;
	
	final private int attributeCount;
	private double entropy;
	private double purity;
	private double mayorityClass;
	private boolean singleClass;
	
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
		if(data.length == 0)
		{
			this.classesCount = 0;
			this.entropy = 999999;
			this.purity = 0;
		}
		else
		{
			this.classesCount = this.getDiscreteValues(this.attributeCount - 1).length;
			this.setEntropyAndPurity();
		}
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
			if(this.data[recordPos][attrNum] < value)
				lessThanRecords.add(data[recordPos]);
			else
			moreThanRecords.add(data[recordPos]);
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
			//Recorremos los atributos menos el último que corresponde a la clase
			for(int attrPos = 0; attrPos < this.attributeCount - 1; attrPos++)
			{
				result += coefs[attrPos] * this.data[recordPos][attrPos];
			}
			
			//El último coeficiente corresponde al coeficiente libre
			result += coefs[this.attributeCount - 1];
			
			//Bias a la derecha cuando hay errores de redondeo
			result += EPSILON;
			
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
	
	public double getMayorityClass()
	{
		return this.mayorityClass;
	}
	
	public boolean isSingleClass()
	{
		return this.singleClass;
	}
	
	public double[] getRecord(int pos)
	{
		return this.data[pos];
	}
	
	public double getBestPivotAttribute(int attrNum)
	{
		int finalPos = 0;
		double maxGain = 0;
		for(int pos = 0; pos < this.data.length; pos++)
		{
			IPartialData[] dividedData = this.divideData(attrNum, this.data[pos][attrNum]);
			double gain = this.getEntropy();
			for(IPartialData partialData : dividedData)
			{
				gain -= ((double)partialData.getLength() / this.getLength()) * partialData.getEntropy();
			}
			
			if(gain > maxGain)
			{
				maxGain = gain;
				finalPos = pos;
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
			double partial = (double)discreteData[i] / this.data.length;
			this.entropy -= (partial * Math.log(partial) / Math.log(2));
			
			if(discreteData[i] > topRep)
			{
				topRep = discreteData[i];

				//Aprovecharemos el método para guardar el valor de la clase mayoritaria
				this.mayorityClass = attributeValues.get(i);
			}
		}
		
		this.purity = (double) topRep / this.data.length;
		
		if(this.purity == 1)
			this.singleClass = true;
	}
}
