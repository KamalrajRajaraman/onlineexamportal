import React from 'react'

const UserAttemptDetailsTable = ({userAttemptRecord,topicResult}) => {
  return (<>
    <table className="table table-striped border">
        <thead>
          <tr>
            <th scope="col">Exam Name</th>
            <th scope="col">Total Correct</th>
            <th scope="col">Total Wrong</th>
            <th scope="col">Total Question</th>
            <th scope="col">Score</th>
            <th scope="col">Result</th>
            
          </tr>
        </thead>
        <tbody>
          <tr>
            <th scope="row">{userAttemptRecord.examName}</th>
            <td> {userAttemptRecord.totalCorrect}</td>
            <td> {userAttemptRecord.totalWrong}</td>
            <td> {userAttemptRecord.noOfQuestions}</td>
            <td> {userAttemptRecord.score}</td>
            <td>{userAttemptRecord.userPassed}</td>
          </tr>
         
        </tbody>
      </table>
      <div className="mt-3">
        <h4>Topicwise Results</h4>
      </div>
      <table class="table table-striped border ">
      <thead>
         
        <tr >
          <th scope="col">TopicId</th>
          <th scope="col">Topic pass percentage</th>
          <th scope="col">User topic percentage</th>
          <th scope="col">User passed this topic</th>
        </tr>
        </thead>
        <tbody>
        {topicResult &&
          topicResult.map((topic) => (
           
              <tr className="mt-4" key={topic.topicId}>
                <td>{topic.topicName}</td>
                <td>{topic.topicPassPercentage}</td>
                <td>{topic.userTopicPercentage}</td>
                <td>{topic.userPassedThisTopic}</td>
              </tr>
            
          ))}</tbody>
      </table></>
  )
}

export default UserAttemptDetailsTable