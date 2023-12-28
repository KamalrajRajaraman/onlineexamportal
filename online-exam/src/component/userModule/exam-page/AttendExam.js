import React, { useEffect  } from "react";

import Question from "./Question";

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
      
      setQuestions([...questions, ...selectedQuestion]);
      setActiveQuestion(questionsRef.current[0]);
    } catch (error) {
      console.log(error);
    }
    
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

  const handleSubmit = async () => {
    console.log(questions)
    
  };

  return (
    <>
      {questions && (
        <div className="container-fluid  exam-body py-3 ">
          <div className="row ">
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
                  <div className="d-grid gap-2 d-md-flex justify-content-center">
                    <button
                      onClick={handleSubmit}
                      className="btn btn-outline-danger col-3  "
                      type="button"
                    >
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
