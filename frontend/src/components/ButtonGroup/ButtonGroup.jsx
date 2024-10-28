import * as React from "react";
import cn from "classnames";
import "./ButtonGroup.scss";
import Button from "../Button/Button";

const ButtonGroup = ({ options, disabled, className, onChange, value }) => {
  const handleClick = (option, event) => {
    if (option.onClick) {
      option.onClick(event);
    }
    if (onChange) {
      onChange(option.value);
    }
  };
  return (
    <div className={cn("ButtonGroup", className)}>
      {options.map((option, index) => (
        <Button
          key={index}
          disabled={disabled}
          type={"button"}
          active={option.value === value}
          className={cn("ButtonGroupButton", {
            "ButtonGroupButton-active": option.value === value,
          })}
          onClick={(event) => handleClick(option, event)}
          variant={option.value === value ? "primary" : "secondary"}
        >
          {option.label}
        </Button>
      ))}
    </div>
  );
};
export default ButtonGroup;
