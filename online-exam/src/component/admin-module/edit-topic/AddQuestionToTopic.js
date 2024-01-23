import React, { useContext } from 'react'
import AddQuestions from '../question/AddQuestions'
import { useParams } from 'react-router-dom';
import { EditTopicContext } from './EditTopic';

const AddQuestionToTopic = () => {
  const {fetchQuestion}=useContext(EditTopicContext);
    const {topicId} = useParams();
   
  return (
    <div>
     <AddQuestions topicId={topicId} isTopicWiseAddQuestion={true} fetchQuestionbyTopicId={fetchQuestion} />
    </div>
  )
}

export default AddQuestionToTopic
