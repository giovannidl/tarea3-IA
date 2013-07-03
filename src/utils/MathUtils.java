package utils;

public class MathUtils
{
	// no instanciable
	private MathUtils() { }

	/*
	 * Orden de coords. en matrices son asi:
	 * matrix[i][j]
	 * +-----+-----+
	 * | 0,0 | 1,0 |
	 * |-----+-----+
	 * | 0,1 | 1,1 |
	 * +-----+-----+
	 */
	
	/**
	 * Suma los elementos de m2 sobre la matriz m1.
	 * m1 y m2 deben ser del mismo tamaño.
	 */
	public static void MatrixAdd(int[][] m1, int[][] m2)
	{
		for(int i = 0; i < m1.length; i++)
		{
			for(int j = 0; j < m1[0].length; j++)
				m1[i][j] += m2[i][j];
		}
	}
	
	/**
	 * Suma de los elementos de la diagonal.
	 * Matrix debe ser cuadrada.
	 */
	public static int MatrixTrace(int[][] matrix)
	{
		int result = 0;
		for(int i = 0; i < matrix.length; i++) 
		{
			result += matrix[i][i];
		}
		return result;
	}
	
	/**
	 * Suma todos los elementos de la matriz.
	 */
	public static int MatrixSumAll(int[][] matrix)
	{
		int result = 0;
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[0].length; j++)
				result += matrix[i][j];
		}
		return result;
	}
	
	/**
	 * Suma todos los elementos de la i-esima columna.
	 */
	public static int MatrixSumColumn(int[][] matrix, int i)
	{
		int result = 0;
		for(int j = 0; j < matrix[i].length; j++)
			result += matrix[i][j];
		return result;
	}
}
