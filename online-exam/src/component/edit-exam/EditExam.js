import React, { createContext, useEffect, useState } from "react";
import MainContent from "../common/MainContent";
import { useParams } from "react-router-dom";
import ExamTopicTable from "./ExamTopicTable";
import AccordinMaker from "./AccordinMaker";

export const EditExamContext = createContext();
const EditExam = () => {

  const [examList, setExamList] = useState([]);

  const [examTopicMap, setExamTopicMap] = useState([]);

  const { examId } = useParams();

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
      `https://localhost:8443/onlineexam/control/getExamDetails?examId=${examId}`
    );
    const { examList } = await res.json();
    setExamList(examList);
    topicList(examList);
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
        body: JSON.stringify(examTopicMappingDetails),
      }
    );
    const data = await res.json();
    const { examTopicMappingMasterRecord } = data;
    setExamTopicMap([...examTopicMap, examTopicMappingMasterRecord]);
  };

  const topicList = (examList) => {
    let unique = [];
    let topic=[];
    examList.forEach((element) => {
      if (!unique.includes(element.topicId)){
        unique.push(element.topicId);
        topic.push({ 
          examId:element.examId,
          examName:element.examName,
          topicId: element.topicId, 
          topicName: element.topicName,
          percentage:element.percentage,
          topicPassPercentage:element.topicPassPercentage,
          questionPerExam:element.questionPerExam
        });
      }
       
    });
    return topic;
  };

  return (
    <EditExamContext.Provider
      value={{ examId, onCreateExamTopicMappingMaster }}
    >
      <MainContent
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
      />
    </EditExamContext.Provider>
  );
};

export default EditExam;
