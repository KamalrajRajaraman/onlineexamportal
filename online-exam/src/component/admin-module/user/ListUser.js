import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import AccordinMaker from '../../common/AccordinMaker';
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
import Swal from 'sweetalert2';
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
          "findAllUsers",{ credentials: 'include'}
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
      const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`deleteUser?partyId=${id}`,
      {credentials: 'include'})
      const data =await  res.json();
      console.log(data);
      const {result} = data
      if(result==="success"){
        Swal.fire({
          title: "Good job!",
          text: "Deleted User successfully!",
          icon: "success",
          timer:2000,
          showConfirmButton:false
        });
        fetchUsers();
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