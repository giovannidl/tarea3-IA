package dataManagement.decisionTrees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataManagement.IPartialData;

public class ObliqueTree implements IDecisionTree
{
	private final double EPSILON = 0.0000000001;
	private Node root;
	private int minRecords;
	private int k;
	private int restart_iteration;
	
	public ObliqueTree(int minRecords, int k, int restart_iteration)
	{
		root = null;
		this.minRecords = minRecords;
		this.k = k;
		this.restart_iteration = restart_iteration;
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
		while(!auxNode.isLeaf())
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

		//Si k es mayor que uno, lo disminuimos y comenzamos la búsqueda de los demás coeficientes
		if(this.k > 1)
		{
			List < Integer > attrExpanded = new ArrayList < Integer >();
			attrExpanded.add(attrSelected);
			maxGain = calculateCombinationCoefficients(data, coefs, attrExpanded, this.k - 1, maxGain);
			
			//Si k es mayor que uno, implementamos RESTART_ITERATION iteraciones con el primero atributo elegido al azar,
			//para evitar caer en un máximo local
			for(int i = 0; i < this.restart_iteration; i++)
			{
				int attrRandom = ((Double)(Math.random() * data.getAttributes().length)).intValue();
				double valueAttrRandom = data.getBestPivotAttribute(attrRandom);
				IPartialData[] dividedData = data.divideData(attrRandom, valueAttrRandom);
				double randomGain = data.getEntropy();
				for(IPartialData partialData : dividedData)
				{
					randomGain -= ((double)partialData.getLength() / data.getLength()) * partialData.getEntropy();
				}
				
				double[] randomCoefs = new double[data.getAttributes().length];
				randomCoefs[attrRandom] = (double) 1 / valueAttrRandom;
				randomCoefs[data.getAttributes().length - 1] = -1;
				
				List < Integer > attrRandomExpanded = new ArrayList < Integer >();
				attrRandomExpanded.add(attrSelected);
				randomGain = calculateCombinationCoefficients(data, randomCoefs, attrRandomExpanded, this.k - 1, randomGain);
				
				//Comparamos si la opción random es mejor que la obtenida anteriormente
				if(randomGain > maxGain)
				{
					maxGain = randomGain;
					this.copyArrays(coefs, randomCoefs);
				}
			}
		}
		
		return coefs;
	}

	private double calculateCombinationCoefficients(IPartialData data, double[] coefs, List < Integer > attrExpanded, 
			int k, double maxGain)
	{
		int attrSelected = -1;
		
		//No ocupamos el último atributo porque es la clase del registro.
		for(int attrNum = 0; attrNum < data.getAttributes().length - 1; attrNum++)
		{
			if(attrExpanded.contains(attrNum))
				continue;
			
			double[] auxCoefs = Arrays.copyOf(coefs, coefs.length);
			IPartialData[] dividedData;
			
			this.getBestCombinationCoefficients(data, auxCoefs, attrExpanded, attrNum);
			
			dividedData = data.divideData(auxCoefs);
	
			double gain = data.getEntropy();
			for(IPartialData partialData : dividedData)
			{
				gain -= ((double)partialData.getLength() / data.getLength()) * partialData.getEntropy();
			}
			
			if(gain > maxGain)
			{
				maxGain = gain;
				this.copyArrays(coefs, auxCoefs);
				attrSelected = attrNum;
			}
		}
		
		//Si attrSelected sigue siendo negativo, es porque ninguna combinación de atributos mejora la ganancia
		if(attrSelected == -1)
		{
			return maxGain;
		}
		
		//Si k es mayor que uno, lo disminuimos y buscamos los demás coeficientes
		if(k > 1)
		{
			attrExpanded.add(attrSelected);
			maxGain = calculateCombinationCoefficients(data, coefs, attrExpanded, k - 1, maxGain);
		}
		
		return maxGain;
	}
	
	private double getBestCombinationCoefficients(IPartialData data, double[] coefs, List < Integer > attrExpanded, int attrNum)
	{
		List < Integer > actualAttrCoef = new ArrayList < Integer >(attrExpanded);
		actualAttrCoef.add(attrNum);
		
		//Empezamos con un valor random
		int randomPos = ((Double)(Math.random() * data.getLength())).intValue();
		double continuousValue = data.getRecord(randomPos)[attrNum];
		coefs[attrNum] = (double) 1 / continuousValue;
		
		double maxGain = data.getEntropy();
		IPartialData[] dividedData = data.divideData(coefs);
		for(IPartialData partialData : dividedData)
		{
			maxGain -= ((double)partialData.getLength() / data.getLength()) * partialData.getEntropy();
		}
		
		boolean hasBeenModify = true;
		//Iteramos hasta que ningún coeficiente ha sido modificado
		while(hasBeenModify)
		{
			hasBeenModify = false;
			//Iteramos por los atributos que tienen coeficiente
			for(Integer expAttr : actualAttrCoef)
			{
				//Obtenemos los valores de U para cada registro
				double[] uValues = new double[data.getLength()];
				for(int i = 0; i < uValues.length; i++)
				{
					uValues[i] = coefs[expAttr] * data.getRecord(i)[expAttr] - this.valuateCoefficients(data.getRecord(i), coefs);
					uValues[i] /= data.getRecord(i)[expAttr];
				}
				
				Arrays.sort(uValues);
				//Obtenemos la media entre cada par de valores y lo guardamos en el mismo arreglo para guardar memoria
				for(int i = 0; i < uValues.length - 1; i++)
				{
					uValues[i] = (uValues[i] + uValues[i + 1]) / 2;
				}
				
				//Creamos un arreglo auxiliar para ir iterando los coeficientes
				double[] auxCoefs = Arrays.copyOf(coefs, coefs.length);
				//Obtenemos la ganancia por cada valor y elegimos la que nos da mayor ganancia
				//No ocupamos el último valor, ya que no es ninguna media
				for(int i = 0; i < uValues.length - 1; i++)
				{
					auxCoefs[expAttr] = uValues[i];
					dividedData = data.divideData(auxCoefs);
					double gain = data.getEntropy();
					for(IPartialData partialData : dividedData)
					{
						gain -= ((double)partialData.getLength() / data.getLength()) * partialData.getEntropy();
					}
					
					if(gain > maxGain)
					{
						coefs[expAttr] = uValues[i];
						maxGain = gain;
						hasBeenModify = true;
					}
				}
			}
		}
		
		//Si llegamos hasta aca es porque no se puede mejorar ningún coeficiente, osea tenemos el mejor.
		return maxGain;
	}
	
	private double valuateCoefficients(double[] record, double[] coefs)
	{
		double value = 0;
		//No tomamos la ultima, porque es la clase
		for(int i = 0; i < record.length - 1; i++)
		{
			value += record[i] * coefs[i];
		}
		
		//Le sumamos el coeficiente libre
		value += coefs[coefs.length - 1];
		return value;
	}
	
	private void copyArrays(double[] copy, double[] original)
	{
		for(int i = 0; i < original.length; i++)
		{
			copy[i] = original[i];
		}
	}
}
