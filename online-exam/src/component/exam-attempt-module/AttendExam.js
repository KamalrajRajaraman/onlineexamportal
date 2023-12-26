import React, { useEffect, useState } from "react";
import QuestionType from "./QuestionType";
import Question from "./Question";
import Options from "./Options";
import QuestionTiles from "./QuestionTiles";
import CandidateDetail from "./CandidateDetail";
import QuestionNav from "./QuestionNav";
import QuestionPalette from "./QuestionPalette";
import { useParams } from "react-router-dom";
import useStateRef from "react-usestateref";
import "./Styles/examPage.css";
const AttendExam = () => {
  const { examId } = useParams();

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    console.log(examId);
    try {
      const res = await fetch(
        `https://localhost:8443/onlineexam/control/createAttemptMasterRecords?examId=${examId}`,
        { credentials: "include" }
      );
      const data = await res.json();
      const { selectedQuestion } = data;
      // console.log("data:: ", data);
      setQuestions([...questions, ...selectedQuestion]);
      setActiveQuestion(questionsRef.current[0]);
    } catch (error) {
      console.log(error);
    }
    //  console.log("questions :: ",questionsRef.current);
    //  console.log("activeQuetions :: ",activeQuestion);
  };
  const [questions, setQuestions, questionsRef] = useStateRef([]);
  const [activeQuestion, setActiveQuestion, activeQuestionRef] = useStateRef(
    {}
  );

  const onSequenceNumClick = (sequenceNum) => {
    questions.map((question) => {
      question.sequenceNum === sequenceNum && setActiveQuestion(question);
    });
  };


  const handleSubmit =async()=>{
    try{
      const res = await fetch(
        `https://localhost:8443/onlineexam/control/createAttemptMasterRecords?examId=${examId}`,
        { credentials: "include" }
      );
      const data = await res.json();
      const { selectedQuestion } = data;
    }catch(error){
      console.log(error)
    }
    

  }
  return (
    <>
      {questions && (
        <div className="container-fluid  exam-body py-3 ">
          {/* <div className="row">
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
      </div> */}

          <div className="row mt-3 pt-2 ">
            <div className="col">
              <Question
                question={activeQuestionRef.current}
                onSequenceNumClick={onSequenceNumClick}
                setQuestions={setQuestions}
                questions={questions}
              />
              <hr />
              <div className="row">
                
                <div className="col">
                <div class="d-grid gap-2 d-md-flex justify-content-center">
                <button onClick={handleSubmit} class="btn btn-outline-danger col-3  " type="button">
                  Submit Exam
                </button>
               
              </div>
                </div>

              </div>
             
              
            </div>
            <div className="col-3">
              <div className="col text-center">
                <div>
                  <img
                    src="user.jpg"
                    className="img-rounded float-start"
                    alt="..."
                  ></img>
                  <h6>Candidate ID : 1092</h6>
                  <h5>Time Left: 10:00</h5>
                </div>
              </div>
              <hr />
              <h4>You are viewing Topic A </h4>
              <br />
              <h6>Questions Palette: </h6>
              <div
                className="btn-toolbar mx-4"
                role="toolbar"
                aria-label="Toolbar with button groups"
              >
                <QuestionPalette
                  questions={questions}
                  onSequenceNumClick={onSequenceNumClick}
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default AttendExam;
