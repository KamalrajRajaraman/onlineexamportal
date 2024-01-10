import React from "react";
import { TiEdit } from "react-icons/ti"
import { useState } from "react";
import EditExamTopic from "./EditExamTopic";

const TableRowMaker = ({ array }) => {

  return (<>{
    array.map((obj) => (<><tr key={obj.topicId}>
      <th scope="row">{obj["examId"]}</th>
      <td>{obj["examName"]}</td>
      <td>{obj["topicId"]}</td>
      <td>{obj["topicName"]}</td>
      <td>{obj["percentage"]}</td>
      <td>{obj["topicPassPercentage"]}</td>
      <td>{obj["questionsPerExam"]}</td>
      <td><TiEdit type="button"
        data-bs-toggle="modal"
        data-bs-target={`#exampleModal${obj["topicId"]}`} />
      </td>
      </tr>

      <div className="modal fade" id={`exampleModal${obj["topicId"]}`} tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"   >

        <div className="modal-dialog modal-dialog-centered">

          <div className="modal-content">
            <EditExamTopic obj={obj}/>
          </div>

        </div>
      </div>
      </>
    ))
  }
  </>
  );
};

export default TableRowMaker;
