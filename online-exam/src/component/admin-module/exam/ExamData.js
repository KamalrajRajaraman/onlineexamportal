import { createContext, useContext, useState } from "react";
import {
  CONTROL_SERVLET,
  DELETE,
  DOMAIN_NAME,
  GET,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
  swalFireAlert,
  vanishAlert,
} from "../../common/CommonConstants";

import { useNavigate } from "react-router-dom";
const ExamContext = createContext(null);

export const ExamProvider = ({ children }) => {
  const navigate = useNavigate();
  const [exams, setExams] = useState([]);
  const [alert, setAlert] = useState(false);

  const [refresh, setRefresh] = useState(true);


  const getExams = async () => {
    const dataFetched = await fetchExam();
    if(dataFetched.result==="success"){
      const examList = dataFetched.examList;
      setExams(examList);
    }else{
      setExams(null);
    } 
  };

  const fetchExam = async () => {
    const res = await fetch( PROTOCOL + DOMAIN_NAME +PORT_NO + WEB_APPLICATION +
                            CONTROL_SERVLET +"findAllExams",GET );  
    if (res.status === 401) {
      navigate("/login");
    }
    else if (res.status === 500) {
      console.log("SERVER_ERROR");
    }
   
    const data = await res.json();
   if (data.result==="error"){
    swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
    }
    return data;

  };

  //Delete Exam
  const onDelete = async (id) => {
    const res = await fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        `deleteExam?examId=${id}`,DELETE
     
    );
    const data = await res.json();
    const { result } = data;
    if (result === "success") {
      vanishAlert( "Good job!", "Exam Deleted successfully!","success",2000,false);
      getExams();
    }
    else if (result==="error"){
      swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
    }
  };

  return (
    <>
      <ExamContext.Provider
        value={{
          getExams,
          exams,
          setExams,
          onDelete,
          alert,
          setAlert,
          fetchExam,
          refresh,
          setRefresh,
        }}
      >
        {children}
      </ExamContext.Provider>
    </>
  );
};

export const useExamContext = () => {
  return useContext(ExamContext);
};
