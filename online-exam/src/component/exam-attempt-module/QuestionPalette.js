import React from 'react'
import PaletteButton from './PaletteButton';

const QuestionPalette = ({questions,onSequenceNumClick}) => {



  return (
    <> {questions &&  (
        questions.map((question, index) => (
         <PaletteButton question={question}  key={question.sequenceNum} onSequenceNumClick={onSequenceNumClick} />
        ))
      ) }</>
  )
}

export default QuestionPalette