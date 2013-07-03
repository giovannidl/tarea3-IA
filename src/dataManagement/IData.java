package dataManagement;

import java.util.List;

public interface IData
{
	AttributeType getAttributeType(int attrPos);
	
	IPartialData getTestData();
	
	IPartialData getTrainingData();
	
	int getTestNumPart();
	
	void setTestNumPart(int num);
	
	int getLength();
	
	double getEntropy();
	
	String getName();
	
	List<Double> getClassesNames();
}
