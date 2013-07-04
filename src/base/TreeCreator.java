package base;

import treeTraining.TreeTrainer;
import dataManagement.DataReader;
import dataManagement.IData;

public class TreeCreator
{
	public static void main(String[] args)
	{
		String filePath = "";
		
		String[] testFiles = new String[] { "glass_mm.txt", "heart_mm.txt", "pima_indians_diabetes.txt" };
		
		//Número mínimo de registros que puede haber por hoja 
		final int initialMinRecords = 2;
		final int finalMinRecords = 5;
		
		//Número máximo de atributos elegidos en cada nodo
		int k = 2;
		
		//Número de veces que el algoritmo resetea los coeficientes elegidos para evitar caer en un maximo local
		final int restart_iteration = 2;
		
		if(args.length != 0)
		{
			filePath = args[0];
			k = Integer.parseInt(args[1]);
		}
		else
		{
			filePath = testFiles[2];
		}
		
		IData expData = DataReader.getInstance().getData(filePath);
		
		System.out.println("Data file: " + filePath);
		System.out.println("R: " + restart_iteration);
		for(int i = initialMinRecords; i <= finalMinRecords; i ++)
		{
			System.out.println("\n** k = " + k + ", minRecords = " + i);
			TreeTrainer.getInstance().trainTree(expData, i, k, restart_iteration);
		}
	}
}
