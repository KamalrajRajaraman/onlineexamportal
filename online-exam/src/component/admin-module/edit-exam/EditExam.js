import React, { createContext, useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import MainContent from "../../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";
import Swal from "sweetalert2";
import ViewExam from "../exam/ViewExam";

export const EditExamContext = createContext();
const EditExam = () => {
  const examRecord =useLocation().state;
  const  [examDetails, setExamDetails] = useState(examRecord);
 
  const [examTopicMap, setExamTopicMap] = useState([]);
  //examId is retrieved from url 
  const { examId } = useParams();

 const[refresh,setRefresh]=useState(true);

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

  const editExam = (props)=>{
    fetch("https://localhost:8443/onlineexam/control/editExam",{
      method:"POST",
      headers: {
        "Content-type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(props)
    }).then((response)=>{return response.json();})
    .then((data)=>{console.log(data)})
      // if(data.result==="success"){
      //   setExamDetails(data.examDetails)
      //   }})
  }

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
      
      <ViewExam examDetails={examDetails} onEdit={editExam}/>
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
