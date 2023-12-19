import React, { useEffect, useState } from "react";
import MainContent from "../common/MainContent";
import AccordinMaker from "../common/AccordinMaker";
import { useExamContext } from "./examData";
import { useNavigate } from "react-router-dom";
import  Alert  from "../common/Alert";

const Exam = () => {
  const navigate = useNavigate();
  const { exams, setExams, onDelete,alert,setAlert} = useExamContext();

  useEffect(() => {
    fetchExam();
    return () => {
      setExams([]);
    };
  }, []);

  const fetchExam = async () => {
    const result = await getExams();
    const examList = result.examList;
    setExams([...exams, ...examList]);
  };

  const getExams = async () => {
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/findAllExams"
    );
    const data = await res.json();
    return data;
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
