package edu.cmu.deiis.types;

import java.util.Iterator;
import java.util.regex.*;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.FSIndex;

import edu.cmu.deiis.types.*;
public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  
  
  
  private double scoring(String ques, String ans){
  
   //get the tokens
   // use " " to split the question in to tokens
    String tags = "[ ]";
    String []quesToken = ques.split(tags);
  //use " " to split the string into tokens
    String []ansToken = ans.split(tags);
   // the score consists of 3 parts: overlap, neg and passive
   // overlap: mearuse the overlapping of question and answer.
     double num = 0;
      for (int i = 0; i< quesToken.length;i++){
        String regEx = quesToken[i];
        
        Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(ans);
        if(m.find()){
          num=num+1;
                  }
        
      }
     // System.out.println(num);
      double total = quesToken.length;
      double overlapScore = num/ total;
      
    // neg : a score based on negation, since negation will change the meaning of sentence
      String regEx = "no|not|n't|never";   
      Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher(ans);
      boolean neg = m.find();
      double negScore = 0;
      if(neg){
         negScore = -1;
      }else{
        negScore = 0.5;
      }
      
    // passive: a score based on passive tense
      double pos=-1;
      for (int i=0;i<ansToken.length;i++){
        if (ansToken[i].equals("by")){
          
          for (int j=0;j<quesToken.length;j++){
            if(quesToken[j].equals(ansToken[i+1])){
              pos=j;
            }
          }
        }
      }
      
      double passiveScore = 1- (pos+1)/(double)quesToken.length;
      
      //final score 
      double finalScore = 0.5*overlapScore + 0.25*negScore + 0.25*passiveScore;
     // System.out.println("total:" + Double.toString(finalScore)+ "over:"+ Double.toString(overlapScore)+ 
      //        "neg:"+ Double.toString(negScore)+"pass:"+ Double.toString(passiveScore));
      return finalScore;
      
        
  }
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
    String queString = docText.substring(ques.getBegin(),ques.getEnd());
    //read the "Answer" annotation
    Iterator ansIte = AnswerIndex.iterator();
   
    while (ansIte.hasNext()){
      double score =0;
      //create a new annotation
      Answer ans = (Answer) ansIte.next();
      String ansString = docText.substring(ans.getBegin(), ans.getEnd());
      AnswerScore annotation = new AnswerScore(aJCas);
      annotation.setBegin(ans.getBegin());
      annotation.setEnd(ans.getEnd());
      annotation.setAnswer(ans);
    //calculate the score 
      score = scoring(queString,ansString);
      annotation.setScore(score);
      annotation.addToIndexes();
      
    }
    
    
  }

}
