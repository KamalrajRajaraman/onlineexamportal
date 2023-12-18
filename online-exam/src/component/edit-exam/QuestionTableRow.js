import React from 'react'

const QuestionTableRow = ({array}) => {
   
        return (<>{
          array.map((obj)=>(<tr key={obj.questionId}>
              <th scope="row">{obj["questionId"]}</th>
              <td>{obj["questionDetail"]}</td>
            </tr>))}
          </>
        );
      
}

export default QuestionTableRow