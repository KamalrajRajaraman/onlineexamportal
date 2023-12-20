import React from 'react'

const QuestionNav = () => {
  return (
    <div className='mt-3'>
      <button type="button" class="btn btn-outline-primary me-1">Mark for Review</button>
      <button type="button" class="btn btn-outline-primary">Reset</button>
      <button type="button" class="btn btn-outline-success float-end">Save & Next</button>

    </div>
  )
}

export default QuestionNav
