package edu.cmu.deiis.types;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import edu.cmu.deiis.types.Question;
public class QuestionAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas myJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    //get document text
    String docText = myJCas.getDocumentText();
    int pos = docText.indexOf("?");   // the position of the first occurance of "\r"
    if (pos!=-1)
    {
      Question annotation = new Question (myJCas);
      annotation.setBegin(2);
      annotation.setEnd(pos);
      annotation.setConfidence(1);
      annotation.addToIndexes();
      String question = docText.substring(2, pos);
      System.out.println(question);
      
    }
    
    
    
    System.out.println("successful launch of 'QuestionAnnotator' ");
    
  }

}
