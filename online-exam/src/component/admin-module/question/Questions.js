import React, { useEffect, useState } from "react";
import MainContent from "../../common/MainContent";
import AccordinMaker from "../../common/AccordinMaker";
import { useQuestionContext } from "./QuestionData";
import FormInput from "../../common/FormInput";
import { useTopicContext } from "../topic/TopicData";
import { useNavigate } from "react-router-dom";
import EditQuestion from "./EditQuestion";


const Questions = () => {
 
  const [questionId, setQuestionId] = useState([]);
  const { question,setQuestion,questions, setQuestions, onDelete, onEdit ,fetchQuestion} = useQuestionContext();
  const { getTopics, topics, setTopics, fetchTopic } = useTopicContext();
 
  const initialValue = {
    questionId: "",
    questionDetail: "",
    optionA: "",
    optionB: "",
    optionC: "",
    optionD: "",
    optionE: "",
    answer: "",
    numAnswers: "",
    questionType: "01",
    difficultyLevel: "",
    answerValue: "",
    topicId: "",
    negativeMarkValue: "",
  };
  const [formValues, setFormValues] = useState(initialValue);

  useEffect(() => {
    fetchQuestion();
    return () => {
      setQuestions([]);
    };
  }, []);

  useEffect(() => {
    getTopics();
    return () => {
      setTopics([]);
    };
  }, []);

 

  const modalEdit = async (id, question) => {
    setQuestion(question);
  };

  

 

  const text = {
    header: "Questions",
    btnText: "Questions",
  };

  return (
    <>
      <MainContent text={text} to="add-questions" back="/admin/questions" />
      {questions.length ? <AccordinMaker
        objects={questions}
        id={"questionId"}
        name={"questionDetail"}
        onEdit={modalEdit}
        onDelete={onDelete}
      />:"No Questions.Please Add Question"}
       {question && <EditQuestion question={question} setQuestion={setQuestion}/>}
    </>
  );
};

export default Questions;
