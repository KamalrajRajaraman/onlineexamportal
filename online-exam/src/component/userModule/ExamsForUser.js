import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const ExamsForUser = () => {
  const [exams, setExams] = useState([]);
  const navigate = useNavigate();
 
  useEffect(() => {
    fetch(`https://localhost:8443/onlineexam/control/showExamsForPartyId`, {
      credentials: "include",
    })
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        console.log("data.examlist::::", data.examList);
        setExams(data.examList);
      });
  }, []);

  return (
    <div className="container-fluid ">
      <div className="container-fluid border-bottom border-dark border-3">
        <h2 className="p-2 ">Scheduled Exams</h2>
      </div>
      <div className="mt-1 ">
      {exams.map((exam) => (
       
            <h2  key={exam.examId} className ="accordion-header border py-3 px-3" id="headingOne">
              <div className="row">
                <div className="col-10">
                  <h5 className="m-0 pt-1"> {exam.examName}</h5>
                </div>
                <div className="col-2">
                  <div className="d-grid gap-2 d-md-flex justify-content-md-end ">
                    <button
                      onClick={() => navigate(`exam-page/${exam.examId}`)}
                      className="btn btn-outline-success  me-4 col-8"
                    >
                      Take exam
                    </button>
                  </div>
                </div>
              </div>
            </h2>
       
      ))}
    </div>
    </div>
  );
};

export default ExamsForUser;

{
  /* <div className="container col-6 border rounded bg-secondary ">
          <h4>
            {exam.examName}
            <span className="offset-7 ">
              <button
                className="mt-3 btn bg-primary"
                onClick={() => navigate("/introduction")}
              >
                Take Exam
              </button>
            </span>
          </h4>
        </div> */
}
