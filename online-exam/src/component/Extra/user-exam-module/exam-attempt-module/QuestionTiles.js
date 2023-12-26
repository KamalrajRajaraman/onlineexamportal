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
            {/* <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">2</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">3</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">4</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">5</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">6</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">7</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">8</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary px-3">9</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary ">10</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary">11</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary">12</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary">13</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary">14</button>
            </div>
            <div class="btn-group m-2" role="group">
                <button type="button" class="btn btn-outline-secondary">15</button>
            </div> */}
           

        </div>
        </div>
    )
}

export default QuestionTiles
