import React, { useEffect, useState } from "react";
import MainContent from "../../common/MainContent";
import AccordinMaker from "../../common/AccordinMaker";
import { useQuestionContext } from "./QuestionData";
import FormInput from "../../common/FormInput";
import { useTopicContext } from "../topic/TopicData";


const Questions = () => {
  const [questionId, setQuestionId] = useState([]);
  const { questions, setQuestions, onDelete, onEdit ,fetchQuestion} = useQuestionContext();
  const { getTopics, topics, setTopics, fetchTopic } = useTopicContext();
  const initialValue = {
    questionId: "",
    questionDetail: "",
    optionA: "",
    optionB: "",
    optionC: "",
    optionD: "",
    optionE: "",
    answer: "",
    numAnswers: "",
    questionType: "01",
    difficultyLevel: "",
    answerValue: "",
    topicId: "",
    negativeMarkValue: "",
  };
  const [formValues, setFormValues] = useState(initialValue);

  useEffect(() => {
    fetchQuestion();
    return () => {
      setQuestions([]);
    };
  }, []);

  useEffect(() => {
    getTopics();

    return () => {
      setTopics([]);
    };
  }, []);

 

  const modalEdit = async (id, object) => {
    setQuestionId(id);
    setFormValues(object);
    console.log("questionId:::", questionId);
    document.getElementById("buttonId").click();
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    onEdit(questionId, formValues);
  };

 

  const text = {
    header: "Questions",
    btnText: "Questions",
  };

  return (
    <>
      <MainContent text={text} to="add-questions" back="/admin/questions" />
      <AccordinMaker
        objects={questions}
        id={"questionId"}
        name={"questionDetail"}
        onEdit={modalEdit}
        onDelete={onDelete}
      />

      <button
        type="button"
        className="btn btn-primary d-none"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
        id="buttonId"
      >
        Launch demo modal
      </button>

      <div
        className="modal fade"
        id="exampleModal"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLabel">
                Question Edit
              </h5>
              <button
                type="button"
                id="modal-close"
                className="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div className="modal-body">
              <form onSubmit={handleSubmit}>
                <FormInput
                  id="questionId"
                  name="questionId"
                  type={"text"}
                  text={"questionId"}
                  value={formValues.questionId}
                />

                <FormInput
                  id="questionDetail"
                  name="questionDetail"
                  type={"text"}
                  text={"Question Detail"}
                  value={formValues.questionDetail}
                  onChange={handleChange}
                />
                <FormInput
                  name="optionA"
                  id="optionA"
                  type={"text"}
                  text={"Option A"}
                  value={formValues.optionA}
                  onChange={handleChange}
                />
                <FormInput
                  name="optionB"
                  id="optionB"
                  type={"text"}
                  text={"Option B"}
                  value={formValues.optionB}
                  onChange={handleChange}
                />
                <FormInput
                  name="optionC"
                  id="optionC"
                  type={"text"}
                  text={"Option C"}
                  value={formValues.optionC}
                  onChange={handleChange}
                />
                <FormInput
                  name="optionD"
                  id="optionD"
                  type={"text"}
                  text={"Option D"}
                  value={formValues.optionD}
                  onChange={handleChange}
                />
                <FormInput
                  name="optionE"
                  id="optionE"
                  type={"text"}
                  text={"Option E"}
                  value={formValues.optionE}
                  onChange={handleChange}
                />

                <FormInput
                  name="answer"
                  id="answer"
                  type={"text"}
                  text={"Answer"}
                  value={formValues.answer}
                  onChange={handleChange}
                />

                <div className="row">
                  <div className="mb-3 col">
                    <label htmlFor="questionType" className="form-label">
                      Question Type
                    </label>
                    <select
                      className="form-control"
                      id="questionType"
                      name="questionType"
                      value={formValues.questionType}
                      onChange={handleChange}
                    >
                      <option value="01">Single Choice</option>
                      <option value="02">Multiple Choice</option>
                      <option value="03">True or False</option>
                      <option value="04">Fill in the Blanks</option>
                      <option value="05">Detailed Answer</option>
                    </select>
                  </div>
                  <div className="mb-3 col">
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
                      {topics.map((topic) => (
                        <option key={topic.topicId} value={topic.topicId}>
                          {topic.topicName}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="row">
                  <div className="mb-3 col">
                    <FormInput
                      name="numAnswers"
                      id="numAnswers"
                      type={"text"}
                      text={"Num Answers"}
                      value={formValues.numAnswers}
                      onChange={handleChange}
                    />
                  </div>

                  <div className="mb-3 col">
                    <FormInput
                      id="difficultyLevel"
                      name="difficultyLevel"
                      type={"text"}
                      text={"Difficulty Level"}
                      value={formValues.difficultyLevel}
                      onChange={handleChange}
                    />
                  </div>
                </div>
                <div className="row">
                  <div className="mb-3 col">
                    <FormInput
                      id="answerValue"
                      name="answerValue"
                      type={"text"}
                      text={"Answer Value"}
                      value={formValues.answerValue}
                      onChange={handleChange}
                    />
                  </div>

                  <div className="mb-3 col">
                    <FormInput
                      name="negativeMarkValue"
                      id="negativeMarkValue"
                      type={"text"}
                      text={"Negative Mark Value"}
                      value={formValues.negativeMarkValue}
                      onChange={handleChange}
                    />
                  </div>
                </div>

                <div className="col-2 mx-auto  d-grid gap-2 ">
                  <button className="btn btn-primary" type="submit">
                    Submit
                  </button>
                </div>
              </form>
            </div>
           
          </div>
        </div>
      </div>
    </>
  );
};

export default Questions;
