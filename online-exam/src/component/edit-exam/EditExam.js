import React, { createContext, useEffect, useState } from "react";
import { Link, Outlet, useNavigate, useParams } from "react-router-dom";
import MainContent from "../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";
import AccordinMaker from "../common/AccordinMaker"

export const EditExamContext = createContext();

const EditExam = () => {
  const [examList, setExamList] = useState([]);
  const [examTopicMap, setExamTopicMap] = useState([]);

  const { examId } = useParams();
  const navigate = useNavigate();
  const initialValue ={
    examId,
    topicId:"",
    percentage:"",
    topicPassPercentage:"",
    questionsPerExam:""
  }
  const [formValues,setFormValues] =useState(initialValue);
  const text = {
    header: "Exam-Topic-Mapping",
    btnText: "Topic to Exam ",
  };


  const onCreateExamTopicMappingMaster = async (examTopicMappingDetails) => {
    console.log(examTopicMappingDetails);
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/createExamTopicMappingMasterRecord",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(examTopicMappingDetails),
      }
    );
    const data = await res.json();
    if(data.result==="success"){
      setFormValues(initialValue);
    const { examTopicMappingMasterRecord } = data;
    setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);

    }
  };

  return (
    <EditExamContext.Provider
      value={{
        examList,
        examId,
        onCreateExamTopicMappingMaster,
        examTopicMap,
        setExamTopicMap,
        setExamList,
        formValues,
        setFormValues
       
      }}
    >
      

      <MainContent
        text={text}
        to="add-topic-to-Exam"
        back={`/admin/exam/edit/examId/${examId}`}
      />
      <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>
     
    </EditExamContext.Provider>
  );
};

export default EditExam;
