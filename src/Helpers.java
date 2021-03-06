import java.util.ArrayList;
import java.util.HashMap;


public class Helpers {

	public static double LogBaseX(double x, double base){
		return Math.log(x) / Math.log(base);
	}

	// calculating entropy
	static double calcEntropy( ArrayList<TrainingDataItem> examples ){
		double entropy = 0;

		HashMap<String, Integer> targetClassOccurrenceCount = new HashMap<String, Integer>();

		for( TrainingDataItem item : examples ){
			Integer counter = targetClassOccurrenceCount.get( item.targetClass );
			if( counter == null ) {
				counter = 0;
			}
			counter++;

			targetClassOccurrenceCount.put(item.targetClass, counter);
		}
			
		// if all examples is 'pure', belong to one class
		if(targetClassOccurrenceCount.size() == 1 ) {
			return 0.0;
		}
		
		for( String key : targetClassOccurrenceCount.keySet() ) {
			double occurrence = targetClassOccurrenceCount.get( key );
			double p = occurrence / examples.size();
			entropy -= p * LogBaseX(p,4.0); // because there are 4 target classifikations
		}

		return entropy;
	}

	// calculating information gain
	 static double calcGain(ArrayList<TrainingDataItem> examples, String attribute ){
		 
		 double gain = calcEntropy( examples );
		 
		 HashMap< String, ArrayList<TrainingDataItem> > listOfExamplesSplitByAttributeValue = new HashMap< String, ArrayList<TrainingDataItem> >();
		 splitExamples(examples, attribute, listOfExamplesSplitByAttributeValue);

		 for( String key : listOfExamplesSplitByAttributeValue.keySet() ) {
			 
			 ArrayList<TrainingDataItem> list = listOfExamplesSplitByAttributeValue.get(key);			 

			 gain -= ( list.size() * calcEntropy( list )) / examples.size() ; // mind the order

		 }

		 return gain;
	 }
	 
	// splitting the examples by one attribute
	// the result is returned via reference of the HashMap.
	public static void splitExamples(ArrayList<TrainingDataItem> examples, String attribute, HashMap<String, ArrayList<TrainingDataItem>> listOfExamplesSplitByAttributeValue) {
		for( TrainingDataItem item : examples ){
            ArrayList<TrainingDataItem> list = listOfExamplesSplitByAttributeValue.get( item.getAttributeValue( attribute ) );

            if( list == null ){
                list = new ArrayList<TrainingDataItem>();
                listOfExamplesSplitByAttributeValue.put( item.getAttributeValue( attribute ), list );
            }

            list.add( item );
        }
	}

	 // returns best valued attribute
	 static String selectBestAttribute(ArrayList<TrainingDataItem> examples, ArrayList<String> attributes) {

		 String bestCurrentAttribute = "";
		 double bestCurrentGain = -1;

		 for( String attribute : attributes) {
			 // calc GAIN for every attribute
			 double currentGain = calcGain(examples, attribute);
			 
			 if( currentGain > bestCurrentGain ) {
				 bestCurrentAttribute = attribute;
				 bestCurrentGain = currentGain;
			 }
		 }

		 return bestCurrentAttribute;
	 }
}
