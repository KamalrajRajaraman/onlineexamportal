import { useState } from "react";
import { useNavigate } from "react-router-dom";

const ExamAccordion = ({ object, name, onDelete, id, onEdit }) => {
  const navigate = useNavigate();

  const [toggle, setToggle] = useState(true);
  const onClick = () => {
    setToggle(!toggle);
  };

  return (
    <div className=" container bg-white border">
      <div className="accordion accordion-flush">
        <div className="accordion-item">
          <h2 className="accordion-header position-relative ">
            <button
              onClick={onClick}
              className={`accordion-button ${toggle && "collapsed"}`}
              type="button"
            >
              <h5>{object.examName}</h5>
              <p></p>
            </button>
            <div className="  row z-3 position-absolute end-0 me-5  top-0 mt-3 ">
              <button
                onClick={() => navigate(`exam-page/${object.examId}`)}
                className="btn btn-outline-success  btn-sm me-4 col"
              >
                Take Exam
              </button>
            </div>
          </h2>
          <div className={`accordion-collapse ${toggle && "collapse"}`}>
            <div className="accordion-body">
              <div className="row">
                <div className="col">
                  <table>
                    <tbody>
                      <tr>
                        <th>allowedAttempts</th>
                        <th>:</th>
                        <td>{object.allowedAttempts}</td>
                      </tr>

                      <tr>
                        <th>noOfAttempts</th>
                        <th>:</th>
                        <td>{object.noOfAttempts}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div className="col">
                  <table>
                    <tbody>
                      <tr>
                        <th>lastPerformanceDate</th>
                        <th>:</th>
                        <td>{object.lastPerformanceDate}</td>
                      </tr>
                      <tr>
                        <th>maxSplitAttempts</th>
                        <th>:</th>
                        <td>{object.maxSplitAttempts}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExamAccordion;
