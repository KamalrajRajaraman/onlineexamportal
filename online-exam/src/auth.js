import {useState,useContext, createContext } from "react";

const AuthContext = createContext(null);

export const AuthProvider =({children})=>{
    const[user,setUser]=useState(null);
    const[partyRole,SetPartyRole]=useState(null);

    const login=(user,partyRole)=>{
       
        setUser(user);
        SetPartyRole(partyRole);

    }
    const logout=()=>{
        setUser(null);
        SetPartyRole(null);
    }
    return(
        <AuthContext.Provider value={{user,login,logout,partyRole}}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth=()=>{
    return useContext(AuthContext);
}
