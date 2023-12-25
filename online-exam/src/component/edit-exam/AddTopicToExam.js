import React, { useContext, useEffect, useState } from "react";
import FormInput from "../common/FormInput";
import { useTopicContext } from "../topic/topicData";
import { EditExamContext } from "./EditExam";

const AddTopicToExam = () => {
  const { examId, onCreateExamTopicMappingMaster, fromValues,
    setFromValues } =
    useContext(EditExamContext);
  const { topics, setTopics, fetchTopic } = useTopicContext();

 
  const [formErrors,setFromErrors] =useState({});
  const [isSubmit,setIsSubmit] =useState(false);
  const handleChange =(e)=>{
    const {name,value} = e.target;
    setFromValues({...fromValues,[name]:value})
  }

  useEffect(()=>{
    if(Object.keys(formErrors).length === 0 && isSubmit){
      onCreateExamTopicMappingMaster(fromValues);
      
    }
  },[formErrors])

 

  // const [topicId, setTopicId] = useState("00");
  // const [percentage, setPercentage] = useState("");
  // const [topicPassPercentage, setTopicPassPercentage] = useState("");
  // const [questionsPerExam, setQuestionsPerExam] = useState("");

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


  const handleSubmit = (e) => {
    e.preventDefault();
    setFromErrors(validate(fromValues));
    setIsSubmit(true);


    // onCreateExamTopicMappingMaster({
    //   examId,
    //   topicId,
    //   percentage,
    //   topicPassPercentage,
    //   questionsPerExam,
    // });
    // setTopicId("00");
    // setPercentage("");
    // setTopicPassPercentage("");
    // setQuestionsPerExam("");
  };

 const validate =(values)=>{
  const errors={}
  if(!values.topicId){
    errors.topicId ="Topic is required"
  }
  if(!values.percentage){
    errors.percentage ="Percentage is required"
  }
  if(!values.topicPassPercentage){
    errors.topicPassPercentage ="Topic Percentage is required"
  }
  return errors;
 }

  
  return (
    <> 
    <div className="container-fluid border mt-2">
     
      <form onSubmit={handleSubmit}>
        <div className="row">
          <div className="col">
            <div className="mb-3">
              <label className="form-label">Exam Id</label>
              <div className="form-control ">{examId}</div>
            </div>
          </div>
          <div className="col">
            <div className="mb-3">
              <label htmlFor="topicId" className="form-label">
                Topic
              </label>
              <select
                className="form-control"
                id="topicId"
                name="topicId"
                value={fromValues.topicId}
                onChange={handleChange}
              >
                <option value="">None</option>
                {topics.map((topic) => (
                  <option key={topic.topicId} value={topic.topicId}>
                    {topic.topicName}
                  </option>
                ))}
              </select>
              <small className="text-danger">{formErrors.topicId}</small>
            </div>
          </div>
          <div className="col">
            <FormInput
              id={"percentage"}
              name={"percentage"}
              text="percentage"
              value={fromValues.percentage}
              onChange={handleChange}
              type={"text"}
              placeholder={""}
              error={formErrors.percentage}
            />
          </div>
          <div className="col">
            <FormInput
            id={"topicPassPercentage"}
              name={"topicPassPercentage"}
              text="Topic Pass Percentage"
              value={fromValues.topicPassPercentage}
              onChange={handleChange}
              type={"text"}
              placeholder={""}
              error={formErrors.topicPassPercentage}
            />
          </div>
          {/* <div className="col-2">
            <FormInput
              id={"questionsPerExam"}
              name={"questionsPerExam"}
              text="Questions Per Exam"
              value={fromValues.questionsPerExam}
              onChange={handleChange}
              type={"text"}
              placeholder={""}
              error={formErrors.questionsPerExam}
            />
          </div> */}
          <div className="col">
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
