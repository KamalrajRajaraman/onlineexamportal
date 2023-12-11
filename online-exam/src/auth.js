import {useState,useContext, createContext } from "react";

const AuthContext = createContext(null);

export const AuthProvider =({children})=>{
    const[user,setUser]=useState(null);
    const[authToken,SetAuthToken]=useState("");

    const login=(user,authToken)=>{
        console.log(authToken)
        setUser(user);
        SetAuthToken(authToken)

    }
    const logout=()=>{
        setUser(null);
    }
    return(
        <AuthContext.Provider value={{user,login,logout,authToken}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth=()=>{
    return useContext(AuthContext);
}
