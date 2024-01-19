import { createContext, useContext, useState } from "react";
import { CONTROL_SERVLET, DOMAIN_NAME, GET, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
import Swal from "sweetalert2";
const QuestionContext = createContext(null);

export const QuestionProvider =({children})=>{
    const [questions, setQuestions] = useState([]);

    const onDelete=async(id)=>{
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`deleteQuestion?questionId=${id}`,
      {method:'DELETE', credentials: 'include'})
      const data = await res.json();
      const {result} = data
      if(result==="success"){
        Swal.fire({
          title: "Good job!",
          text: "Question Deleted successfully!",
          icon: "success",
          timer:2000,
          showConfirmButton:false});
        fetchQuestion();

        // setQuestions(questions.filter(question=> question.questionId!==id));
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
    };

    const onEdit=async(id, object)=>{
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`createQuestion`,
      { method: "PUT",
      headers: {
        'Content-type':"application/json"
      },
       credentials: 'include',
      body: JSON.stringify(object)})
      const data = await res.json();
      const {result} = data
      
      if(result==="success"){
        document.getElementById("modal-close").click();
        fetchQuestion();
        Swal.fire({
          title: "Good job!",
          text: "question updated  successfully!",
          icon: "success",
          timer: 2000,
          showConfirmButton: false,
        });   

      }
      
    }
    
    return<QuestionContext.Provider value={{questions,setQuestions,onDelete,onEdit,fetchQuestion}}>
        {children}
    </QuestionContext.Provider>
     
}

export const useQuestionContext=()=>{
    return useContext(QuestionContext);
}
