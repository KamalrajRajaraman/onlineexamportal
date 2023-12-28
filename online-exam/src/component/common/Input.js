import React, { useState } from "react";

const Input = ({
  name,
  text,
  state,
  setStateFun,
  type,
  placeholder,
  passView,
}) => {
  const [typePass, setTypePass] = useState(true);
  const onClick = () => {
    setTypePass(!typePass);
  };
  return (
    <div className="mb-3">
      <label htmlFor={name} className="form-label">
        {text}
      </label>
      <input
        type={typePass ? type : "text"}
        className="form-control"
        id={name}
        value={state}
        onChange={(e) => setStateFun(e.target.value)}
        placeholder={placeholder}
        security=""
      />
      {passView && type === "password" && (
        <>
          <input type="checkbox" onClick={onClick} /> Show Password
        </>
      )}
    </div>
  );
};

export default Input;
