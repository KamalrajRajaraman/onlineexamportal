import React from 'react'

const Anchor = ({text,onClick,showPage}) => {
  return (
    <>
          <a className={`nav-link ${showPage? "active":""}`}aria-current="page" href="#" onClick={onClick}  >
                  {text}
         </a>
    </>
  )
}

export default Anchor