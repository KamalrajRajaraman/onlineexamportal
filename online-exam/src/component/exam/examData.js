import { createContext, useContext, useState } from "react";

const ExamContext = createContext(null);

export const ExamProvider  = ({children})=>{

    const [exams, setExams] = useState([]);
    const [alert,setAlert] =useState(false);

    //Delete Exam
    const onDelete=async(id)=>{
      const res = await fetch(`https://localhost:8443/onlineexam/control/deleteExam?examId=${id}`,{method:'DELETE'})
      const data =await  res.json();
      const response = data.result.responseMessage
      if(response==="success"){
        setExams(exams.filter(exam=>exam.examId !== id));
      }
    }

      return <ExamContext.Provider value={{exams, setExams,onDelete,alert,setAlert}}>
        {children}
      </ExamContext.Provider>

}

export const useExamContext = ()=>{
    return useContext(ExamContext);
}