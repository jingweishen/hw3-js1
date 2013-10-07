package edu.cmu.deiis.types;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.Answer;
public class AnswerAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    //get document text
    String docText = aJCas.getDocumentText();
    
    // use ". \\ ?" to split the document into parts
    String tags = "[.\\?]";   
    String []results = docText.split(tags);
    
    
    
    int pos = results[0].length()+1;
    
    for (int i=1;i<results.length-1;i++)
    {
      //create annotation
      Answer annotation = new Answer(aJCas);
      if ( results[i].charAt(4)=='0') 
        annotation.setIsCorrect(false);
      else
        annotation.setIsCorrect(true);
      annotation.setBegin(pos+6);
      annotation.setEnd(pos+results[i].length());
      annotation.addToIndexes();
      pos = pos + results[i].length()+1;   // update the position 
      
    }
    
    System.out.println("successful launch of 'AnswerAnnotator' ");
    
  }

}
