package dataManagement;


public interface IPartialData
{
	AttributeType getAttributeType(int attrPos);
	
	IPartialData getEqualsRecords(int attrNum, double value);
	
	IPartialData getNotEqualsRecords(int attrNum, double value);
	
	IPartialData getMoreThanRecords(int attrNum, double value);
	
	IPartialData getLessThanRecords(int attrNum, double value);

	IPartialData[] divideData(double[] coefs);
	
	IPartialData[] divideData(int attrNum, double value);
	
	IPartialData[] divideByDiscreteAttribute(int attrNum);
	
	int getLength();
	
	double[][] getAttributes();
	
	double getEntropy();
	
	double getPurity();
	
	double getMayorityClass();
	
	boolean isSingleClass();
	
	double[] getRecord(int pos);
	
	double getBestPivotAttribute(int attrNum);
	
	Double[] getDiscreteValues(int attrNum);
}
