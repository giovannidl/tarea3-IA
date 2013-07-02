package dataManagement;

public interface IData
{
	AttributeType getAttributeType(int attrPos);
	
	IPartialData getTestData();
	
	IPartialData getTrainingData();
	
	int getTestNumPart();
	
	void setTestNumPart(int num);
	
	int getLength();
	
	double getEntropy();
}
