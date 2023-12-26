import React, { useEffect, useState } from "react";

const Question = ({
  question,
  onSequenceNumClick,
  setQuestions,
  questions,
}) => {
  const [answer, setAnswer] = useState("");

  useEffect(() => {
    setAnswer(question?.answer);
  }, [question]);

  const onSave = (sequenceNum) => {
    onSequenceNumClick(sequenceNum + 1 );
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, answer }
          : question
      )
    );
    console.log(questions);
  };

  const onReset = (sequenceNum) => {
    setQuestions(
      questions.map((question) =>
        question.sequenceNum === sequenceNum
          ? { ...question, answer: null }
          : question
      )
    );
    setAnswer("");
  };

  return (
    <div className="col">
      <div className="border border-secondary-subtle p-3 rounded pb-5">
        <h6 >Question Type : {question.questionType}</h6>
        <hr />
        <h6>Question No. {question.sequenceNum}</h6>
        <hr />
        <p>{question.questionDetails}</p>
        <div>
          <div className="form-check">
            <input
              className="form-check-input"
              type="radio"
              name="flexRadioDefault"
              id="flexRadioDefault1"
              value={question.optionA}
              checked={question.optionA === answer}
              onChange={(e) => setAnswer(e.target.value)}
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
              checked={question.optionB === answer}
              onChange={(e) => setAnswer(e.target.value)}
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
              checked={question.optionC === answer}
              onChange={(e) => setAnswer(e.target.value)}
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
              checked={question.optionD === answer}
              value={question.optionD}
              onChange={(e) => setAnswer(e.target.value)}
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
              checked={question.optionE === answer}
              onChange={(e) => setAnswer(e.target.value)}
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
        <button type="button" className="btn btn-outline-primary me-1">
          Mark for Review
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
