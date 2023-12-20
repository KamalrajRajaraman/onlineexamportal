import React, { useEffect, useState } from 'react'

const useComponent = () => {
    const [secondsLeft, setSecondsLeft]= useState();

    useEffect(()=>{
        if(secondsLeft<=0) return;

        const timeOut = setTimeout(()=>{
            setSecondsLeft(secondsLeft-1);
        },5000);
        return ()=> clearTimeout(timeOut);
    }, [secondsLeft]);

    function start(number){
        setSecondsLeft(number);
    }

    return {secondsLeft, start};
}

export default useComponent
