import { createContext, useContext, useState } from "react";

const ExamContext = createContext(null);

export const ExamProvider  = ({children})=>{

    const [exams, setExams] = useState([]);
    const [alert,setAlert] =useState(false);

    const [refresh,setRefresh]= useState(true);

    const fetchExam = async () => {
      const res = await fetch(
        "https://localhost:8443/onlineexam/control/findAllExams",
        { credentials: 'include'}
      );
      console.log(res.status)
      if (res.status === 401) {
        console.log("ClientSide error");
      }
      if (res.status === 500) {
        console.log("SERVER_ERROR");
      }
      const data = await res.json();   
        
      return data;
    };

    //Delete Exam
    const onDelete=async(id)=>{
      const res = await fetch(`https://localhost:8443/onlineexam/control/deleteExam?examId=${id}`,
      {method:'DELETE', credentials: 'include'})
      const data =await  res.json();
      console.log(data);
      const {result} = data
      if(result==="success"){
        setExams(exams.filter(exam=>exam.examId !== id));
      }
    }

      return (
      <> <ExamContext.Provider value={{exams, setExams,onDelete,alert,setAlert,fetchExam,refresh,setRefresh}}>
        {children}
      </ExamContext.Provider></>)

}

export const useExamContext = ()=>{
    return useContext(ExamContext);
}
