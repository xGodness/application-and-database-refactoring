import * as React from "react";

import cn from "classnames";
import "./Button.scss";

const Button = ({
  children,
  onClick,
  active,
  disabled,
  className,
  type,
  variant,
  id,
  ...rest
}) => {
  return (
    <button
      {...rest}
      type={type}
      onClick={onClick}
      className={cn("Button", className, {
        [`Button_${variant}`]: variant,
        [`Button_active`]: active,
        [`Button_disabled`]: disabled,
      })}
    >
      <span className={cn("ButtonText")} id={id}>{children}</span>
    </button>
  );
};

export default Button;
