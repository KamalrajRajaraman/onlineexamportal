import React from 'react'

const QuestionTiles = ({questions,setQuestion}) => {
    return (
        <div>
            <hr />
            <h4>You are viewing Topic A </h4>
            <br/>
            <h6>Questions Palette: </h6>
            <div class="btn-toolbar mx-4" role="toolbar" aria-label="Toolbar with button groups">
            
            {questions? questions.map((question,index)=>(

            <div class="btn-group m-2" role="group" key={index}>
                <button type="button" class="btn btn-outline-secondary px-3" onClick={()=>setQuestion(index)}>{index+1}</button>
            </div>
            )):<></>}
           
        </div>
        </div>
    )
}

export default QuestionTiles
