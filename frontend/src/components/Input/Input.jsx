import React from "react";
import PropTypes from "prop-types";
import cn from "classnames";
import "./Input.scss";

const Input = ({ onChange, disabled, className, ...rest }) => {
  return (
    <input
      // disabled={disabled}
      onChange={onChange}
      className={cn("Input", className)}
      {...rest}
    />
  );
};
export default Input;
