package com.example.ekaksha.Data;

import java.util.ArrayList;

public class Question {

     private String question;
     private ArrayList<String>Options=new ArrayList<String>();
     private String Answer;

      public ArrayList<String> getOptions() {
            return Options;
      }

      public String getQuestion() {
            return question;
      }

      public void setQuestion(String question) {
            this.question = question;
      }

      public void setAnswer(String answer) {
            Answer = answer;
      }

      public void setOptions(ArrayList<String> options) {
            Options = options;
      }

      public String getAnswer() {
            return Answer;
      }
}


