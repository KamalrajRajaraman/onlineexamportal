import React, { createContext, useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import MainContent from "../../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";
import ViewExam from "../exam/ViewExam";
import { swalFireAlert ,POST, CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, vanishAlert, WEB_APPLICATION } from "../../common/CommonConstants";


export const EditExamContext = createContext();

const EditExam = () => {
  const navigate = useNavigate();
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
      "createExamTopicMappingMasterRecord",{...POST,  body: JSON.stringify(examTopicMappingDetails)}
    );

    //Unauthorized
    if (res.status === 401) {
      navigate("/login");
    }
    
    const data = await res.json();

    if(data.result==="success"){
      vanishAlert( "Good job!","Topic is added to exam created successfully!", "success",2000,false);
      
      setFormValues(initialValue);
      const { examTopicMappingMasterRecord } = data;
      setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);
    }
    else if (data.result==="error"){
      swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
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
     <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>
     
    </EditExamContext.Provider>
  );
};

export default EditExam;
