import React, { useContext, useEffect, useState } from "react";
import FormInput from "../../common/FormInput";
import { EditUserContext } from "./EditUser";
import {PUT, CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION, vanishAlert, swalFireAlert } from "../../common/CommonConstants";
import { useNavigate } from "react-router-dom";


const EditUserExam = ({ userExamMapping }) => {
  const navigate = useNavigate();
  const {fetchAllExamsForUser } = useContext(EditUserContext);
  const [formErrors, setFormErrors] = useState({});
  const [isSubmit, setIsSubmit] = useState(false);
  const [formValues, setFormValues] = useState(userExamMapping);
  
  useEffect(() => {
    console.log(formErrors);
    console.log(Object.keys(formErrors).length);
    if (Object.keys(formErrors).length === 0 && isSubmit) {
      console.log(formValues);
        createUserExamMappingRecord(formValues);
    }
  }, [formErrors]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };

  const validate = (formValues) => {
    console.log("validate called");
    const errors = {};

    if (!formValues.examId || formValues.examId === "00") {
      errors.examId = "Exam id required!";
    }
    if (!formValues.allowedAttempts) {
      errors.allowedAttempts = "Allowed attempts is required!";
    }
    

    if (!formValues.timeoutDays) {
      errors.timeoutDays = "Timeout days is required!";
    }
    if (!formValues.passwordChangesAuto) {
      errors.passwordChangesAuto = "Password changes auto is required!";
    }
    if (!formValues.canSplitExams) {
      errors.canSplitExams = "Can split exams required!";
    }
    if (!formValues.canSeeDetailedResults) {
      errors.canSeeDetailedResults = "Can see detailed results id required!";
    }
    if (!formValues.maxSplitAttempts) {
      errors.maxSplitAttempts = "Max split attempts id required!";
    }
    return errors;
  };

  const handleSubmit = (e) => {
    console.log("handle submit called");
    e.preventDefault();
    setFormErrors(validate(formValues));
    setIsSubmit(true);
  };

  const createUserExamMappingRecord = async (formValues) => {
    console.log("fetchexams for user called");
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
      "createUserExamMappingRecord",{...PUT, body: JSON.stringify(formValues),} );
    if (res.status === 401) {
      navigate("/login");
    }
    const data = await res.json();
    if (data.result === "success") {
      vanishAlert("Good job!","Edit successfully!","success",2000,false);
      document.getElementById(`close${formValues.examId}`).click();
      fetchAllExamsForUser();
     
    }else if (data.result==="error"){
      swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
    }
  
  };
  return (
    <div className="container-fluid fw-bold">
      <div className="modal-header border-dark">
        <h1 className="modal-title fs-3" id="exampleModalLabel">
           Edit User Exam Mapping
        </h1>
        <button
            id={`close${formValues.examId}`}
          type="button"
          className="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>

      <form id="addExamToUser" className="mt-1" onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="examName" className="form-label">
            Exam Name
          </label>
          <input
            className="form-control"
            value={formValues.examName}
            id="examName"
            disabled
          />
        </div>

        <FormInput
          name={"allowedAttempts"}
          value={formValues.allowedAttempts}
          type="text"
          text="Allowed attempts"
          class="form-control"
          onChange={handleChange}
          error={formErrors.allowedAttempts}
        />

        <FormInput
          name={"noOfAttempts"}
          value={formValues.noOfAttempts}
          type="text"
          text="No Of Attempts"
          class="form-control"
          onChange={handleChange}
          error={formErrors.noOfAttempts}
        />

        <FormInput
          name={"timeoutDays"}
          value={formValues.timeoutDays}
          type="text"
          text="Timeout Days"
          class="form-control"
          onChange={handleChange}
          error={formErrors.timeoutDays}
        />

        <FormInput
          name={"maxSplitAttempts"}
          value={formValues.maxSplitAttempts}
          type="text"
          text="Max split attempts"
          class="form-control"
          onChange={handleChange}
          error={formErrors.maxSplitAttempts}
        />
        <div className="mb-3 col">
          <label htmlFor="passwordChangesAuto" className="form-label">
            passwordChangesAuto
          </label>
          <select
            className="form-control"
            id="passwordChangesAuto"
            name="passwordChangesAuto"
            class="form-control"
            onChange={handleChange}
            value={formValues.passwordChangesAuto}
          >
            <option value="Y">Yes</option>
            <option value="N">NO</option>
          </select>
          <small className="text-danger">
            {formErrors.passwordChangesAuto}
          </small>
        </div>
        <div className="mb-3 col-6">
          <label htmlFor="canSeeDetailedResults" className="form-label">
            canSeeDetailedResults
          </label>
          <select
            className="form-control"
            id="canSeeDetailedResults"
            name="canSeeDetailedResults"
            class="form-control"
            onChange={handleChange}
            value={formValues.canSeeDetailedResults}
          >
            <option value="Y">Yes</option>
            <option value="N">NO</option>
          </select>
          <small className="text-danger">
            {formErrors.canSeeDetailedResults}
          </small>
        </div>
        <div className="mb-3 col">
          <label htmlFor="canSplitExams" className="form-label">
            canSplitExams
          </label>
          <select
            className="form-control"
            id="canSplitExams"
            name="canSplitExams"
            class="form-control"
            value={formValues.canSplitExams}
            onChange={handleChange}
          >
            <option value="Y">Yes</option>
            <option value="N">NO</option>
          </select>
          <small className="text-danger">{formErrors.canSplitExams}</small>
        </div>
        <div className="d-grid gap-2 col-4 mx-auto mb-2">
          <button className="btn btn-primary" type="submit">
            Update
          </button>
        </div>
      </form>
    </div>
  );
};

export default EditUserExam;
