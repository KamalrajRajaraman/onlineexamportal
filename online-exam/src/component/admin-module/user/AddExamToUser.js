import React, { useContext, useEffect, useState } from "react";
import { EditUserContext } from "./EditUser";
import { useExamContext } from "../exam/examData";
import FormInput from "../../common/FormInput";


const AddExamToUser = () => {
  const { partyId } = useContext(EditUserContext);
  const [formErrors, setFormErrors] = useState({});
  const initialValues = {
    partyId,
    examId: "",
    allowedAttempts: "",
    noOfAttempts: "",
    lastPerformanceDate: "",
    timeoutDays: "",
    passwordChangesAuto: "Y",
    canSplitExams: "Y",
    canSeeDetailedResults: "Y",
    maxSplitAttempts: "",
  };

  const { exams, setExams, fetchExam } = useExamContext();
  const [formValues, setFormValues] = useState(initialValues);

  const [isSubmit, setIsSubmit] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormValues({ ...formValues, [name]: value });
  };

  const handleSubmit = (e) => {
    console.log("handle submit called");
    e.preventDefault();
    setFormErrors(validate(formValues));
    setIsSubmit(true);
  };
  useEffect(() => {
    getExams();
    return () => {
      setExams([]);
    };
  }, []);

  useEffect(() => {
    console.log(Object.keys(formErrors).length);
    if (Object.keys(formErrors).length === 0 && isSubmit) {
      console.log("hiiii");
      fetchExamsForUser(formValues);
      // getExams();
    }
  }, [formErrors]);

  const validate = (values) => {
    console.log("validate called");
    const errors = {};

    if (!formValues.examId) {
      errors.examId = "Exam id required!";
    }
    if (!formValues.allowedAttempts) {
      errors.allowedAttempts = "Allowed attempts is required!";
    }
    if (!formValues.noOfAttempts) {
      errors.noOfAttempts = "No of attempts is required!";
    }
    if (!formValues.lastPerformanceDate) {
      errors.lastPerformanceDate = "Last performance date is required!";
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

  const getExams = async () => {
    const result = await fetchExam();
    const examList = result.examList;
    setExams([...exams, ...examList]);
  };

  const fetchExamsForUser = async (formValues) => {
    console.log("fetchexams for user called");
    const res = await fetch(
      "https://localhost:8443/onlineexam/control/createUserExamMappingRecord",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(formValues),
      }
    );

    const data = await res.json();

    console.log(data);
  };

 
  function get() {}
  return (
    <div className="fluid-container mt-2  fw-bold">
      <form id="addExamToUser" onSubmit={handleSubmit}>
        <div className="row col-10 mx-auto pt-2 border ">
          <div className="col">
            <div className="mb-3 ">
              <label htmlFor="examId" className="form-label">
                examId
              </label>
              <select
                className="form-control"
                id="examId"
                name="examId"
                value={formValues.examId}
                onChange={handleChange}
                error={formErrors.examId}
              >
                <option value="00">None</option>
                {exams.map((exam) => (
                  <option key={exam.examId} value={exam.examId}>
                    {exam.examName}
                  </option>
                ))}
              </select>
            </div>

            <div class="mb-3 ">
              <FormInput
                name={"allowedAttempts"}
                value={formValues.allowedAttempts}
                type="text"
                text="Allowed attempts"
                class="form-control"
                onChange={handleChange}
                error={formErrors.allowedAttempts}
              />

             
            </div>

            <div class="mb-3  ">
              <FormInput
                name={"noOfAttempts"}
                value={formValues.noOfAttempts}
                text="No of attempts"
                type="text"
                class="form-control"
                onChange={handleChange}
                error={formErrors.noOfAttempts}
              />

              
            </div>

            <div className="row ">
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
                <small className="text-danger">
                  {formErrors.canSplitExams}
                </small>
              </div>
            </div>
          </div>

          <div className="col">
            <div class="mb-3  ">
              <FormInput
                name={"lastPerformanceDate"}
                value={formValues.lastPerformanceDate}
                type="date"
                text="Last performance date"
                class="form-control"
                onChange={handleChange}
                error={formErrors.lastPerformanceDate}
              />
             
            </div>

          
            <div className="mb-3 ">
              <FormInput
                name={"timeoutDays"}
                value={formValues.timeoutDays}
                type="text"
                text="Timeout Days"
                class="form-control"
                onChange={handleChange}
                error={formErrors.timeoutDays}
              />
            
            </div>

            <div className="mb-3 ">
              <FormInput
                name={"maxSplitAttempts"}
                value={formValues.maxSplitAttempts}
                type="text"
                text="Max split attempts"
                class="form-control"
                onChange={handleChange}
                error={formErrors.maxSplitAttempts}
              />
             
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
          </div>

          </div>
          <div className="d-grid gap-2 col-4 mx-auto mb-2">
            <button className="btn btn-primary" type="submit">
              Submit
            </button>
          </div>
        
      </form>
    </div>
  );
};

export default AddExamToUser;
