import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ExamAccordion from "./ExamAccordion";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
const ExamsForUser = () => {
  const [exams, setExams] = useState([]);
  
 
  useEffect(() => {
    fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"findAllExamForPartyId", {
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

        <ExamAccordion key ={exam.examId}object={exam} />
       
            // <h2  key={exam.examId} className ="accordion-header border py-3 px-3" id="headingOne">
            //   <div className="row">
            //     <div className="col-10">
            //       <h5 className="m-0 pt-1"> {exam.examName}</h5>
            //     </div>
            //     <div className="col-2">
            //       <div className="d-grid gap-2 d-md-flex justify-content-md-end ">
            //         <button
            //           onClick={() => navigate(`exam-page/${exam.examId}`)}
            //           className="btn btn-outline-success  me-4 col-8"
            //         >
            //           Take exam
            //         </button>
            //       </div>
            //     </div>
            //   </div>
            // </h2>
       
      ))}
    </div>
    </div>
  );
};

export default ExamsForUser;
