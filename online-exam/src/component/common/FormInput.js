import React, { useState } from 'react'

const FormInput = ({id,value,onChange,placeholder,text,type,passView,name,error}) => {
    const [typePass, setTypePass] = useState(true);
    const onClick = () => {
      setTypePass(!typePass);
    };
  return (
    <div className="mb-3">
    <label htmlFor={id} className="form-label">
      {text}
    </label>
    <input
      type={typePass ? type : "text"}
      className="form-control"
      id={id}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      security=""
    />
    <small className='text-danger'>{error}</small>
    {passView && type === "password" && (
      <>
        <input type="checkbox" onClick={onClick} /> Show Password
      </>
    )}
  </div>
  )
}

export default FormInput