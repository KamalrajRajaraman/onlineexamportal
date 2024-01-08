import React, { useEffect, useState } from 'react'

const PaletteButton = ({onSequenceNumClick ,question}) => {
    const [mounted,setMounted]=useState(false);
   
    const [color,setColor] =useState("btn-outline-secondary");

    useEffect(()=>{
        setMounted(true);    
    },[])
    
    useEffect(()=>{
        checkAnswered();
    });
    
    const checkAnswered =()=>{
        if(mounted){
            if(question?.isFlagged){
                setColor("btn-danger")  
            }
            else if(question?.submittedAnswer){
                setColor("btn-success")     
            }else if(question?.isViewed){
                setColor("btn-warning")
            }         
        }
    }

    const HandleClick =(sequenceNum)=>{
        if(color!=="btn-success" &&  color!=="btn-danger"){
            setColor("btn-warning")
        }
        onSequenceNumClick(sequenceNum);
    
    }
  return (
    <div className="btn-group m-2" role="group">
    <button
      type="button"
      className={`btn  px-3 ${ color }`}
      onClick={()=>HandleClick(question.sequenceNum)}
    >
      {question.sequenceNum}
    </button>
  </div>
  )
}

export default PaletteButton