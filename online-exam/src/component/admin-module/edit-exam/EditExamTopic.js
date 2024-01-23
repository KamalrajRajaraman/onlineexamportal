import React, { useContext, useState } from "react";
import { EditExamContext } from "./EditExam";
import {
  CONTROL_SERVLET,
  POST,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  vanishAlert,
  WEB_APPLICATION,
} from "../../common/CommonConstants";
import { useNavigate } from "react-router-dom";


const EditExamTopic = ({ obj }) => {
  
  const navigate = useNavigate();
  const { refresh, setRefresh } = useContext(EditExamContext);
  const examId = obj["examId"];
  const topicId = obj["topicId"];
  const [percentage, setPercentage] = useState(obj["percentage"]);
  const [topicPassPercentage, setTopicPassPercentage] = useState(
    obj["topicPassPercentage"]
  );
  const [questionsPerExam, setQuestionsPerExam] = useState(
    obj["questionsPerExam"]
  );
  
  function submitExamTopicForm(e) {
    e.preventDefault();
    updateExamTopic({
      examId,
      topicId,
      percentage,
      topicPassPercentage,
      questionsPerExam,
    });
  }

  async function updateExamTopic(data) {
    
    await fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        "createExamTopicMappingMasterRecord",
      { ...POST, body: JSON.stringify(data) }
    )
      .then((response) => {
        if (response.status === 401) {
          navigate("/login");
        }
        return response.json();
      })
      .then((res) => {
        console.log(res);
        if (res.result === "success") {
          setRefresh(!refresh);
          vanishAlert(
            "Good job!",
            "Exam-Topic details updated successfully!",
            "success",
            2000,
            false
          );
        }
        if (res.result === "error") {
          vanishAlert("Error!", res._ERROR_MESSAGE_, "error", 2000, false);
        }
      });
  }

  return (
    <>
      <div className="modal-header">
        <h1 className="modal-title fs-5" id="exampleModalLabel">
          <b> Edit Exam-Topic-Mapping</b>
        </h1>
        <button
          type="button"
          className="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>
      <form onSubmit={submitExamTopicForm}>
        <div className="modal-body">
          <div className="mb-3">
            <label htmlFor="examName" className="form-label">
              Exam Name
            </label>
            <input
              disabled="true"
              type="text"
              value={obj["examName"]}
              className="form-control"
              id="examName"
            />
          </div>

          <div className="mb-3">
            <label htmlFor="topicName" className="form-label">
              Topic Name
            </label>
            <input
              disabled="true"
              type="text"
              value={obj["topicName"]}
              className="form-control"
              id="ExamName"
            />
          </div>

          <div className="mb-3">
            <label htmlFor="percentage" className="form-label">
              percentage
            </label>
            <input
              type="text"
              value={percentage}
              onChange={(e) => setPercentage(e.target.value)}
              className="form-control"
              id="percentage"
            />
          </div>

          <div className="mb-3">
            <label htmlFor="topicPassPercentage" className="form-label">
              topicPassPercentage
            </label>
            <input
              type="text"
              value={topicPassPercentage}
              onChange={(e) => setTopicPassPercentage(e.target.value)}
              className="form-control"
              id="topicPassPercentage"
            />
          </div>
        </div>
        <div className="modal-footer">
          <button
            type="button"
            className="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Close
          </button>
          <button
            type="submit"
            id="submit"
            className="btn btn-primary"
            data-bs-dismiss="modal"
          >
            Save changes
          </button>
        </div>
      </form>
    </>
  );
};

export default EditExamTopic;
