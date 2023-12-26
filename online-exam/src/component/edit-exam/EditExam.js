import React, { createContext, useEffect, useState } from "react";
import { Link, Outlet, useNavigate, useParams } from "react-router-dom";
import MainContent from "../common/MainContent"
import ExamTopicTable from "./ExamTopicTable";
import AccordinMaker from "../common/AccordinMaker"

export const EditExamContext = createContext();

const EditExam = () => {
  const [examList, setExamList] = useState([]);
  const [examTopicMap, setExamTopicMap] = useState([]);

  const { examId } = useParams();
  const navigate = useNavigate();
  const initialValue ={
    examId,
    topicId:"",
    percentage:"",
    topicPassPercentage:"",
    questionsPerExam:""
  }
  const [formValues,setFormValues] =useState(initialValue);
  const text = {
    header: "Exam-Topic-Mapping",
    btnText: "Topic to Exam ",
  };

  // useEffect(() => {
  //   fetchExamList();
  //   return () => {
  //     setExamList([]);
  //   };
  // }, []);

  // const fetchExamList = async () => {
  //   const res = await fetch(
  //     `https://localhost:8443/onlineexam/control/getExamDetails?examId=${examId}`,
  //     { credentials: "include" }
  //   );
  //   const { examList } = await res.json();
  //   console.log(examList);
  //   setExamList(examList);
  //   return;
  // };

  const onCreateExamTopicMappingMaster = async (examTopicMappingDetails) => {
    console.log(examTopicMappingDetails);
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/createExamTopicMappingMasterRecord",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(examTopicMappingDetails),
      }
    );
    const data = await res.json();
    if(data.result==="success"){
      setFormValues(initialValue);
    const { examTopicMappingMasterRecord } = data;
    setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);

    }
  };

  return (
    <EditExamContext.Provider
      value={{
        examList,
        examId,
        onCreateExamTopicMappingMaster,
        examTopicMap,
        setExamTopicMap,
        setExamList,
        formValues,
        setFormValues
        // fetchExamList,
      }}
    >
      {/* <div className="row">
        <div className="col p-0 border-bottom border-3 border-dark">
          <h2 className="p-2">Exam Details</h2>
        </div>
      </div>
      <nav>
        <div
          className="nav nav-tabs mt-2 border navbar-light bg-light fw-bold   px-2 pt-2"
          id="nav-tab"
          role="tablist"
        >
          <button
            className="nav-link text-dark active"
            id="nav-view-all-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-view-all"
            type="button"
            role="tab"
           
            onClick={() => navigate("view-all")}
          >
            Exam To Topic Relation
          </button>
          <button
            className="nav-link  text-dark "
            id="nav-add-topic-to-exam-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-add-topic-to-exam"
            type="button"
            role="tab"
            
            onClick={() => navigate("add-topic-to-exam")}
          >
            Add Topic to Exam
          </button>

          <button
            className="nav-link text-dark"
            id="nav-questions-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-questions"
            type="button"
            role="tab"
           
            onClick={() => navigate("questions")}
          >
            Questions
          </button>
        </div>
      </nav>

      <div className="tab-content" id="nav-tabContent">
        <div
          class="tab-pane fade show active"
          id="nav-view-all"
          role="tabpanel"
        >
          <Outlet />
        </div>
        <div className="tab-pane fade" id="nav-add-topic-to-exam" role="tabpanel">
          <Outlet />
        </div>

        <div className="tab-pane fade" id="nav-questions" role="tabpanel">
          <Outlet />
        </div>
      </div> */}

      <MainContent
        text={text}
        to="add-topic-to-Exam"
        back={`/admin/exam/edit/examId/${examId}`}
      />
      <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>
      {/* <AccordinMaker
        objects={[]}
        id={"topicId"}
        name={"topicName"}
        onDelete={""}
        onEdit={""}
        questions={examList}
      /> */}
    </EditExamContext.Provider>
  );
};

export default EditExam;
