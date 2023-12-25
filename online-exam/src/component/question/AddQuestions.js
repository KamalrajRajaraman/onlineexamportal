import React, { useEffect, useState } from "react";
import FormInput from "../common/FormInput";
import { useTopicContext } from "../topic/topicData";
import { useQuestionContext } from "./questionData";

const AddQuestions = () => {
  
  const {questions, setQuestions} = useQuestionContext();
  const { topics,setTopics,fetchTopic } = useTopicContext();

  const initialValue ={
    questionDetail:"",
    optionA:"",
    optionB:"",
    optionC:"",
    optionD:"",
    optionE:"",
    answer:"",
    numAnswers:"",
    questionType:"01",
    difficultyLevel:"",
    answerValue:"",
    topicId:"",
    negativeMarkValue:""
  }
  const [formValues,setFormValues] =useState(initialValue);
  const [formErrors,setFormErrors] =useState({});
  const [isSumbit,setIsSumbit] =useState(false);

  // const [questionId, setQuestionId] = useState("");
  // const [questionDetail, setQuestionDetail] = useState("");
  // const [optionA, setOptionA] = useState("");
  // const [optionB, setOptionB] = useState("");
  // const [optionC, setOptionC] = useState("");
  // const [optionD, setOptionD] = useState("");
  // const [optionE, setOptionE] = useState("");
  // const [answer, setAnswer] = useState("");
  // const [numAnswers, setNumAnswers] = useState("");
  // const [questionType, setquestionType] = useState("01");
  // const [difficultyLevel, setDifficultyLevel] = useState("");
  // const [answerValue, setAnswerValue] = useState("");
  // const [topicId, setTopicId] = useState("00");
  // const [negativeMarkValue, setNegativeMarkValue] = useState("");

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
         credentials: 'include',
        body: JSON.stringify(questionDetail)
      }
    );

    const data = await res.json();
    console.log(data)
    if(data.result==="success"){
      setFormValues(initialValue);
      const {question} =data;
      setQuestions([...questions,question])
    }
     
   
  };

  const handleChange =(e)=>{
    
    const {name,value}=e.target;
    setFormValues({...formValues,[name]:value});
   
  }

  useEffect(()=>{
    if(Object.keys(formErrors).length === 0 && isSumbit ){
      onCreateQuestion(formValues);
     
    }
  },[formErrors])
  

  const handleSubmit = (e) => {
    e.preventDefault();
    setFormErrors(validate(formValues));
    setIsSumbit(true)
   
  
    // onCreateQuestion({
    //   questionId,
    //   questionDetail,
    //   optionA,
    //   optionB,
    //   optionC,
    //   optionD,
    //   optionE,
    //   answer,
    //   numAnswers,
    //   questionType,
    //   difficultyLevel,
    //   answerValue,
    //   topicId,
    //   negativeMarkValue}
    // );

    // setQuestionId("");
    // setQuestionDetail("");
    // setOptionA("");
    // setOptionB("");
    // setOptionC("");
    // setOptionD("");
    // setOptionE("");
    // setAnswer("");
    // setNumAnswers("");
    // setquestionType("");
    // setDifficultyLevel("");
    // setAnswerValue("");
    // setTopicId("");
    // setNegativeMarkValue("");
  };

  const validate =(values)=>{
    const errors ={};
   
    if(!values.questionDetail){
      errors.questionDetail ="Question Detail Id is Required"
    }
    if(!values.optionA){
      errors.optionA ="optionA is Required"
    }
    if(!values.optionB){
      errors.optionB ="optionB is Required"
    }
    if(!values.optionC){
      errors.optionC ="optionC is Required"
    }
    if(!values.optionD){
      errors.optionD ="optionD is Required"
    }
    if(!values.optionE){
      errors.optionE ="Queestion Id is Required"
    }
    if(!values.answer){
      errors.answer ="Answer is Required"
    }
    if(!values.numAnswers){
      errors.numAnswers ="NumAnswers  is Required"
    }
    if(!values.questionType){
      errors.questionType =" Question Type is Required"
    }
    if(!values.difficultyLevel){
      errors.difficultyLevel ="difficultyLevel  is Required"
    }
    if(!values.answerValue){
      errors.answerValue ="Answer Value is Required"
    }
    if(!values.topicId){
      errors.topicId ="Topic is Required"
    }
    if(!values.negativeMarkValue){
      errors.negativeMarkValue ="Negative Mark Value is Required"
    }

    return errors;
  }
  return (

    <div className="container-fluid border my-2 fw-bold ">
    
      <form onSubmit={handleSubmit}>
        <div className="row pt-2">
          <div className="col-6">
            {/* <FormInput
              id="questionId"
              name="questionId"
              type={"text"}
              text={"Question Id"}
              value={formValues.questionId}
              onChange={handleChange}
              error={formErrors.questionId}
            /> */}
            <FormInput
              id="questionDetail"
              name="questionDetail"
              type={"text"}
              text={"Question Detail"}
              value={formValues.questionDetail}
              onChange={handleChange}
              error={formErrors.questionDetail}
            />
            <FormInput
               name="optionA"
              id="optionA"
              type={"text"}
              text={"Option A"}
              value={formValues.optionA}
              onChange={handleChange}
              error={formErrors.optionA}
            />
            <FormInput
             name="optionB"
              id="optionB"
              type={"text"}
              text={"Option B"}
              value={formValues.optionB}
              onChange={handleChange}
              error={formErrors.optionB}
            />
            <FormInput
             name="optionC"
              id="optionC"
              type={"text"}
              text={"Option C"}
              value={formValues.optionC}
              onChange={handleChange}
              error={formErrors.optionC}
            />
            <FormInput
              name="optionD"
              id="optionD"
              type={"text"}
              text={"Option D"}
              value={formValues.optionD}
              onChange={handleChange}
              error={formErrors.optionD}
            />
            <FormInput
             name="optionE"
              id="optionE"
              type={"text"}
              text={"Option E"}
              value={formValues.optionE}
              onChange={handleChange}
              error={formErrors.optionE}
            />
          </div>
          <div className="col-6">
            <FormInput
             name="answer"
              id="answer"
              type={"text"}
              text={"Answer"}
              value={formValues.answer}
              onChange={handleChange}
              error={formErrors.answer}

            />
            <FormInput
             name="numAnswers"
              id="numAnswers"
              type={"text"}
              text={"Num Answers"}
              value={formValues.numAnswers}
              onChange={handleChange}
              error={formErrors.numAnswers}
            />
            <div className="row">
            <div className="mb-3 col">
              <label htmlFor="questionType" className="form-label">
                Question Type
              </label>
              <select
                className="form-control"
                id="questionType"
                name="questionType"
                value={formValues.questionType}
                onChange={handleChange}
                
              >
                <option value="01">Single Choice</option>
                <option value="02">Multiple Choice</option>
                <option value="03">True or False</option>
                <option value="04">Fill in the Blanks</option>
                <option value="05">Detailed Answer</option>
              </select>
              <small className="text-danger">{formErrors.questionType}</small>
            </div>
            <div className="mb-3 col">
              <label htmlFor="topicId" className="form-label">
                Topic
              </label>
              <select
                className="form-control"
                id="topicId"
                name="topicId"
                value={formValues.topicId}
                onChange={handleChange}
              ><option value="">None</option>
                { topics.map((topic) => (
                  <option key={topic.topicId} value={topic.topicId}>{topic.topicName}</option>
                ))}
              </select>
              <small className="text-danger">{formErrors.topicId}</small>
            </div>


            </div>
           
            <FormInput
              id="difficultyLevel"
              name="difficultyLevel"
              type={"text"}
              text={"Difficulty Level"}
              value={formValues.difficultyLevel}
              onChange={handleChange}
              error={formErrors.difficultyLevel}
            />
            <FormInput
              id="answerValue"
              name="answerValue"
              type={"text"}
              text={"Answer Value"}
              value={formValues.answerValue}
              onChange={handleChange}
              error={formErrors.answerValue}
            />

          

            <FormInput
             name="negativeMarkValue"
              id="negativeMarkValue"
              type={"text"}
              text={"Negative Mark Value"}
              value={formValues.negativeMarkValue}
              onChange={handleChange}
              error={formErrors.negativeMarkValue}
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
