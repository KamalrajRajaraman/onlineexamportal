import React, { useContext, useEffect, useState } from "react";
import Input from "../Input";
import { useTopicContext } from "../topic/topicData";
import { EditExamContext } from "./EditExam";

const AddTopicToExam = () => {
  const { examId, onCreateExamTopicMappingMaster } =
    useContext(EditExamContext);
  const { topics, setTopics, fetchTopic } = useTopicContext();
  const [topicId, setTopicId] = useState("00");
  const [percentage, setPercentage] = useState("");
  const [topicPassPercentage, setTopicPassPercentage] = useState("");
  const [questionsPerExam, setQuestionsPerExam] = useState("");

  useEffect(() => {
    getTopics();

    return () => {
      setTopics([]);
    };
  }, []);

  const getTopics = async () => {
    const topicList = await fetchTopic();
    setTopics([...topicList]);
  };


  const onSubmit = (e) => {
    e.preventDefault();
    onCreateExamTopicMappingMaster({
      examId,
      topicId,
      percentage,
      topicPassPercentage,
      questionsPerExam,
    });
    setTopicId("00");
    setPercentage("");
    setTopicPassPercentage("");
    setQuestionsPerExam("");
  };

 

  
  return (
    <> 
    <div className="container-fluid border mt-2">
     
      <form onSubmit={onSubmit}>
        <div className="row">
          <div className="col-2">
            <div className="mb-3">
              <label className="form-label">Exam Id</label>
              <div className="form-control ">{examId}</div>
            </div>
          </div>
          <div className="col-2">
            <div className="mb-3">
              <label htmlFor="topicId" className="form-label">
                Topic
              </label>
              <select
                className="form-control"
                id="topicId"
                value={topicId}
                onChange={(e) => setTopicId(e.target.value)}
              >
                <option value="00">None</option>
                {topics.map((topic) => (
                  <option key={topic.topicId} value={topic.topicId}>
                    {topic.topicName}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="col-2">
            <Input
              name={"percentage"}
              text="percentage"
              state={percentage}
              setStateFun={setPercentage}
              type={"text"}
              placeholder={""}
            />
          </div>
          <div className="col-2">
            <Input
              name={"topicPassPercentage"}
              text="Topic Pass Percentage"
              state={topicPassPercentage}
              setStateFun={setTopicPassPercentage}
              type={"text"}
              placeholder={""}
            />
          </div>
          <div className="col-2">
            <Input
              name={"questionsPerExam"}
              text="Questions Per Exam"
              state={questionsPerExam}
              setStateFun={setQuestionsPerExam}
              type={"text"}
              placeholder={""}
            />
          </div>
          <div className="col-2">
            <div className="d-grid gap-2 mx-auto mt-4 p-2">
              <button className="btn btn-primary" type="submit">
                Submit
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
    <label>
        Topic coverage 
      </label>
    <div class="progress">
     
    <div
      class="progress-bar"
      role="progressbar"
      style={{width: 250}}
      aria-valuenow="25"
      aria-valuemin="0"
      aria-valuemax="100"
    >
      25%
    </div>
  </div>
    </>
  );
};

export default AddTopicToExam;
