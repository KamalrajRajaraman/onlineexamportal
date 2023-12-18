import React, { useEffect, useState } from "react";
import Input from "../Input";
import { useTopicContext } from "../topic/topicData";
import { useQuestionContext } from "./questionData";

const AddQuestions = () => {
  
  const {questions, setQuestions} = useQuestionContext();
  const { topics,setTopics,fetchTopic } = useTopicContext();

  const [questionId, setQuestionId] = useState("");
  const [questionDetail, setQuestionDetail] = useState("");
  const [optionA, setOptionA] = useState("");
  const [optionB, setOptionB] = useState("");
  const [optionC, setOptionC] = useState("");
  const [optionD, setOptionD] = useState("");
  const [optionE, setOptionE] = useState("");
  const [answer, setAnswer] = useState("");
  const [numAnswers, setNumAnswers] = useState("");
  const [questionType, setquestionType] = useState("01");
  const [difficultyLevel, setDifficultyLevel] = useState("");
  const [answerValue, setAnswerValue] = useState("");
  const [topicId, setTopicId] = useState("00");
  const [negativeMarkValue, setNegativeMarkValue] = useState("");

  useEffect(() => {
     getTopics();
    
    return () => {
      setTopics([]);
    };
  }, []);

  const getTopics = async () => {
    const topicList = await fetchTopic();
    setTopics([...topicList]);
   
  };

  const onCreateQuestion = async (questionDetail) => {
   console.log(questionDetail)
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/createQuestion",
      {
        method: "POST",
        headers: {
          'Content-type':"application/json"
        },
        body: JSON.stringify(questionDetail)
      }
    );

    const data = await res.json();
    console.log(data)
      const {question} =data;
      setQuestions([...questions,question])
    console.log(data);
  };


  const onSubmit = (e) => {
    e.preventDefault();
    onCreateQuestion({
      questionId,
      questionDetail,
      optionA,
      optionB,
      optionC,
      optionD,
      optionE,
      answer,
      numAnswers,
      questionType,
      difficultyLevel,
      answerValue,
      topicId,
      negativeMarkValue}
    );

    setQuestionId("");
    setQuestionDetail("");
    setOptionA("");
    setOptionB("");
    setOptionC("");
    setOptionD("");
    setOptionE("");
    setAnswer("");
    setNumAnswers("");
    setquestionType("");
    setDifficultyLevel("");
    setAnswerValue("");
    setTopicId("");
    setNegativeMarkValue("");
  };
  return (
    <div className="container-fluid border my-2 fw-bold ">
    
      <form onSubmit={onSubmit}>
        <div className="row pt-2">
          <div className="col-6">
            <Input
              name="questionId"
              type={"text"}
              text={"Question Id"}
              state={questionId}
              setStateFun={setQuestionId}
            />
            <Input
              name="questionDetail"
              type={"text"}
              text={"Question Detail"}
              state={questionDetail}
              setStateFun={setQuestionDetail}
            />
            <Input
              name="optionA"
              type={"text"}
              text={"Option A"}
              state={optionA}
              setStateFun={setOptionA}
            />
            <Input
              name="optionB"
              type={"text"}
              text={"Option B"}
              state={optionB}
              setStateFun={setOptionB}
            />
            <Input
              name="optionC"
              type={"text"}
              text={"Option C"}
              state={optionC}
              setStateFun={setOptionC}
            />
            <Input
              name="optionD"
              type={"text"}
              text={"Option D"}
              state={optionD}
              setStateFun={setOptionD}
            />
            <Input
              name="optionE"
              type={"text"}
              text={"Option E"}
              state={optionE}
              setStateFun={setOptionE}
            />
          </div>
          <div className="col-6">
            <Input
              name="answer"
              type={"text"}
              text={"Answer"}
              state={answer}
              setStateFun={setAnswer}
            />
            <Input
              name="numAnswers"
              type={"text"}
              text={"Num Answers"}
              state={numAnswers}
              setStateFun={setNumAnswers}
            />

            <div className="mb-3">
              <label htmlFor="questionType" className="form-label">
                Question Type
              </label>
              <select
                className="form-control"
                id="questionType"
                value={questionType}
                onChange={(e) => setquestionType(e.target.value)}
              >
                <option value="01">Single Choice</option>
                <option value="02">Multiple Choice</option>
                <option value="03">True or False</option>
                <option value="04">Fill in the Blanks</option>
                <option value="05">Detailed Answer</option>
              </select>
            </div>

            <Input
              name="difficultyLevel"
              type={"text"}
              text={"Difficulty Level"}
              state={difficultyLevel}
              setStateFun={setDifficultyLevel}
            />
            <Input
              name="answerValue"
              type={"text"}
              text={"Answer Value"}
              state={answerValue}
              setStateFun={setAnswerValue}
            />

            <div className="mb-3">
              <label htmlFor="topicId" className="form-label">
                Topic
              </label>
              <select
                className="form-control"
                id="topicId"
                value={topicId}
                onChange={(e) => setTopicId(e.target.value)}
              ><option value="00">None</option>
                { topics.map((topic) => (
                  <option key={topic.topicId} value={topic.topicId}>{topic.topicName}</option>
                ))}
              </select>
            </div>

            <Input
              name="negativeMarkValue"
              type={"text"}
              text={"Negative Mark Value"}
              state={negativeMarkValue}
              setStateFun={setNegativeMarkValue}
            />
          </div>

          <div className="col-2 mx-auto  d-grid gap-2 ">
            <button className="btn btn-primary" type="submit">
              Submit
            </button>
          </div>
        </div>
      </form>
      
    </div>
  );
};

export default AddQuestions;
