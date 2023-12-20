import React, { useContext, useEffect } from "react";
import { EditUserContext } from "./EditUser";
import { useExamContext } from "../exam/examData";

const AddExamToUser = () => {
  const {partyId} =useContext(EditUserContext);
  const { exams, setExams,fetchExam} = useExamContext();
  
  useEffect(() => {
    getExams();
    return () => {
      setExams([]);
    };
  }, []);

  const getExams = async () => {
    const result = await fetchExam();
    const examList = result.examList;
    setExams([...exams, ...examList]);
  };

  const fetchExamsForUser=async(formData)=>{
   console.log(formData)
  const res = await  fetch('https://localhost:8443/onlineexam/control/createUserExamMappingRecord',
    {
      method:'POST', 
      headers:{
        'Content-type':'application/json'
      },
      credentials: 'include',
     body: JSON.stringify(formData)
  })
  
  const data = await res.json();

  console.log(data);
}

  const handleSubmit= (e)=>{

    e.preventDefault();
    const form = new FormData(e.target);
    console.log(form);
    const formData = Object.fromEntries(form.entries());

  
    fetchExamsForUser(formData)
   document.getElementById("addExamToUser").reset();
     

  }
  function get(){

  }
  return (
    <div className="fluid-container mt-2  fw-bold">
      <form id="addExamToUser"onSubmit={handleSubmit}>
        <div className="row col-10 mx-auto pt-2 border ">
          <div className="col">
           
            <div class="mb-3">
              <label for="partyId" class="form-label">
                partyId
              </label>
              <input
              disabled
                value={partyId}
                type="text"
                class="form-control"
                readOnly
              />
               <input
             hidden
                value={partyId}
                type="text"
                class="form-control"
                id="partyId"
                name="partyId"
                readOnly
              />
            </div>

           

            <div className="mb-3">
              <label htmlFor="examId" className="form-label">
                examId
              </label>
              <select
                className="form-control"
                id="examId"
                name="examId"
               
              ><option value="00">None</option>
                { exams.map((exam) => (
                  <option key={exam.examId} value={exam.examId}>{exam.examName}</option>
                ))}
              </select>
            </div>

        
            <div class="mb-3">
              <label for="allowedAttempts" class="form-label">
                allowedAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="allowedAttempts"
                name="allowedAttempts"
              />
            </div>
            
            <div class="mb-3">
              <label for="noOfAttempts" class="form-label">
                noOfAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="noOfAttempts"
                name="noOfAttempts"
              />
            </div>
            <div class="mb-3 col-4">
              <label for="lastPerformanceDate" class="form-label">
                lastPerformanceDate
              </label>
              <input
                type="date"
                class="form-control"
                id="lastPerformanceDate"
                name="lastPerformanceDate"
              />
            </div>
          </div>
          <div className="col">
            <div class="mb-3">
              <label for="timeoutDays" class="form-label">
                timeoutDays
              </label>
              <input
                type="text"
                class="form-control"
                id="timeoutDays"
                name="timeoutDays"
              />
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="passwordChangesAuto" className="form-label">
                passwordChangesAuto
              </label>
              <select
                className="form-control"
                id="passwordChangesAuto"
                name="passwordChangesAuto"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="canSplitExams" className="form-label">
                canSplitExams
              </label>
              <select
                className="form-control"
                id="canSplitExams"
                name="canSplitExams"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div className="mb-3 col-3">
              <label htmlFor="canSeeDetailedResults" className="form-label">
                canSeeDetailedResults
              </label>
              <select
                className="form-control"
                id="canSeeDetailedResults"
                name="canSeeDetailedResults"
                defaultValue={"Y"}
              >
                <option value="Y">Yes</option>
                <option value="N">NO</option>
              </select>
            </div>

            <div class="mb-3">
              <label for="maxSplitAttempts" class="form-label">
                maxSplitAttempts
              </label>
              <input
                type="text"
                class="form-control"
                id="maxSplitAttempts"
                name="maxSplitAttempts"
              />
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
