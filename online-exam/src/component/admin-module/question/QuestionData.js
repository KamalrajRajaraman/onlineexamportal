import { createContext, useContext, useState } from "react";
import { PUT,CONTROL_SERVLET, DELETE, DOMAIN_NAME, GET, PORT_NO, PROTOCOL, WEB_APPLICATION, vanishAlert, swalFireAlert } from "../../common/CommonConstants";

import { useNavigate } from "react-router-dom";
const QuestionContext = createContext(null);

export const QuestionProvider =({children})=>{
  const navigate = useNavigate();
    const [questions, setQuestions] = useState([]);

    const onDelete=async(id)=>{
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`deleteQuestion?questionId=${id}`,
      DELETE);
      if (res.status === 401) {
        navigate("/login");
      }
      const data = await res.json();
      const {result} = data
      if(result==="success"){
        vanishAlert("Good job!","Question Deleted successfully!","success",2000,false);
        fetchQuestion();
      }
      else if (result==="error"){
        swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
      }
    }

    const fetchQuestion = async () => {
      const res = await fetch(
        PROTOCOL +
          DOMAIN_NAME +
          PORT_NO +
          WEB_APPLICATION +
          CONTROL_SERVLET +
          "findAllQuestions",GET
       
      );
      const data = await res.json();
  
      if (data.result === "success") {
        const questionList = data.questionList;
        setQuestions(questionList);
      }
       else if (data.result==="error"){
        swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
      }
    };

    const onEdit=async(id, object)=>{
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`createQuestion`,
      {...PUT, body: JSON.stringify(object)}
      )

      if (res.status === 401) {
        navigate("/login");
      }
      const data = await res.json();
      const {result} = data
      
      if(result==="success"){
        document.getElementById("modal-close").click();
        fetchQuestion();
        vanishAlert("Good job!", "question updated  successfully!","success", 2000, false);   

      } else if (data.result==="error"){
        swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
      }
      
    }
    
    return<QuestionContext.Provider value={{questions,setQuestions,onDelete,onEdit,fetchQuestion}}>
        {children}
    </QuestionContext.Provider>
     
}

export const useQuestionContext=()=>{
    return useContext(QuestionContext);
}
