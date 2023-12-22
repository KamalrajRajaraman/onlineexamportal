import React, { useContext, useEffect } from "react";
import { EditExamContext } from "./EditExam";

const QuestionTable= () => {
  const {examList,fetchExamList,setExamList} = useContext(EditExamContext);
  useEffect(()=>{
   fetchExamList();
    return()=>{
      setExamList([]);
    }
  },[])
  const topicList = (examList) => {
      let unique = [];
      let topic = [];
      examList.forEach((element) => {
        if (!unique.includes(element.topicId)) {
          unique.push(element.topicId);
          topic.push({
            examId: element.examId,
            examName: element.examName,
            topicId: element.topicId,
            topicName: element.topicName,
            percentage: element.percentage,
            topicPassPercentage: element.topicPassPercentage,
            questionPerExam: element.questionPerExam,
          });
        }
      });
      return topic;
    };

    const questionList = (examList,topicId) => {  
      const questions = examList.filter((obj) => obj["topicId"] === topicId);  
      return  questions.map((obj) => (
        obj["questionId"] &&
        <tr key={obj["questionId"]}>
          <td >{obj["questionId"]}</td>
          <td>{obj["questionDetail"]}</td>
        </tr>
      ))
    };

  return (
    <div className="container-fluid border  ">
      {topicList(examList) && topicList(examList).map((topic)=><div className="border mt-2">
      <h6 key = {topic.topicId }className="bg-light p-2 mb-0 fw-bold  text-danger border-bottom  border-2">{topic.topicName}</h6>
      <table className="table table-bordered">
      <thead  >
        <tr >
          <th scope="col">Question Id</th>
          <th scope="col">Question</th>
        </tr>
      </thead>
      <tbody>
        {questionList(examList,topic.topicId)}
      </tbody>
    </table>
    </div>
      )}
      
    </div>
  );
};

export default QuestionTable;
