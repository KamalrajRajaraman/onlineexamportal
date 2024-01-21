import React, { useContext, useEffect, useState } from "react";

import { useTopicContext } from "../topic/TopicData";
import { EditExamContext } from "./EditExam";
import FormInput from "../../common/FormInput";

const AddTopicToExam = () => {

  //Consuming data from EditExamContext
  const {
    examId,
    onCreateExamTopicMappingMaster,
    formValues,
    examTopicMap,
    setFormValues,
  } = useContext(EditExamContext);

  //Consuming data from useTopicContext
  const { topics, setTopics, fetchTopic } = useTopicContext();

  //Error state contains  error given by validator
  const [formErrors, setFormErrors] = useState({});
  const [isSubmit, setIsSubmit] = useState(false);

  //onchange of inputs the entry are stored in formValues
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };

  //if no error and form is sumbitted, posting the data  to backend
  useEffect(() => {
    if (Object.keys(formErrors).length === 0 && isSubmit) {
      onCreateExamTopicMappingMaster(formValues);
    }
  }, [formErrors]);

  //topics are retrived from backend and emptying topics while unmounting
  useEffect(() => {
    getTopics();
    return () => {
      setTopics([]);
    };
  }, [examTopicMap]);

  //topics are retrived from backend
  const getTopics = async () => {
    const topicList = await fetchTopic();
    const topics = distincttopicList(topicList);
    setTopics(topics);
  };

  //filters the topic already mapped to exam
  const distincttopicList = (topicList) => {
    let unique = [];
    let topics = [];
    examTopicMap.forEach((examTopicRecord) =>
      unique.push(examTopicRecord.topicId)
    );
    topicList && topicList.forEach((topic) => {
      if (!unique.includes(topic.topicId)) {
        topics.push(topic);
      }
    });
    return topics;
  };

  //Form submit action
  const handleSubmit = (e) => {
    e.preventDefault();
    setFormErrors(validate(formValues));
    setIsSubmit(true);
  };

  //Form validation
  const validate = (values) => {
    const errors = {};
    if (!values.topicId) {
      errors.topicId = "Topic is required";
    }
    if (!values.percentage) {
      errors.percentage = "Percentage is required";
    }
    if (!values.topicPassPercentage) {
      errors.topicPassPercentage = "Topic Percentage is required";
    }
    return errors;
  };

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
                  value={formValues.topicId}
                  onChange={handleChange}
                >
                  <option value="">None</option>
                  {topics && topics.map((topic) => (
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
                value={formValues.percentage}
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
                value={formValues.topicPassPercentage}
                onChange={handleChange}
                type={"text"}
                placeholder={""}
                error={formErrors.topicPassPercentage}
              />
            </div>
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
    </>
  );
};

export default AddTopicToExam;
