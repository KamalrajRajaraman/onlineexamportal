import React, { useContext, useEffect, useState } from "react";
import { EditUserContext } from "./EditUser";
import { useExamContext } from "../exam/ExamData";
import FormInput from "../../common/FormInput";
import Swal from "sweetalert2";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
const AddExamToUser = () => {
  const { exams, setExams, fetchExam } = useExamContext();
  const { partyId, fetchAllExamsForUser } = useContext(EditUserContext);
  const [formErrors, setFormErrors] = useState({});
  const initialValues = {
    partyId,
    examId: "",
    noOfAttempts: 0,
    allowedAttempts: "",
    timeoutDays: "",
    passwordChangesAuto: "Y",
    canSplitExams: "Y",
    canSeeDetailedResults: "Y",
    maxSplitAttempts: "",
  };

  
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
    console.log(formErrors);
    console.log(Object.keys(formErrors).length);
    if (Object.keys(formErrors).length === 0 && isSubmit) {
      createUserExamMappingRecord(formValues);
      // getExams();
    }
  }, [formErrors]);

  const validate = (values) => {
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

  const getExams = async () => {
    const dataFetched = await fetchExam();
    if (dataFetched.result === "success") {
      const examList = dataFetched.examList;
      setExams(examList);
    } else {
      setExams(null);
    }
  };

  const createUserExamMappingRecord = async (formValues) => {
    console.log("fetchexams for user called");
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
      "createUserExamMappingRecord",
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
    if (data.result === "success") {
      Swal.fire({
        title: "Good job!",
        text: "Exam added to user  successfully!",
        icon: "success",
        timer: 2000,
        showConfirmButton: false,
      });
      fetchAllExamsForUser();
      setFormValues(initialValues);
    }
    console.log(data);
  };

  return (
    <div className="fluid-container mt-2 p-2 fw-bold border">
      <form id="addExamToUser" onSubmit={handleSubmit}>
        <div className="row   ">
          <div className="col">
            <div className="mb-3 ">
              <label htmlFor="examId" className="form-label">
                Exam Name
              </label>
              <select
                className="form-control"
                id="examId"
                name="examId"
                value={formValues.examId}
                onChange={handleChange}
              >
                <option value="00">None</option>
                {exams &&
                  exams.map((exam) => (
                    <option key={exam.examId} value={exam.examId}>
                      {exam.examName}
                    </option>
                  ))}
              </select>
              <small className="text-danger">{formErrors.examId}</small>
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
          </div>
          <div className="col">
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
          </div>
          <div className="col">
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
          </div>
          <div className="col">
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
