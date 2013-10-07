package edu.cmu.deiis.types;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.Comparator;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Answer;

public class Evaluation extends JCasAnnotator_ImplBase {

  // for our later sort of AnswerScore according to the score
  public class ComparatorImpl implements Comparator <AnswerScore>{
    public int compare(AnswerScore s1,AnswerScore s2){
      double score1 = s1.getScore();
      double score2 = s2.getScore();
      if( score1>score2){
        return -1;
      }else{
        if(score1<score2){
          return 1;
        }else
          return 0;
        
      }
             
    }
  }
  
  
  
  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    // get the document
    String docText = aJCas.getDocumentText();
    //get input annotation indexes
    FSIndex ansScoIndex = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator ite = ansScoIndex.iterator();
     
    //vectors of the AnswerScore
    Vector anScoV = new Vector();
    //record of the total correct answers indicated by "IsCorret"
    double numCorrect = 0;
    
    System.out.println("************ the display of answer scores************");
    while( ite.hasNext()){
      //get the information of the annotation: Answer, Score, String of the answer
      AnswerScore anSco = ( AnswerScore) ite.next();
      Answer ans = anSco.getAnswer();
      String anString = docText.substring(ans.getBegin(),ans.getEnd());
      double score = anSco.getScore();
      String scoreString = Double.toString(score);
      
     anScoV.add(anSco);
      
      // display the information in the console with format of "+/-  score  answer"
      if(ans.getIsCorrect()){
        numCorrect++;
        System.out.println("+" + " "+ scoreString+ " " + anString);
      }
      else
        System.out.println("-" + " "+ scoreString+ " " + anString);
            
      
    }
    
    
    // sort the vector according to the score
    Comparator comp = new ComparatorImpl();
    Collections.sort(anScoV,comp);
    
    
    double precisionNum = 0;
    for (int i=0;i<numCorrect;i++){
      AnswerScore anSco =(AnswerScore) anScoV.get(i);
      
      
      if(anSco.getAnswer().getIsCorrect()){
        precisionNum++;
             }
    }
    
    
    double precision = precisionNum/numCorrect;
    System.out.println("**********the precision********");
    
        System.out.println("precision of " + Double.toString(numCorrect) + ":  " + Double.toString(precision));

    
  }

}
