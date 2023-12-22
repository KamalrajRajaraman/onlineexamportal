import React, { useState } from "react";
import QuestionType from "./QuestionType";
import Question from "./Question";
import Options from "./Options";
import QuestionTiles from "./QuestionTiles";
import CandidateDetail from "./CandidateDetail";
import QuestionNav from "./QuestionNav";
import QuestionPalette from "./QuestionPalette";

const AttendExam = () => {
  const [questions, setQuestions] = useState([
    {
      sequenceNum: 1,
      questiomId: 101,
      questionDetails:  "Number of primitive data types in Java are?",
      optionA: "6",
      optionB: "7",
      optionC: "8",
      optionD: "9",
      optionE: "none",
      questionType: "Single Choice",
    },
    {
      sequenceNum: 2,
      questiomId: 102,
      questionDetails: "What is Runnable?",
      optionA: "class",
      optionB: "enum",
      optionC: "interface",
      optionD: "method",
      optionE: "none",
      questionType: "Single Choice",
    },
    {
      sequenceNum: 3,
      questiomId: 103,
      questionDetails: "Which of the following exception is thrown when divided by zero statement is executed?",
      optionA: "NullPointerException",
      optionB: "ClassCastException",
      optionC: "ArithmeticException",
      optionD: "NumberFormatException",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 4,
      questiomId: 104,
      questionDetails: "What is the extension of java code files?",
      optionA: ".js",
      optionB: ".txt",
      optionC: ".jsx",
      optionD: ".java",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 5,
      questiomId: 105,
      questionDetails: "Which one of the following is not an access modifier?",
      optionA: "pulbic",
      optionB: "protected",
      optionC: "private",
      optionD: "void",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 6,
      questiomId: 106,
      questionDetails: " Which function is used to perform some action when the object is to be destroyed?",
      optionA: "finialize()",
      optionB: "delete()",
      optionC: "main()",
      optionD: "remove()",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 7,
      questiomId: 107,
      questionDetails: "What is the return type of the hashCode() method in the Object class?",
      optionA: "int",
      optionB: "Integer",
      optionC: "Object",
      optionD: "String",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 8,
      questiomId: 108,
      questionDetails: "An interface with no fields or methods is known as a ______.",
      optionA: "Runnable Interface",
      optionB: "Marker Interface",
      optionC: "Abstract  Interface",
      optionD: "CharSequence  Interface",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 9,
      questiomId: 109,
      questionDetails: "What is the initial quantity of the ArrayList list?",
      optionA: "5",
      optionB: "10",
      optionC: "20",
      optionD: "15",
      optionE: "none",
      questionType: "True Or False",
    },
    {
      sequenceNum: 10,
      questiomId: 110,
      questionDetails: "JDK stands for ____.",
      optionA: "Java development kit",
      optionB: "Java deployment kit",
      optionC: "JavaScript deployment kit",
      optionD: "JavaScript development kit",
      optionE: "none",
      questionType: "True Or False",
    },
  ]);

  const [activeQuestion, setActiveQuestion] = useState(questions[0]);
 
  
  const onSequenceNumClick=(sequenceNum) =>{
    questions.map(
      (question) =>{ question.sequenceNum === sequenceNum && setActiveQuestion(question); }
    ); 
  }

  return (
    <div className="container-fluid  exam-body py-3 ">
      <div className="row">
        <div className="col-9  ">
          <ul className="nav nav-pills border border-secondary-subtle p-2 rounded">
            <li className="nav-item">
              <button className="nav-link active">Core Java</button>
            </li>
            <li className="nav-item">
              <button className="nav-link">Advanced Java</button>
            </li>
          </ul>
        </div>
        <div className="col-3 text-center">
          <div>
            <img src="user.jpg" className="img-rounded float-start" alt="..."></img>
            <h6>Candidate ID : 1092</h6>
            <h5>Time Left: 10:00</h5>
          </div>
        </div>
      </div>
        
      <div className="row mt-3 ">
       
        <Question question={activeQuestion} onSequenceNumClick={onSequenceNumClick} setQuestions={setQuestions} questions ={questions}/>
        
        <div className="col-3">
          <hr />
          <h4>You are viewing Topic A </h4>
          <br />
          <h6>Questions Palette: </h6>
          <div
            className="btn-toolbar mx-4"
            role="toolbar"
            aria-label="Toolbar with button groups"
          >
           <QuestionPalette questions={questions} onSequenceNumClick={onSequenceNumClick} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default AttendExam;
