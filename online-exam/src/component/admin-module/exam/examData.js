import { createContext, useContext, useState } from "react";

const ExamContext = createContext(null);

export const ExamProvider  = ({children})=>{

    const [exams, setExams] = useState([]);
    const [alert,setAlert] =useState(false);

    const fetchExam = async () => {
      const res = await fetch(
        "https://localhost:8443/onlineexam/control/findAllExams",
        { credentials: 'include'}
      );
      const data = await res.json();
      console.log(data);
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

      return (<> <ExamContext.Provider value={{exams, setExams,onDelete,alert,setAlert,fetchExam}}>
        {children}
      </ExamContext.Provider></>)

}

export const useExamContext = ()=>{
    return useContext(ExamContext);
}