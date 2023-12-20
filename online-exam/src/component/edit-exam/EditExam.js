import React, { createContext, useEffect, useState } from "react";
import { Link, Outlet, useNavigate, useParams } from "react-router-dom";


export const EditExamContext = createContext();

const EditExam = () => {
  const [examList, setExamList] = useState([]);

  const [examTopicMap, setExamTopicMap] = useState([]);

  const { examId } = useParams();
  const navigate = useNavigate();
  const text = {
    header: "Exam-Topic-Mapping",
    btnText: "Topic to Exam ",
  };

  useEffect(() => {
    fetchExamList();
    return () => {
      setExamList([]);
    };
  }, []);

  const fetchExamList = async () => {
    const res = await fetch(
      `https://localhost:8443/onlineexam/control/getExamDetails?examId=${examId}`,{credentials: 'include'}
    );
    const { examList } = await res.json();
    setExamList(examList);
    return;
  };

  const onCreateExamTopicMappingMaster = async (examTopicMappingDetails) => {
    console.log(examTopicMappingDetails);
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/createExamTopicMappingMasterRecord",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        credentials: 'include',
        body: JSON.stringify(examTopicMappingDetails),
      }
    );
    const data = await res.json();
    const { examTopicMappingMasterRecord } = data;
    setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);
  };

 
  return (
    <EditExamContext.Provider
      value={{examList, examId, onCreateExamTopicMappingMaster,examTopicMap,setExamTopicMap,setExamList,fetchExamList }}
    >
      <div className="row">
        <div className="col p-0 border-bottom border-3 border-dark">
          <h2 className="p-2">Exam Details</h2>
        </div>
      </div>
      <nav>
        <div class="nav nav-tabs mt-2 border navbar-light bg-light fw-bold   px-2 pt-2" id="nav-tab" role="tablist">
        
          <button
            class="nav-link  text-dark active"
            id="nav-home-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-home"
            type="button"
            role="tab"
            aria-controls="nav-home"
            aria-selected="true"
            onClick={()=>navigate("view-all")}
          >
            
           Exam To Topic Relation
          </button>
          <button
            class="nav-link  text-dark "
            id="nav-profile-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-profile"
            type="button"
            role="tab"
            aria-controls="nav-profile"
            aria-selected="false"
            onClick={()=>navigate("add-topic-to-exam")}
          >
            Add Topic to Exam
          </button>

          <button
             class="nav-link text-dark"
            id="nav-profile-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-profile"
            type="button"
            role="tab"
            aria-controls="nav-profile"
            aria-selected="false"
            onClick={()=>navigate("questions")}
          >
            Questions
          </button>
          
        </div>
      </nav>
    
      <div class="tab-content" id="nav-tabContent">
        <div
          class="tab-pane fade show active"
          id="nav-home"
          role="tabpanel"
          aria-labelledby="nav-home-tab"
        >
          <Outlet/>
        </div>
        <div
          class="tab-pane fade"
          id="nav-profile"
          role="tabpanel"
          aria-labelledby="nav-profile-tab"
        >
         <Outlet/>
        </div>

        <div
          class="tab-pane fade"
          id="nav-profile"
          role="tabpanel"
          aria-labelledby="nav-profile-tab"
        >
         <div>Topic</div>
         <div>What is Method Overloading?</div>
        </div>
       
      </div>

      {/* <MainContent
        text={text}
        to="Topic to Exam"
        back={`/admin/exam/edit/examId/${examId}`}
      />
      <ExamTopicTable examId={examId} examTopicMap={examTopicMap} setExamTopicMap={setExamTopicMap}/>
      <AccordinMaker
        objects={topicList(examList)}
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
