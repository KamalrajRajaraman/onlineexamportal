import React, { useEffect, useState } from "react";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
const Question = ({
  question,
  onSequenceNumClick,
  setQuestions,
  questions,
}) => {
  const [submittedAnswer, setSubmittedAnswer] = useState("");
  const [isFlagged, setIsFlagged] = useState(false);
  useEffect(() => {
    if (question.questionType === "02") {
      setSubmittedAnswer(question.submittedAnswer || []);
    } else {
      setSubmittedAnswer(question?.submittedAnswer||"");
    }

    setIsFlagged(question.isFlagged === 1 ? true : false);
    questionViewed(question.sequenceNum);
  }, [question]);

  const submitAnswerAndNext = async (answeredQuestion, sequenceNum) => {
    const response = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
      "updateAnswerInUserAttemptAnswerMaster",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(answeredQuestion),
      }
    );
    if (response.status === 200) {
      const data = await response.json();
      console.log(data);
      if (data.result === "success") {
        if (data.isFlagged === 1) {
          setIsFlagged(true);
        } else {
          setIsFlagged(false);
        }

        if (answeredQuestion.submittedAnswer === "") {
          setQuestions(
            questions.map((question) =>
              question.sequenceNum === sequenceNum
                ? { answeredQuestion }
                : question
            )
          );
          setSubmittedAnswer("");
        }
        setQuestions(
          questions.map((question) =>
            question.sequenceNum === sequenceNum ? answeredQuestion : question
          )
        );
      }
    }
    if (response.status === 401) {
      console.log("ClientSide error");
    }
    if (response.status === 500) {
      console.log("SERVER_ERROR");
    }
  };

  const onSave = (sequenceNum) => {
    console.log(submittedAnswer);
    questions.forEach((question) => {
      if (question.sequenceNum === sequenceNum) {
        const answeredQuestion = { ...question, submittedAnswer };
        submitAnswerAndNext(answeredQuestion, sequenceNum);
      }
    });
  };

  const onReset = (sequenceNum) => {
    questions.forEach(
      (question) =>
        question.sequenceNum === sequenceNum &&
        submitAnswerAndNext({ ...question, submittedAnswer: "" }, sequenceNum)
    );
  };

  const onMarkForReview = (sequenceNum) => {
    const flagValue = isFlagged ? 0 : 1;
    questions.forEach(
      (question) =>
        question.sequenceNum === sequenceNum &&
        submitAnswerAndNext({ ...question, isFlagged: flagValue }, sequenceNum)
    );
  };
  const moveNext = (sequenceNum) => {
    onSequenceNumClick(sequenceNum + 1);
  };
  const movePrev = (sequenceNum) => {
    onSequenceNumClick(sequenceNum - 1);
  };

  const questionViewed = (sequenceNum) => {
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, isViewed: true }
          : question
      )
    );
  };

  const showOption = (questionType) => {
    var selectedQuestionType;
    switch (questionType) {
      case "01":
        selectedQuestionType = (
          <div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionA"
                id="flexRadioDefault1"
                value={question.optionA}
                checked={"optionA" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault1">
                {question.optionA}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionB"
                id="flexRadioDefault2"
                value={question.optionB}
                checked={"optionB" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault2">
                {question.optionB}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionC"
                id="flexRadioDefault3"
                value={question.optionC}
                checked={"optionC" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault3">
                {question.optionC}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionD"
                id="flexRadioDefault4"
                checked={"optionD" === submittedAnswer}
                value={question.optionD}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault4">
                {question.optionD}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionE"
                id="flexRadioDefault5"
                value={question.optionE}
                checked={"optionE" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault5">
                {question.optionE}
              </label>
            </div>
          </div>
        );
        break;
      case "02":
        selectedQuestionType = (
          <div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="optionA"
                id="flexRadioDefault1"
                value={question.optionA}
                checked={submittedAnswer && submittedAnswer.includes("optionA")}
                onChange={(e) =>
                  setSubmittedAnswer([...submittedAnswer, e.target.name])
                }
              />
              <label className="form-check-label" htmlFor="flexRadioDefault1">
                {question.optionA}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="optionB"
                id="flexRadioDefault2"
                value={question.optionB}
                checked={submittedAnswer && submittedAnswer.includes("optionB")}
                onChange={(e) =>
                  setSubmittedAnswer([...submittedAnswer, e.target.name])
                }
              />
              <label className="form-check-label" htmlFor="flexRadioDefault2">
                {question.optionB}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="optionC"
                id="flexRadioDefault3"
                value={question.optionC}
                checked={submittedAnswer && submittedAnswer.includes("optionC")}
                onChange={(e) =>
                  setSubmittedAnswer([...submittedAnswer, e.target.name])
                }
              />
              <label className="form-check-label" htmlFor="flexRadioDefault3">
                {question.optionC}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="optionD"
                id="flexRadioDefault4"
                checked={submittedAnswer && submittedAnswer.includes("optionD")}
                value={question.optionD}
                onChange={(e) =>
                  setSubmittedAnswer([...submittedAnswer, e.target.name])
                }
              />
              <label className="form-check-label" htmlFor="flexRadioDefault4">
                {question.optionD}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                name="optionE"
                id="flexRadioDefault5"
                value={question.optionE}
                checked={submittedAnswer && submittedAnswer.includes("optionE")}
                onChange={(e) =>
                  setSubmittedAnswer([...submittedAnswer, e.target.name])
                }
              />
              <label className="form-check-label" htmlFor="flexRadioDefault5">
                {question.optionE}
              </label>
            </div>
          </div>
        );
        break;
      case "03":
        selectedQuestionType = (
          <div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionA"
                id="flexRadioDefault6"
                value={"True"}
                checked={"optionA" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault6">
                {question.optionA}
              </label>
            </div>
            <div className="form-check">
              <input
                className="form-check-input"
                type="radio"
                name="optionB"
                id="flexRadioDefault7"
                value={question.optionB}
                checked={"optionB" === submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.name)}
              />
              <label className="form-check-label" htmlFor="flexRadioDefault7">
                {question.optionB}
              </label>
            </div>
          </div>
        );
        break;
      case "04":
       
        selectedQuestionType = (
          <div>
            <div className="col-6">
              <input
               autocomplete="off"
                className="form-control"
                type="text"
                name="answer"
                value={submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.value)}
              />
              <br/>
              <br/>
              <br/>
              <br/>
              
            </div>
          </div>
        );
        break;
      case "05":
        selectedQuestionType = (
          <div>
            <div className="form-check">
              <input
               autocomplete="off"
                className="form-input"
                type="textarea"
                name="flexRadioDefault"
                id="flexRadioDefault1"
                value={submittedAnswer}
                onChange={(e) => setSubmittedAnswer(e.target.value)}
              />
            </div>
          </div>
        );
        break;
    }
    return selectedQuestionType;
  };
  return (
    <div className="col">
      <div className="border border-secondary-subtle p-3 rounded pb-5">
        <h6>Question Type : {question.questionType}</h6>
        <hr />
        <h6>Question No. {question.sequenceNum}</h6>
        <hr />
        <p>{question.questionDetail}</p>

        {showOption(question.questionType)}
      </div>

      <div className="mt-3 row">
        <div className="col-3">
          <button
            type="button"
            onClick={() => movePrev(question.sequenceNum)}
            className="btn btn-outline-success me-1  "
          >
            Previous
          </button>
        </div>
        <div className="col ">
          <div className="col-8 mx-auto">
            <button
              onClick={() => onMarkForReview(question.sequenceNum)}
              type="button"
              className="btn btn-outline-danger "
            >
              {isFlagged ? "Remove Mark" : "Mark for Review"}
            </button>
            <button
              type="button"
              onClick={() => onReset(question.sequenceNum)}
              className="btn btn-outline-primary ms-2"
            >
              Reset
            </button>

            <button
              type="button"
              onClick={() => onSave(question.sequenceNum)}
              className="btn btn-outline-success ms-2 "
            >
              Save
            </button>
          </div>
        </div>
        <div className="col-3">
          <button
            type="button"
            onClick={() => moveNext(question.sequenceNum)}
            className="btn btn-outline-success float-end"
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default Question;
