import React, { createContext, useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import MainContent from "../../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";
import Swal from "sweetalert2";
import ViewExam from "../exam/ViewExam";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";

export const EditExamContext = createContext();
const EditExam = () => {
  //examId is retrieved from url 
  const { examId } = useParams();
  const examRecord =useLocation().state;
  const  [examDetails, setExamDetails] = useState(examRecord);
  const [examTopicMap, setExamTopicMap] = useState([]);
  const[refresh,setRefresh]=useState(true);

  //used for adding topic to exam
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
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
      "createExamTopicMappingMasterRecord",
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
      Swal.fire({
        title: "Good job!",
        text: "Topic is added to exam created successfully!",
        icon: "success",
        timer:2000,
        showConfirmButton:false
      });
      
      setFormValues(initialValue);
      const { examTopicMappingMasterRecord } = data;
      setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);
    }
    else if (data.result==="error"){
      Swal.fire({
        icon: "error",
        title: "Error",
        text: data._ERROR_MESSAGE_,
      });
    }
  };

  return (
    <EditExamContext.Provider
      value={{
        examId,
        onCreateExamTopicMappingMaster,
        examTopicMap,
        setExamTopicMap,
        formValues,
        setFormValues,
        refresh,
        setRefresh
       
      }}
    >
      
      <ViewExam examDetails={examDetails} setExamDetails={setExamDetails} />
      <MainContent
        text={text}
        to="add-topic-to-Exam"
        back={`/admin/exam/edit/examId/${examId}`}
      />
     { examTopicMap.length ? <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>:"No Topic mapped to Exam.Please Add Topic To This Exam"}
     
    </EditExamContext.Provider>
  );
};

export default EditExam;
