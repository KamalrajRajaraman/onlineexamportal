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

 

  // optionA: "When a class is made final, a subclass of it can not be created."
  // optionB: " When a method is final, it can not be overridden"
  // optionC: "When a variable is final, it can be assigned value only once"
  // optionD: "All the above"
  // optionE: "none"
  // performanceId: 10461
  // questionDetail: "What is the use of final keyword in Java?"
  // questionId: 10280
  // questionType: "01"
  // sequenceNum: 1
  // submittedAnswer: "When a class is made final, a subclass of it can not be created."
  // topicId: "10270"

  const submitAnswerAndNext = async (answeredQuestion, sequenceNum) => {
    const response = await fetch(
      "https://localhost:8443/onlineexam/control/updateAnswerInUserAttemptAnswerMaster",
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
   const flagValue = question?.isFlagged?0:1;
    questions.forEach(
      (question) =>
        question.sequenceNum === sequenceNum &&
        submitAnswerAndNext({ ...question, isFlagged:flagValue }, sequenceNum)
    );
  
    onSequenceNumClick(sequenceNum);
  };

  return (
    <div className="col">
      <div className="border border-secondary-subtle p-3 rounded pb-5">
        <h6>Question Type : {question.questionType}</h6>
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

      <div className="mt-3 row">
        <div className="col-3">
          <button
            type="button"
            onClick={() => onSequenceNumClick(question.sequenceNum - 1)}
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
              className="btn btn-outline-primary "
            >
              {question?.isFlagged?"Remove Mark":"Mark for Review"}
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
            onClick={() => onSequenceNumClick(question.sequenceNum + 1)}
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
