import React, { useState } from "react";
import Input from "../Input";
import { useAuth } from "../../auth";
import { useExamContext } from "./examData";


const AddExam = () => {
  const auth= useAuth();
  const{exams,setExams,setAlert} =useExamContext();

 
  const authToken=auth.authToken;

  const [examId, setExamId] = useState("");
  const [examName, setExamName] = useState("");
  const [description, setDescription] = useState("");
  const [creationDate, setCreationDate] = useState("");
  const [expirationDate, setExpirationDate] = useState("");
  const [noOfQuestions, setNoOfQuestions] = useState("");
  const [durationMinutes, setDurationMinutes] = useState("");
  const [passPercentage, setPassPercentage] = useState("");
  const [questionsRandomized, setQuestionsRandomized] = useState("Y");
  const [answersMust, setAnswersMust] = useState("N");
  const [enableNegativeMark, setEnableNegativeMark] = useState("N");
  const [negativeMarkValue, setNegativeMarkValue] = useState("");

  const checkLength = (input, min, max) => {
    if (input.length < min) {
      return `must be atleast ${min} characters`;
    } else if (input.length > max) {
      return `must be less than ${max} characters`;
    } else {
      return `success`;
    }
  };

   //Create Exam
   function onCreateExam(examId) {
   
    fetch("https://localhost:8443/onlineexam/control/createExam", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      credentials: 'include',
      body: JSON.stringify( examId ),
    }).
    then((response) => response.json()).
    then(data=>{
      console.log(data);

      const {exam} = data
      console.log(exam)
      if(data.result==="success"){
        setAlert(true);
      }
      setExams([...exams,exam])

    
     
    });

  }

 

  const onSubmit = (e) => {
    e.preventDefault();

    // const ExamId = checkLength(examId, 0, 20);
    // if (!(ExamId === "success")) {
    //   alert(`Exam Id ${ExamId}`);
    //   return;
    // }

    // const ExamName = checkLength(examName, 5, 255);
    // if (!(ExamName === "success")) {
    //   alert(`Exam Name ${ExamName}`);
    //   return;
    // }

    // const Description = checkLength(description, 0, 1000);
    // if (!(Description === "success")) {
    //   alert(`Description ${Description}`);
    //   return;
    // }

    onCreateExam({
      examId,
      examName,
      description,
      creationDate,
      expirationDate,
      noOfQuestions,
      durationMinutes,
      passPercentage,
      questionsRandomized,
      answersMust,
      enableNegativeMark,
      negativeMarkValue,
      PASSWORD: "ofbiz",
      USERNAME: "admin",
      _LOGIN_PASSED_: "TRUE",
    });

    setExamId("");
    setExamName("");
    setDescription("");
    setCreationDate("");
    setExpirationDate("");
    setNoOfQuestions("");
    setDurationMinutes("");
    setPassPercentage("");
    setQuestionsRandomized("Y");
    setAnswersMust("N");
    setEnableNegativeMark("N");
    setNegativeMarkValue("");
  };

  return (
    <div className="container-fluid my-1 border bg-white   fw-bold">
      <div className="main-content">
        <form onSubmit={onSubmit}>
          <div className="row mt-2 mb-4">
            <div className="col-4">
              <Input
                name={"examId"}
                text="Exam Id"
                state={examId}
                setStateFun={setExamId}
                type={"text"}
                placeholder={""}
              />
              <Input
                name={"examName"}
                text="Exam Name"
                state={examName}
                setStateFun={setExamName}
                type={"text"}
                placeholder={""}
              />
              <Input
                name={"noOfQuestions"}
                text="No Of Questions"
                state={noOfQuestions}
                setStateFun={setNoOfQuestions}
                type={"text"}
                placeholder={""}
              />
              <Input
                name={"passPercentage"}
                text="Pass Percentage"
                state={passPercentage}
                setStateFun={setPassPercentage}
                type={"text"}
                placeholder={""}
              />
            </div>
            <div className="col-4">
              <Input
                name={"description"}
                text="Description"
                state={description}
                setStateFun={setDescription}
                type={"text"}
                placeholder={""}
              />
              <Input
                name={"negativeMarkValue"}
                text="Negative Mark Value"
                state={negativeMarkValue}
                setStateFun={setNegativeMarkValue}
                type={"text"}
                placeholder={""}
              />
              <Input
                name={"durationMinutes"}
                text="Duration Minutes"
                state={durationMinutes}
                setStateFun={setDurationMinutes}
                type={"text"}
                placeholder={""}
              />
              <div className="mb-3">
                <label htmlFor="questionsRandomized" className="form-label">
                  Questions Randomized
                </label>
                <select
                  className="form-control"
                  id="questionsRandomized "
                  value={questionsRandomized}
                  onChange={(e) => setQuestionsRandomized(e.target.value)}
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
              </div>
            </div>
            <div className="col-4">
              <div className="mb-3">
                <label htmlFor="answersMust" className="form-label">
                  Answers Must
                </label>
                <select
                  className="form-control"
                  id="answersMust"
                  value={answersMust}
                  onChange={(e) => setAnswersMust(e.target.value)}
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
              </div>

              <div className="mb-3">
                <label htmlFor="enableNegativeMark" className="form-label">
                  Enable Negative Mark
                </label>
                <select
                  className="form-control"
                  id="enableNegativeMark"
                  value={enableNegativeMark}
                  onChange={(e) => setEnableNegativeMark(e.target.value)}
                >
                  <option value="Y">Yes</option>
                  <option value="N">NO</option>
                </select>
              </div>

              <Input
                name={"creationDate"}
                text="Creation Date"
                state={creationDate}
                setStateFun={setCreationDate}
                type={"date"}
              />
              <Input
                name={"expirationDate"}
                text="Expiration Date"
                state={expirationDate}
                setStateFun={setExpirationDate}
                type={"date"}
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
