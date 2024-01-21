import React from 'react'
import AddQuestions from '../question/AddQuestions'
import { useParams } from 'react-router-dom';

const AddQuestionToTopic = () => {
    const {topicId} = useParams();
    console.log('topicId from add question', topicId);
  return (
    <div>
     <AddQuestions topicId={topicId} />
    </div>
  )
}

export default AddQuestionToTopic
