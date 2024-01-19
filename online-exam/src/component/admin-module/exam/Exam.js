import React, { useEffect } from "react";
import MainContent from "../../common/MainContent";
import AccordinMaker from "../../common/AccordinMaker";
import { useExamContext } from "./ExamData";
import { useNavigate } from "react-router-dom";
import  Alert  from "../../common/Alert";


const Exam = () => {

  const navigate = useNavigate();
  const {getExams, exams, setExams, onDelete,alert,setAlert,fetchExam} = useExamContext();

  useEffect(() => {
    getExams();
    return () => {
      setExams([]);
    };
  },[]);

  

  const onEdit = (id,exam) => {
    navigate(`edit/examId/${id}`,{state:exam});
  };

  const text = {
    header: "Exam",
    btnText: "Exam",
  };

  return (
    <div className="position-relative" >
    {alert &&<Alert color={"alert-success"} close={setAlert} message ={"Aww yeah, you successfully created Exam"}/>}
      <MainContent text={text} to="add-exam" back="/admin/exam" />
      {exams ? <AccordinMaker
        objects={exams}
        id={"examId"}
        name={"examName"}
        onDelete={onDelete}
        onEdit={onEdit}
        path={"viewExam"}
      />:"No Exams Found.Please Add Exams"}
    </div>
  );
};

export default Exam;
