import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import AccordinMaker from '../../common/AccordinMaker';

const ListUser = () => {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();
    useEffect(() => {
        fetchUsers();
        return () => {
          setUsers([]);
        };
      }, []);
    
      const fetchUsers = async () => {
        const res = await fetch(
          "https://localhost:8443/onlineexam/control/findAllUsers",{ credentials: 'include'}
        );
    
        const data = await res.json();
    
        const { userList } = data;
    
        setUsers(userList);
      };
    
      const onEdit = (id,user) => {
        console.log(user);
        navigate(`edit/userId/${id}`,{state:user});
      };

    

    //Delete Exam
    const onDelete=async(id)=>{
      console.log(id);
      const res = await fetch(`https://localhost:8443/onlineexam/control/deleteUser?partyId=${id}`,
      {credentials: 'include'})
      const data =await  res.json();
      console.log(data);
      const {result} = data
      if(result==="success"){
        fetchUsers();
      }
    }

  return (
    <>  <AccordinMaker
    objects={users}
    id={"partyId"}
    name={"firstName"}
    onDelete={onDelete}
    onEdit={onEdit}
  /></>
  )
}

export default ListUser