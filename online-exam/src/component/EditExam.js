import React, { createContext, useState } from "react";
import MainContent from "./MainContent";
import { useParams } from "react-router-dom";
import ExamTopicTable from "./edit-exam/ExamTopicTable";
export const EditExamContext = createContext();
const EditExam = () => {
  const [examTopicMap,setExamTopicMap]=useState([]);
  const { examId } = useParams();
  const text = {
    header: "Exam-Topic-Mapping",
    btnText: "Topic to Exam ",
  };

  const  onCreateExamTopicMappingMaster =async (examTopicMappingDetails)=>{
    console.log(examTopicMappingDetails);
  const res = await fetch("https://localhost:8443/onlineexam/control/createExamTopicMappingMasterRecord",{
    method:"POST",
    headers:{
      'Content-type':"application/json"
    },
    body:JSON.stringify(examTopicMappingDetails)

  });
  const data = await res.json();

  const {examTopicMappingMasterRecord}=data;

  setExamTopicMap([...examTopicMap,examTopicMappingMasterRecord])

  
}
  
  return (
    <EditExamContext.Provider value={{examId,onCreateExamTopicMappingMaster}}>
      <MainContent
        text={text}
        to="Topic to Exam"
        back={`/admin/exam/edit/examId/${examId}`}
      />
      <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>
    </EditExamContext.Provider>
  );
};

export default EditExam;
