import React, { useEffect, useState } from "react";

import { useExamContext } from "./ExamData";
import FormInput from "../../common/FormInput";

import {POST, CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION, vanishAlert ,swalFireAlert} from "../../common/CommonConstants";
import { useNavigate } from "react-router-dom";
const AddExam = () => {
  const navigate = useNavigate();

  //consuming data from useExamContext
  const { exams, setExams,  } = useExamContext();

  const initialValues = {
    examId: "",
    examName: "",
    description: "",
    creationDate: "",
    expirationDate: "",
    noOfQuestions: "",
    durationMinutes: "",
    passPercentage: "",
    questionsRandomized: "Y",
    answersMust: "Y",
    enableNegativeMark: "N",
    negativeMarkValue: "",
  };

  //formvalues 
  const [examValues, setExamValues] = useState(initialValues);
  const [formErrors, setFormErrors] = useState({});
  const [isSubmit, setIsSubmit] = useState(false);

  //Handles Form Input
  const handleChange = (e) => {
    const { name, value } = e.target;
    setExamValues({ ...examValues, [name]: value });
  };

  //Handle Form  Submit
  const handleSubmit = (e) => {
    e.preventDefault();
    setFormErrors(validate(examValues));
    setIsSubmit(true);
  };

  //add Exam using useEffect
  useEffect(() => {
    if (Object.keys(formErrors).length === 0 && isSubmit) {
      onCreateExam(examValues);
    }
  }, [formErrors]);

  //Validate Inputs
  const validate = (values) => {
    const errors = {};
    if (!values.examName) {
      errors.examName = "Exam name is required";
    }
    if (!values.description) {
      errors.description = "Description is required";
    }
    if (!values.creationDate) {
      errors.creationDate = "Description is required";
    }
    if (!values.expirationDate) {
      errors.expirationDate = "Expiration date is required";
    }
    if (!values.noOfQuestions) {
      errors.noOfQuestions = "No of questions is required";
    }
    if (!values.durationMinutes) {
      errors.durationMinutes = "Duration minutes is required";
    }
    if (!values.passPercentage) {
      errors.passPercentage = "Pass percentage is required";
    }
    if (!values.questionsRandomized) {
      errors.questionsRandomized = "Questions randomized is required";
    }
    if (!values.answersMust) {
      errors.answersMust = "Answers must is required";
    }
    if (!values.enableNegativeMark) {
      errors.enableNegativeMark = "Enable Negative Mark is required";
    }
    if (!values.negativeMarkValue) {
      errors.negativeMarkValue = "Negative Mark Value is required";
    }
    return errors;
  };

  

  //Create Exam
  function onCreateExam(exam) {
    fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"createExam", 
    {...POST, body: JSON.stringify(exam)})
      .then((response) =>{
        if (response.status === 401) {
          navigate("/login");
        }
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json() })
       .then((data) => {
            if (data.result === "success") {
              vanishAlert("Good job!","Exam created successfully!","success",2000,false);
              setExamValues(initialValues);
              const { exam } = data;
              setExams([...exams, exam]);
            } 
            else if (data.result==="error"){
              swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
            }
          })
         .catch((error) => {
                  console.log(error);
          });
  }

  return (
    <div className="container-fluid my-1 border bg-white   fw-bold">
      <div className="main-content">
        <form onSubmit={handleSubmit}>
          <div className="row mt-2 mb-4">
            <div className="col-4">
              <FormInput
                name={"examName"}
                value={examValues.examName}
                onChange={handleChange}
                id={"examName"}
                text="Exam Name"
                type={"text"}
                placeholder={""}
                error={formErrors.examName}
              />

              <FormInput
                name={"noOfQuestions"}
                id={"noOfQuestions"}
                value={examValues.noOfQuestions}
                onChange={handleChange}
                text="No Of Questions"
                type={"text"}
                placeholder={""}
                error={formErrors.noOfQuestions}
              />

              <FormInput
                name={"passPercentage"}
                id={"passPercentage"}
                value={examValues.passPercentage}
                onChange={handleChange}
                text="Pass Percentage"
                type={"text"}
                placeholder={""}
                error={formErrors.passPercentage}
              />
              <FormInput
                name={"expirationDate"}
                id={"expirationDate"}
                text="Expiration Date"
                value={examValues.expirationDate}
                onChange={handleChange}
                type={"date"}
                error={formErrors.expirationDate}
              />
            </div>
            <div className="col-4">
              <FormInput
                name={"description"}
                id={"description"}
                text="Description"
                value={examValues.description}
                onChange={handleChange}
                type={"text"}
                placeholder={""}
                error={formErrors.description}
              />
              <FormInput
                name={"negativeMarkValue"}
                id={"negativeMarkValue"}
                text="Negative Mark Value"
                value={examValues.negativeMarkValue}
                onChange={handleChange}
                type={"text"}
                placeholder={""}
                error={formErrors.negativeMarkValue}
              />
              <FormInput
                name={"durationMinutes"}
                id={"durationMinutes"}
                text="Duration Minutes"
                value={examValues.durationMinutes}
                onChange={handleChange}
                type={"text"}
                placeholder={""}
                error={formErrors.durationMinutes}
              />
              <div className="mb-3">
                <label htmlFor="questionsRandomized" className="form-label">
                  Questions Randomized
                </label>
                <select
                  name="questionsRandomized"
                  className="form-control"
                  id="questionsRandomized "
                  value={examValues.questionsRandomized}
                  onChange={handleChange}
                 
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
                <small className='text-danger'>{formErrors.questionsRandomized}</small>
              </div>
            </div>
            <div className="col-4">
              <div className="mb-3">
                <label htmlFor="answersMust" className="form-label">
                  Answers Must
                </label>
                <select
                  name="answersMust"
                  className="form-control"
                  id="answersMust"
                  value={examValues.answersMust}
                  onChange={handleChange}
                  
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
                <small className='text-danger'>{formErrors.answersMust}</small>
              </div>

              <div className="mb-3">
                <label htmlFor="enableNegativeMark" className="form-label">
                  Enable Negative Mark
                </label>
                <select
                  name="enableNegativeMark"
                  className="form-control"
                  id="enableNegativeMark"
                  value={examValues.enableNegativeMark}
                  onChange={handleChange}
                
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
                <small className='text-danger'>{formErrors.enableNegativeMark}</small>
              </div>

              <FormInput
                name={"creationDate"}
                id={"creationDate"}
                text="Creation Date"
                value={examValues.creationDate}
                onChange={handleChange}
                type={"date"}
                error={formErrors.creationDate}
              />
            </div>
          </div>

          <div className="d-grid gap-2 col-4 mx-auto mb-2">
            <button className="btn btn-primary" type="submit">
              Submit
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddExam;
