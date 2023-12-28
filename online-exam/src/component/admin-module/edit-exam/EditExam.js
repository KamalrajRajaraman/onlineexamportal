import React, { createContext, useState } from "react";
import { useParams } from "react-router-dom";
import MainContent from "../../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";

export const EditExamContext = createContext();
const EditExam = () => {
  const [examList, setExamList] = useState([]);
  const [examTopicMap, setExamTopicMap] = useState([]);
  //examId is retrieved from url 
  const { examId } = useParams();

  const initialValue ={
    examId,
    topicId:"",
    percentage:"",
    topicPassPercentage:"",
    questionsPerExam:""
  }
  //formValues is maintained here to reset the form based on the success msg
  const [formValues,setFormValues] =useState(initialValue);

  //send as props to MainContent to display respective text
  const text = {
    header: "Exam-Topic-Mapping",
    btnText: "Topic to Exam ",
  };

  //creating examtopicMapping Record 
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