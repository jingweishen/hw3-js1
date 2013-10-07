package edu.cmu.deiis.types;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.FSIndex;

import edu.cmu.deiis.types.*;
public class TokenAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    //get document text
    String docText = aJCas.getDocumentText();
    // get annotation indexes
    FSIndex QuestionIndex = aJCas.getAnnotationIndex(Question.type);
    FSIndex AnswerIndex = aJCas.getAnnotationIndex(Answer.type);
    // read the "Question" annotation
    Question ques = (Question) QuestionIndex.iterator().next();
    // get the string of the question
    String queString = docText.substring(ques.getBegin(), ques.getEnd());
    // use " " to split the question in to tokens
    String tags = "[ ]";
    String []resultsQ = queString.split(tags);
    int pos = ques.getBegin();
    for (int i = 0; i<resultsQ.length;i++){
      //create a new annotation
      Token annotation = new Token(aJCas);
      annotation.setBegin(pos);
      
      annotation.setEnd(pos + resultsQ[i].length());
      annotation.addToIndexes();
      pos = pos + resultsQ[i].length() + 1;
    }
    
    //read the "Answer" annotation
    Iterator ansIte = AnswerIndex.iterator();
    while(ansIte.hasNext()){
      Answer ans = (Answer) ansIte.next();
      //get the string of the answer
      String ansString = docText.substring(ans.getBegin(), ans.getEnd());
      //use " " to split the string into tokens
      String []resultsA = ansString.split(tags);
      pos = ans.getBegin(); 
      for (int i = 0; i< resultsA.length;i++){
        //create a new annotation
        Token annotation = new Token(aJCas);
        annotation.setBegin(pos);
        annotation.setEnd(pos + resultsA[i].length());
        annotation.addToIndexes();
        pos = pos + resultsA[i].length() + 1;
        
      }
        
    }
    
    
   
  }

}
