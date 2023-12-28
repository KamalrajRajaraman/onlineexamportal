import React from "react";

const TableRowMaker = ({array}) => {
  return (<>{
    array.map((obj)=>(<tr key={obj.topicId}>
        <th scope="row">{obj["examId"]}</th>
        <td>{obj["examName"]}</td>
        <td>{obj["topicId"]}</td>
        <td>{obj["topicName"]}</td>
        <td>{obj["percentage"]}</td>
        <td>{obj["topicPassPercentage"]}</td>
        <td>{obj["questionsPerExam"]}</td>
      </tr>))}
    </>
  );
};

export default TableRowMaker;
