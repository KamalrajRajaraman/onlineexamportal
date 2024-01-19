import React, {  useEffect } from "react";

import { TiEdit } from "react-icons/ti";

import EditUserExam from "./EditUserExam";

const ShowExam = ({ exams, fetchAllExamsForUser }) => {
  useEffect(() => {
    fetchAllExamsForUser();
  }, []);

  return (
    <>
      <div class="container table-responsive py-2">
        <table className="table table-striped ">
          <tbody>
            {exams &&
              exams.map((exam) => (
                <tr  key={exam.examId}className="p-0 m-0">
                  <div className="row border">
                    <div className="col-2 mx-auto my-auto ">
                      <div className="fw-bold">Exam Name</div>
                      <div>{exam.examName}</div>
                    </div>
                    <div className="col">
                      <div className="row  fw-bold">
                        <div className="col">Allowed Attempts</div>
                        <div className="col">No Of Attempts</div>
                        <div className="col">Last PerformanceDate</div>
                        <div className="col">Timeout Days</div>
                      </div>
                      <div className="row ">
                        <div className="col">{exam.allowedAttempts}</div>
                        <div className="col">{exam.noOfAttempts}</div>
                        <div className="col">{exam.lastPerformanceDate}</div>
                        <div className="col">{exam.timeoutDays}</div>
                      </div>
                      <div className="row  fw-bold">
                        <div className="col">Password Changes Auto</div>
                        <div className="col">Can Split Exams</div>
                        <div className="col">Can See Detailed Results</div>
                        <div className="col">Max Split Attempts</div>
                      </div>
                      <div className="row   ">
                        <div className="col">{exam.passwordChangesAuto}</div>
                        <div className="col">{exam.canSplitExams}</div>
                        <div className="col">{exam.canSeeDetailedResults}</div>
                        <div className="col">{exam.maxSplitAttempts}</div>
                      </div>
                    </div>
                    <div className="col-1 mx-auto my-auto">
                      <div className="fw-bold">Edit</div>
                      <div>
                        <TiEdit
                          type="button"
                          data-bs-toggle="modal"
                           data-bs-target={`#Modal${exam.examId}`}
                        />
                        <div
                          className="modal fade"
                          id={`Modal${exam.examId}`}
                          tabIndex="-1"
                          aria-labelledby="exampleModalLabel"
                          aria-hidden="true"
                        >
                          <div className="modal-dialog modal-dialog-centered">
                            <div className="modal-content">
                              <EditUserExam userExamMapping ={exam}/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default ShowExam;
