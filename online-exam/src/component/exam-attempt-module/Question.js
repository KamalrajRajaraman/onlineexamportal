import React, { useEffect, useState } from "react";

const Question = ({
  question,
  onSequenceNumClick,
  setQuestions,
  questions,
}) => {
  const [submittedAnswer, setSubmittedAnswer] = useState("");

  useEffect(() => {
    setSubmittedAnswer(question?.submittedAnswer);
  }, [question]);

  const onSave = (sequenceNum) => {
    onSequenceNumClick(sequenceNum + 1 );
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, submittedAnswer }
          : question
      )
    );
  
  };

  const onReset = (sequenceNum) => {
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, submittedAnswer: null }
          : question
      )
    );
    setSubmittedAnswer("");
  };

  const onMarkForReview =(sequenceNum)=>{
    if(question?.isFlagged){
      setQuestions(
        questions.map((question) =>
          question.sequenceNum === sequenceNum
            ? { ...question, isFlagged :false }
            : question
        )
      );
    }else{
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, isFlagged :true }
          : question
      )
    );
    }
  }

  return (
    <div className="col">
      <div className="border border-secondary-subtle p-3 rounded pb-5">
        <h6 >Question Type : {question.questionType}</h6>
        <hr />
        <h6>Question No. {question.sequenceNum}</h6>
        <hr />
        <p>{question.questionDetail}</p>
        <div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault1"
              value={question.optionA}
              checked={question.optionA === submittedAnswer}
              onChange={(e) => setSubmittedAnswer(e.target.value)}
            />
            <label className="form-check-label" htmlFor="flexRadioDefault1">
              {question.optionA}
            </label>
          </div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault2"
              value={question.optionB}
              checked={question.optionB === submittedAnswer}
              onChange={(e) => setSubmittedAnswer(e.target.value)}
            />
            <label className="form-check-label" htmlFor="flexRadioDefault2">
              {question.optionB}
            </label>
          </div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault3"
              value={question.optionC}
              checked={question.optionC === submittedAnswer}
              onChange={(e) => setSubmittedAnswer(e.target.value)}
            />
            <label className="form-check-label" htmlFor="flexRadioDefault3">
              {question.optionC}
            </label>
          </div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault4"
              checked={question.optionD === submittedAnswer}
              value={question.optionD}
              onChange={(e) => setSubmittedAnswer(e.target.value)}
            />
            <label className="form-check-label" htmlFor="flexRadioDefault4">
              {question.optionD}
            </label>
          </div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault4"
              value={question.optionE}
              checked={question.optionE === submittedAnswer}
              onChange={(e) => setSubmittedAnswer(e.target.value)}
            />
            <label className="form-check-label" htmlFor="flexRadioDefault4">
              {question.optionE}
            </label>
          </div>
        </div>
      </div>

      <div className="mt-3">
        <button
          type="button"
          onClick={() => onSequenceNumClick(question.sequenceNum - 1)}
          className="btn btn-outline-success me-1  "
        >
          Previous
        </button>
        <button   onClick={() => onMarkForReview(question.sequenceNum)}  type="button" className={`btn me-1 ${question?.isFlagged ?" btn-outline-danger":" btn-outline-primary"}`}>
          {question?.isFlagged ? "Remove Marked":"Mark for Review"}
        </button>
        <button
          type="button"
          onClick={() => onReset(question.sequenceNum)}
          className="btn btn-outline-primary"
        >
          Reset
        </button>

        <button
          type="button"
          onClick={() => onSave(question.sequenceNum)}
          className="btn btn-outline-success float-end"
        >
          Save & Next
        </button>
      </div>
    </div>
  );
};

export default Question;
