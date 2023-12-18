import { useState } from "react";
import QuestionTableRow from "./QuestionTableRow";

const EditExamAccordin = ({ object, name, onDelete, id, onEdit, topics }) => {
  const [toggle, setToggle] = useState(true);
  const onClick = () => {
    setToggle(!toggle);
  };

  const questionList = () => {  
    const questions = topics.filter((obj) => object[id] === obj.topicId);
    return questions;
  };

  return (
    <div className=" bg-white border">
      <div className="accordion accordion-flush">
        <div className="accordion-item">
          <h2 className="accordion-header position-relative ">
            <button
              onClick={onClick}
              className={`accordion-button ${toggle && "collapsed"}`}
              type="button"
            >
              <h5>{object[name]}</h5>
              <p></p>
            </button>
            
          </h2>
          <div className={`accordion-collapse ${toggle && "collapse"}`}>
            <div className="accordion-body p-0 ">
              <h5 className="border-bottom border-dark p-1">Questions</h5>
                <table className="table">
                  <thead>
                    <tr>
                      <th scope="col">Question Id</th>
                      <th scope="col">Question</th>
                    </tr>
                  </thead>
                  <tbody>
                    <QuestionTableRow array={questionList()}/>
                  </tbody>
                </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditExamAccordin;
