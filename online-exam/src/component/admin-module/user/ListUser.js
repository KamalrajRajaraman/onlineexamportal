import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import AccordinMaker from '../../common/AccordinMaker';
import { CONTROL_SERVLET, DELETE, DOMAIN_NAME, GET, PORT_NO,  PROTOCOL, WEB_APPLICATION, alert, swalFireAlert, vanishAlert } from "../../common/CommonConstants";

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
        const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
          "findAllUsers",GET
        );
        if (res.status === 401) {
          navigate("/login");
        }
        const data = await res.json();
         if (data.result==="error"){
          swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
        }
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
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`deleteUser?partyId=${id}`,
     DELETE);
      if (res.status === 401) {
        navigate("/login");
      }
      const data =await  res.json();
     
      const {result} = data
      if(result==="success"){
        vanishAlert( "Good job!","Deleted User successfully!", "success",2000,false);
        fetchUsers();
      } 
      else if (result==="error"){
        swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
      }
    }

  return (
    <> {users? <AccordinMaker
    objects={users}
    id={"partyId"}
    name={"firstName"}
    onDelete={onDelete}
    onEdit={onEdit}
  />:"No user Found .Please Add User"}</>
  )
}

export default ListUser