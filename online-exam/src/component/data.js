import { createContext,useContext,useState } from "react";

const DataContext = createContext(null);

export const DataProvider =({children})=>{
    const [store,setStore] =useState([]);
    const addData =(newData)=>{
        console.log(newData)
        setStore([...newData]);
        console.log(store);
    }

    const getData=()=>{
        return store;
    }
    const removeData=()=>{

    }
    return(
        <DataContext.Provider value={{addData,getData,removeData}}>
            {children}
        </DataContext.Provider>
    )
}

export const useData=()=>{
    return useContext(DataContext);
}