import Swal from "sweetalert2";

export const PROTOCOL ="https://"
export const DOMAIN_NAME = window.location.hostname;
export const PORT_NO =":8443"
export const WEB_APPLICATION="/onlineexam";
export const CONTROL_SERVLET ="/control/";
export const GET = {credentials: 'include'};
export const DELETE ={ method: "DELETE", credentials: "include" }

export const POST = {
    method: "POST",
    headers: {
      "Content-type": "application/json",
    },
    credentials: "include",
};

export const PUT =  { method: "PUT",
headers: {
  'Content-type':"application/json"
},
 credentials: 'include',
}

export const swalFireAlert =(title,text,icon)=>{
    Swal.fire({
        title:title,
        text:text,
        icon: icon,       
      });
}
export const vanishAlert =(title,text,icon,timer,showConfirmButton)=>{
    Swal.fire({
        title:title,
        text:text,
        icon: icon, 
        timer:timer,
        showConfirmButton:showConfirmButton      
      });
}

