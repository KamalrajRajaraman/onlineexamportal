import React, { useEffect } from "react";
import MainContent from "../../common/MainContent";
import AccordinMaker from "../../common/AccordinMaker";
import { useExamContext } from "./examData";
import { useNavigate } from "react-router-dom";
import  Alert  from "../../common/Alert";

const Exam = () => {

  const navigate = useNavigate();
  const { exams, setExams, onDelete,alert,setAlert,fetchExam} = useExamContext();

  useEffect(() => {
    getExams();
    return () => {
      setExams([]);
    };
  }, []);

  const getExams = async () => {
    const result = await fetchExam();
    const examList = result.examList;
    setExams(examList);
  };

  const onEdit = (id) => {
    navigate(`edit/examId/${id}`);
  };
  
  const text = {
    header: "Exam",
    btnText: "Exam",
  };

  return (
    <div className="position-relative" >
    {alert &&<Alert color={"alert-success"} close={setAlert} message ={"Aww yeah, you successfully created Exam"}/>}
      <MainContent text={text} to="add-exam" back="/admin/exam" />
      <AccordinMaker
        objects={exams}
        id={"examId"}
        name={"examName"}
        onDelete={onDelete}
        onEdit={onEdit}
      />
    </div>
  );
};

export default Exam;
