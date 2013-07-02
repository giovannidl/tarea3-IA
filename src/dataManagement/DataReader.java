package dataManagement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataReader
{
	private static DataReader instance;
	
	private DataReader()
	{
	}
	
	public IData getData(String path)
	{
		IData data = null;
		
		try
		{
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			List < List < Double > > dataList = new ArrayList < List < Double > >();
			String line = br.readLine();
			while(line != null)
			{
				dataList.add(this.getDoubleList(line));
				
				line = br.readLine();
			}
			
			data = new ArrayData(dataList);
			
			br.close();
		}
		catch(IOException ex)
		{
			System.out.println("No existe el archivo ingresado. " + ex.getMessage());
			throw new RuntimeException(ex);
		}
		
		return data;
	}
	
	public static DataReader getInstance()
	{
		if(instance == null)
			instance = new DataReader();
		
		return instance;
	}
	
	private List < Double > getDoubleList(String line)
	{
		//Comienza la iteración desde 1 porque las lineas comienzan con dos espacios.
		final int startIteration = 1;
		
		String[] atts = line.split(" +");
		List < Double > doubleList = new ArrayList < Double >();
		try
		{
			for(int i = startIteration; i < atts.length; i++)
			{
				doubleList.add(Double.valueOf(atts[i]));
			}
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Error en el parseo:" + ex.getMessage());
			throw new RuntimeException(ex);
		}
		
		return doubleList;
	}
}
