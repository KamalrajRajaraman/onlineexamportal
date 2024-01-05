import { createContext, useContext, useState } from "react";

const QuestionContext = createContext(null);

export const QuestionProvider =({children})=>{
    const [questions, setQuestions] = useState([]);

    const onDelete=async(id)=>{
      const res = await fetch(`https://localhost:8443/onlineexam/control/deleteQuestion?questionId=${id}`,{method:'DELETE', credentials: 'include'})
      const data = await res.json();
      const {result} = data
      if(result==="success"){
        setQuestions(questions.filter(question=>question.questionId!==id));
      }
    }
    
    return<QuestionContext.Provider value={{questions,setQuestions,onDelete}}>
        {children}
    </QuestionContext.Provider>
     
}

export const useQuestionContext=()=>{
    return useContext(QuestionContext);
}
