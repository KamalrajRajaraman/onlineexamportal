import { useEffect, useState } from "react";
import Question from "./Question";
import QuestionPalette from "./QuestionPalette";
import { useNavigate, useParams } from "react-router-dom";
import useStateRef from "react-usestateref";
import "./Styles/examPage.css";

import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  GET,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
  swalFireAlert,

} from "../../common/CommonConstants";

const AttendExam = () => {

  
  const navigate = useNavigate();
  const { examId } = useParams();
  const [questions, setQuestions, questionsRef] = useStateRef([]);
  const [activeQuestion, setActiveQuestion, activeQuestionRef] = useStateRef(
    {}
  );
  const [seconds, setSeconds] = useState(0);
  const [min, setMin] = useState(0);
  const [showExam, setShowExam] = useState(false);

  useEffect(() => {
    fetchQuestions();
  }, []);


  useEffect(() => {
    const timer = setInterval(() => {
      setSeconds(seconds - 1);
      if (seconds === 0) {
        setMin(min - 1);
        setSeconds(60);
      }
      if (min === 0 && seconds == 0) {
        console.log("exam submited");
        handleSubmit();
      }
    }, 1000);
    return () => {
      clearInterval(timer);
    };
  });

  const fetchQuestions = async () => {
    console.log(examId);
    try {
      const res = await fetch(
        PROTOCOL +
          DOMAIN_NAME +
          PORT_NO +
          WEB_APPLICATION +
          CONTROL_SERVLET +
          `createAttemptMasterRecords?examId=${examId}`,
        GET
      );
      const data = await res.json();
      console.log(data);
      if (data.result === "error") {
        swalFireAlert("Error",data._ERROR_MESSAGE_,"error");  
        navigate("/user-dashboard/exams");
      }
      if (data.result === "success") {
        setMin(data.durationMinutes);
        const { selectedQuestion } = data;
        setQuestions(selectedQuestion);
        const setFirstQuestion = questionsRef.current[0];
        setActiveQuestion({ ...setFirstQuestion, isViewed: true });
        setShowExam(true);
      }
    } catch (error) {
      console.log(error);
    }
  };

  const onSequenceNumClick = (sequenceNum) => {
    questions.forEach((question) => {
      question.sequenceNum === sequenceNum && setActiveQuestion(question);
    });
  };

  const handleSubmit = async () => {
    navigate(`/user-dashboard/result-page`);
  };

  return (
    <>
      {showExam && (
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
                      className="btn btn-outline-dark col-3  "
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
                  {/* <img
                    src="user.jpg"
                    className="img-rounded float-start"
                    alt="..."
                  ></img>
                  <h6>Candidate ID : 1092</h6> */}
                  <h5>
                    Time Left :{" "}
                    <span class="badge bg-dark">
                      {min}:{seconds}
                    </span>
                  </h5>
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
              <hr />
              <div>
                <h6>
                  <span className="badge bg-success"> </span> Answered Question
                </h6>
                <h6>
                  <span className="badge bg-warning"> </span> Viewed Question
                </h6>
                <h6>
                  <span className="badge bg-danger"> </span> Marked Question
                </h6>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default AttendExam;
